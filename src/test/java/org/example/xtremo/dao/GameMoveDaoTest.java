package org.example.xtremo.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.entity.Game;
import org.example.xtremo.model.entity.GameMove;
import org.example.xtremo.model.entity.Player;
import org.example.xtremo.model.enums.GameResult;
import org.example.xtremo.model.enums.PlayerStatus;
import org.example.xtremo.model.enums.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameMoveDao Tests")
class GameMoveDaoTest {

    private GameMoveDao gameMoveDao;
    private GameDao gameDao;
    private PlayerDao playerDao;
    private Connection connection;
    private static final Gson gson = new Gson();

    @BeforeEach
    void setUp() throws SQLException {
        connection = DBConnection.getConnection();
        gameMoveDao = new GameMoveDaoImpl(connection);
        gameDao = new GameDaoImpl(connection);
        playerDao = new PlayerDaoImpl(connection);
    }

    @Test
    @DisplayName("Should save game move and return entity with generated ID")
    void testSaveGameMove() throws SQLException {
        Player player = new Player("playermove1_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(savedPlayer.getId());
        game.setPlayer2Id(savedPlayer.getId());
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);
        Game savedGame = gameDao.save(game);
        
        GameMove move = new GameMove();
        move.setGameId(savedGame.getGameId());
        move.setPlayerId(savedPlayer.getId());
        move.setMoveNumber(1);
        move.setMoveData("{\"row\": 0, \"col\": 0}");
        
        GameMove savedMove = gameMoveDao.save(move);
        
        assertNotNull(savedMove, "Saved move should not be null");
        assertTrue(savedMove.getMoveId() > 0, "Saved move should have a generated ID");
        assertEquals(savedGame.getGameId(), savedMove.getGameId(), "Game ID should match");
        assertEquals(savedPlayer.getId(), savedMove.getPlayerId(), "Player ID should match");
        assertEquals(1, savedMove.getMoveNumber(), "Move number should match");
        JsonObject expectedData = gson.fromJson("{\"row\": 0, \"col\": 0}", JsonObject.class);
        JsonObject actualData = gson.fromJson(savedMove.getMoveData(), JsonObject.class);
        assertEquals(expectedData, actualData, "Move data should match");
    }

    @Test
    @DisplayName("Should find game move by ID after saving")
    void testFindById() throws SQLException {
        Player player = new Player("playermove2_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(savedPlayer.getId());
        game.setPlayer2Id(savedPlayer.getId());
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);
        Game savedGame = gameDao.save(game);
        
        GameMove move = new GameMove();
        move.setGameId(savedGame.getGameId());
        move.setPlayerId(savedPlayer.getId());
        move.setMoveNumber(1);
        move.setMoveData("{\"row\": 1, \"col\": 1}");
        
        GameMove savedMove = gameMoveDao.save(move);
        
        var retrievedMove = gameMoveDao.findById(savedMove.getMoveId());
        
        assertTrue(retrievedMove.isPresent(), "Move should be found by ID");
        assertEquals(savedMove.getMoveId(), retrievedMove.get().getMoveId(), "Move IDs should match");
        assertEquals(savedGame.getGameId(), retrievedMove.get().getGameId(), "Game ID should match");
        assertEquals(savedPlayer.getId(), retrievedMove.get().getPlayerId(), "Player ID should match");
        assertEquals(1, retrievedMove.get().getMoveNumber(), "Move number should match");
        JsonObject expectedData = gson.fromJson("{\"row\": 1, \"col\": 1}", JsonObject.class);
        JsonObject actualData = gson.fromJson(retrievedMove.get().getMoveData(), JsonObject.class);
        assertEquals(expectedData, actualData, "Move data should match");
    }

    @Test
    @DisplayName("Should find moves by game ID")
    void testFindByGameId() throws SQLException {
        Player player1 = new Player("playermove3a_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player player2 = new Player("playermove3b_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer1 = playerDao.save(player1);
        Player savedPlayer2 = playerDao.save(player2);
        
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(savedPlayer1.getId());
        game.setPlayer2Id(savedPlayer2.getId());
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);
        Game savedGame = gameDao.save(game);
        
        GameMove move1 = new GameMove();
        move1.setGameId(savedGame.getGameId());
        move1.setPlayerId(savedPlayer1.getId());
        move1.setMoveNumber(1);
        move1.setMoveData("{\"row\": 0, \"col\": 0}");
        
        GameMove move2 = new GameMove();
        move2.setGameId(savedGame.getGameId());
        move2.setPlayerId(savedPlayer2.getId());
        move2.setMoveNumber(2);
        move2.setMoveData("{\"row\": 0, \"col\": 1}");
        
        GameMove savedMove1 = gameMoveDao.save(move1);
        GameMove savedMove2 = gameMoveDao.save(move2);
        
        List<GameMove> moves = gameMoveDao.findByGameId(savedGame.getGameId());
        
        assertFalse(moves.isEmpty(), "Should find at least one move for the game");
        assertTrue(moves.size() >= 2, "Should find at least two moves");
        assertTrue(moves.stream().anyMatch(m -> m.getMoveId() == savedMove1.getMoveId()), 
            "Should contain the first saved move");
        assertTrue(moves.stream().anyMatch(m -> m.getMoveId() == savedMove2.getMoveId()), 
            "Should contain the second saved move");
        
        for (int i = 0; i < moves.size() - 1; i++) {
            assertTrue(moves.get(i).getMoveNumber() <= moves.get(i + 1).getMoveNumber(),
                "Moves should be ordered by move number");
        }
    }

