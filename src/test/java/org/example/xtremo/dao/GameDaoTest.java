package org.example.xtremo.dao;

import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.entity.Game;
import org.example.xtremo.model.enums.GameType;
import org.example.xtremo.model.enums.GameResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameDao Tests")
class GameDaoTest {

    private GameDao gameDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        connection = DBConnection.getConnection();
        gameDao = new GameDaoImpl(connection);
    }

    @Test
    @DisplayName("Should save game and return entity with generated ID")
    void testSaveGame() {
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.WIN);
        game.setPlayer1Id(1);
        game.setPlayer2Id(2);
        game.setWinnerId(1);
        game.setStartedAt(LocalDateTime.now());
        game.setEndedAt(LocalDateTime.now());
        game.setRecorded(false);
        game.setRecordFilePath(null);

        Game savedGame = gameDao.save(game);

        assertNotNull(savedGame);
        assertTrue(savedGame.getGameId() > 0);
        assertEquals(GameType.TIC_TAC_TOE, savedGame.getGameType());
        assertEquals(GameResult.WIN, savedGame.getGameResult());
        assertEquals(1, savedGame.getPlayer1Id());
        assertEquals(2, savedGame.getPlayer2Id());
        assertEquals(1, savedGame.getWinnerId());
        assertNotNull(savedGame.getStartedAt());
        assertNotNull(savedGame.getEndedAt());
    }

    @Test
    @DisplayName("Should find game by ID after saving")
    void testFindById() {
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.WIN);
        game.setPlayer1Id(1);
        game.setPlayer2Id(2);
        game.setWinnerId(2);
        game.setStartedAt(LocalDateTime.now());
        game.setEndedAt(LocalDateTime.now());
        game.setRecorded(true);
        game.setRecordFilePath("/records/game1.json");

        Game savedGame = gameDao.save(game);
        Optional<Game> retrieved = gameDao.findById(savedGame.getGameId());

        assertTrue(retrieved.isPresent());
        Game g = retrieved.get();
        assertEquals(savedGame.getGameId(), g.getGameId());
        assertEquals(GameType.TIC_TAC_TOE, g.getGameType());
        assertEquals(GameResult.WIN, g.getGameResult());
        assertEquals(2, g.getWinnerId());
        assertTrue(g.isRecorded());
    }

    @Test
    @DisplayName("Should return empty Optional when game not found by ID")
    void testFindByIdNotFound() {
        Optional<Game> retrieved = gameDao.findById(99999);
        assertFalse(retrieved.isPresent());
    }

    @Test
    @DisplayName("Should find games by player ID")
    void testFindByPlayerId() {
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.WIN);
        game.setPlayer1Id(1);
        game.setPlayer2Id(2);
        game.setWinnerId(1);
        game.setStartedAt(LocalDateTime.now());
        game.setEndedAt(LocalDateTime.now());
        game.setRecorded(false);
        game.setRecordFilePath(null);

        Game savedGame = gameDao.save(game);
        List<Game> games = gameDao.findByPlayerId(1);

        assertFalse(games.isEmpty());
        assertTrue(games.stream().anyMatch(g -> g.getGameId() == savedGame.getGameId()));
    }

    @Test
    @DisplayName("Should find all games")
    void testFindAll() {
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(1);
        game.setPlayer2Id(2);
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);

        gameDao.save(game);
        List<Game> allGames = gameDao.findAll();

        assertFalse(allGames.isEmpty());
        assertTrue(allGames.stream().allMatch(g -> g.getGameType() == GameType.TIC_TAC_TOE));
    }

    @Test
    @DisplayName("Should update existing game")
    void testUpdateGame() {
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(1);
        game.setPlayer2Id(2);
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);

        Game saved = gameDao.save(game);

        saved.setGameResult(GameResult.DRAW);
        saved.setWinnerId(null);
        boolean updated = gameDao.update(saved);

        assertTrue(updated);
        Game updatedGame = gameDao.findById(saved.getGameId()).orElseThrow();
        assertEquals(GameResult.DRAW, updatedGame.getGameResult());
        assertNull(updatedGame.getWinnerId());
    }

    @Test
    @DisplayName("Should delete game by ID")
    void testDeleteById() {
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.WIN);
        game.setPlayer1Id(1);
        game.setPlayer2Id(2);
        game.setWinnerId(1);
        game.setStartedAt(LocalDateTime.now());

        Game saved = gameDao.save(game);
        boolean deleted = gameDao.deleteById(saved.getGameId());

        assertTrue(deleted);
        assertFalse(gameDao.findById(saved.getGameId()).isPresent());
    }

    @Test
    @DisplayName("Should return empty list when player has no games")
    void testFindByPlayerIdEmpty() {
        List<Game> games = gameDao.findByPlayerId(99999);

        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    @DisplayName("Should return false when updating non-existent game")
    void testUpdateNonExistentGame() {
        Game nonExistentGame = new Game();
        nonExistentGame.setGameId(99999);
        nonExistentGame.setGameType(GameType.TIC_TAC_TOE);
        nonExistentGame.setGameResult(GameResult.WIN);
        nonExistentGame.setPlayer1Id(1);
        nonExistentGame.setPlayer2Id(2);
        nonExistentGame.setStartedAt(LocalDateTime.now());

        boolean updateResult = gameDao.update(nonExistentGame);

        assertFalse(updateResult);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent game")
    void testDeleteByIdNotFound() {
        boolean deleteResult = gameDao.deleteById(99999);

        assertFalse(deleteResult);
    }
}