    @Test
    @DisplayName("Should return empty list when game has no moves")
    void testFindByGameIdEmpty() throws SQLException {
        List<GameMove> moves = gameMoveDao.findByGameId(99999);
        
        assertNotNull(moves);
        assertTrue(moves.isEmpty());
    }

    @Test
    @DisplayName("Should find all game moves")
    void testFindAll() throws SQLException {
        Player player1 = new Player("playermove4a_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player player2 = new Player("playermove4b_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer1 = playerDao.save(player1);
        Player savedPlayer2 = playerDao.save(player2);
        
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(savedPlayer1.getId());
        game.setPlayer2Id(savedPlayer2.getId());
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);
        Game savedGame = gameDao.save(game);
        
        GameMove move1 = new GameMove();
        move1.setGameId(savedGame.getGameId());
        move1.setPlayerId(savedPlayer1.getId());
        move1.setMoveNumber(1);
        move1.setMoveData("{\"row\": 0, \"col\": 0}");
        
        GameMove move2 = new GameMove();
        move2.setGameId(savedGame.getGameId());
        move2.setPlayerId(savedPlayer2.getId());
        move2.setMoveNumber(2);
        move2.setMoveData("{\"row\": 1, \"col\": 1}");
        
        GameMove savedMove1 = gameMoveDao.save(move1);
        GameMove savedMove2 = gameMoveDao.save(move2);
        
        List<GameMove> allMoves = gameMoveDao.findAll();
        
        assertNotNull(allMoves);
        assertTrue(allMoves.size() >= 2);
        assertTrue(allMoves.stream().anyMatch(m -> m.getMoveId() == savedMove1.getMoveId()));
        assertTrue(allMoves.stream().anyMatch(m -> m.getMoveId() == savedMove2.getMoveId()));
    }

    @Test
    @DisplayName("Should delete game move by ID successfully")
    void testDeleteById() throws SQLException {
        Player player = new Player("playermove5_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(savedPlayer.getId());
        game.setPlayer2Id(savedPlayer.getId());
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);
        Game savedGame = gameDao.save(game);
        
        GameMove move = new GameMove();
        move.setGameId(savedGame.getGameId());
        move.setPlayerId(savedPlayer.getId());
        move.setMoveNumber(1);
        move.setMoveData("{\"row\": 0, \"col\": 0}");
        
        GameMove savedMove = gameMoveDao.save(move);
        int moveId = savedMove.getMoveId();
        
        assertTrue(gameMoveDao.findById(moveId).isPresent());
        
        boolean deleteResult = gameMoveDao.deleteById(moveId);
        
        assertTrue(deleteResult);
        assertFalse(gameMoveDao.findById(moveId).isPresent());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent move")
    void testDeleteByIdNotFound() throws SQLException {
        boolean deleteResult = gameMoveDao.deleteById(99999);
        
        assertFalse(deleteResult);
    }

    @Test
    @DisplayName("Should delete all moves by game ID successfully")
    void testDeleteByGameId() throws SQLException {
        Player player1 = new Player("playermove6a_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player player2 = new Player("playermove6b_" + System.currentTimeMillis(), "pass", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer1 = playerDao.save(player1);
        Player savedPlayer2 = playerDao.save(player2);
        
        Game game = new Game();
        game.setGameType(GameType.TIC_TAC_TOE);
        game.setGameResult(GameResult.IN_PROGRESS);
        game.setPlayer1Id(savedPlayer1.getId());
        game.setPlayer2Id(savedPlayer2.getId());
        game.setStartedAt(LocalDateTime.now());
        game.setRecorded(false);
        Game savedGame = gameDao.save(game);
        
        GameMove move1 = new GameMove();
        move1.setGameId(savedGame.getGameId());
        move1.setPlayerId(savedPlayer1.getId());
        move1.setMoveNumber(1);
        move1.setMoveData("{\"row\": 0, \"col\": 0}");
        
        GameMove move2 = new GameMove();
        move2.setGameId(savedGame.getGameId());
        move2.setPlayerId(savedPlayer2.getId());
        move2.setMoveNumber(2);
        move2.setMoveData("{\"row\": 1, \"col\": 1}");
        
        GameMove savedMove1 = gameMoveDao.save(move1);
        GameMove savedMove2 = gameMoveDao.save(move2);
        
        assertTrue(gameMoveDao.findById(savedMove1.getMoveId()).isPresent());
        assertTrue(gameMoveDao.findById(savedMove2.getMoveId()).isPresent());
        
        boolean deleteResult = gameMoveDao.deleteByGameId(savedGame.getGameId());
        
        assertTrue(deleteResult);
        assertFalse(gameMoveDao.findById(savedMove1.getMoveId()).isPresent());
        assertFalse(gameMoveDao.findById(savedMove2.getMoveId()).isPresent());
        assertTrue(gameMoveDao.findByGameId(savedGame.getGameId()).isEmpty());
    }

    @Test
    @DisplayName("Should return false when deleting moves for non-existent game")
    void testDeleteByGameIdNotFound() throws SQLException {
        boolean deleteResult = gameMoveDao.deleteByGameId(99999);
        
        assertFalse(deleteResult);
    }

    @Test
    @DisplayName("Should return empty Optional when move not found by ID")
    void testFindByIdNotFound() throws SQLException {
        var retrievedMove = gameMoveDao.findById(99999);
        
        assertFalse(retrievedMove.isPresent(), "Move should not be found");
    }
}

