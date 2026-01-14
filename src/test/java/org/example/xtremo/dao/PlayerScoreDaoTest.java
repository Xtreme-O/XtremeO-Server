package org.example.xtremo.dao;

import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.entity.Player;
import org.example.xtremo.model.entity.PlayerScore;
import org.example.xtremo.model.enums.GameType;
import org.example.xtremo.model.enums.PlayerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlayerScoreDao Tests")
class PlayerScoreDaoTest {

    private PlayerScoreDao playerScoreDao;
    private PlayerDao playerDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DBConnection.getConnection();
        playerScoreDao = new PlayerScoreDaoImpl(connection);
        playerDao = new PlayerDaoImpl(connection);
    }

    @Test
    @DisplayName("Should save player score and return entity with generated ID")
    void testSavePlayerScore() throws SQLException {
        Player player = new Player("testuser_score1_" + System.currentTimeMillis(), "password123", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        PlayerScore score = new PlayerScore();
        score.setUserId(savedPlayer.getId());
        score.setGameType(GameType.TIC_TAC_TOE.name());
        score.setWins(5);
        score.setLosses(2);
        score.setDraws(1);
        score.setLongestStreak(3);
        
        PlayerScore savedScore = playerScoreDao.save(score);
        
        assertNotNull(savedScore, "Saved score should not be null");
        assertTrue(savedScore.getScoreId() > 0, "Saved score should have a generated ID");
        assertEquals(savedPlayer.getId(), savedScore.getUserId(), "User ID should match");
        assertEquals(GameType.TIC_TAC_TOE.name(), savedScore.getGameType(), "Game type should match");
        assertEquals(5, savedScore.getWins(), "Wins should match");
        assertEquals(2, savedScore.getLosses(), "Losses should match");
        assertEquals(1, savedScore.getDraws(), "Draws should match");
        assertEquals(3, savedScore.getLongestStreak(), "Longest streak should match");
    }

    @Test
    @DisplayName("Should find player score by ID after saving")
    void testFindById() throws SQLException {
        Player player = new Player("testuser_score2_" + System.currentTimeMillis(), "password123", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        PlayerScore score = new PlayerScore();
        score.setUserId(savedPlayer.getId());
        score.setGameType(GameType.TIC_TAC_TOE.name());
        score.setWins(10);
        score.setLosses(5);
        score.setDraws(2);
        score.setLongestStreak(7);
        
        PlayerScore savedScore = playerScoreDao.save(score);
        
        var retrievedScore = playerScoreDao.findById(savedScore.getScoreId());
        
        assertTrue(retrievedScore.isPresent(), "Score should be found by ID");
        assertEquals(savedScore.getScoreId(), retrievedScore.get().getScoreId(), "Score IDs should match");
        assertEquals(savedPlayer.getId(), retrievedScore.get().getUserId(), "User ID should match");
        assertEquals(GameType.TIC_TAC_TOE.name(), retrievedScore.get().getGameType(), "Game type should match");
        assertEquals(10, retrievedScore.get().getWins(), "Wins should match");
        assertEquals(7, retrievedScore.get().getLongestStreak(), "Longest streak should match");
    }

    @Test
    @DisplayName("Should find player score by user ID")
    void testFindByUserId() throws SQLException {
        String uniqueUsername = "testuser_score3_" + System.currentTimeMillis();
        Player player = new Player(uniqueUsername, "password123", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        PlayerScore score = new PlayerScore();
        score.setUserId(savedPlayer.getId());
        score.setGameType(GameType.TIC_TAC_TOE.name());
        score.setWins(8);
        score.setLosses(3);
        score.setDraws(1);
        score.setLongestStreak(5);
        
        PlayerScore savedScore = playerScoreDao.save(score);
        
        var retrievedScore = playerScoreDao.findByUserId(savedPlayer.getId());
        
        assertTrue(retrievedScore.isPresent(), "Score should be found by user ID");
        assertEquals(savedScore.getScoreId(), retrievedScore.get().getScoreId(), "Score IDs should match");
        assertEquals(savedPlayer.getId(), retrievedScore.get().getUserId(), "User ID should match");
        assertEquals(8, retrievedScore.get().getWins(), "Wins should match");
        assertEquals(3, retrievedScore.get().getLosses(), "Losses should match");
    }

    @Test
    @DisplayName("Should return empty Optional when score not found by ID")
    void testFindByIdNotFound() throws SQLException {
        var retrievedScore = playerScoreDao.findById(99999);
        
        assertFalse(retrievedScore.isPresent(), "Score should not be found");
    }

    @Test
    @DisplayName("Should return empty Optional when score not found by user ID")
    void testFindByUserIdNotFound() throws SQLException {
        var retrievedScore = playerScoreDao.findByUserId(99999);
        
        assertFalse(retrievedScore.isPresent(), "Score should not be found");
    }

    @Test
    @DisplayName("Should find all player scores")
    void testFindAll() throws SQLException {
        Player player1 = new Player("testuser_score4_" + System.currentTimeMillis(), "password123", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer1 = playerDao.save(player1);
        
        Player player2 = new Player("testuser_score5_" + System.currentTimeMillis(), "password123", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer2 = playerDao.save(player2);
        
        PlayerScore score1 = new PlayerScore();
        score1.setUserId(savedPlayer1.getId());
        score1.setGameType(GameType.TIC_TAC_TOE.name());
        score1.setWins(5);
        score1.setLosses(2);
        score1.setDraws(1);
        score1.setLongestStreak(3);
        
        PlayerScore score2 = new PlayerScore();
        score2.setUserId(savedPlayer2.getId());
        score2.setGameType(GameType.TIC_TAC_TOE.name());
        score2.setWins(10);
        score2.setLosses(5);
        score2.setDraws(2);
        score2.setLongestStreak(7);
        
        PlayerScore savedScore1 = playerScoreDao.save(score1);
        PlayerScore savedScore2 = playerScoreDao.save(score2);
        
        var allScores = playerScoreDao.findAll();
        
        assertNotNull(allScores);
        assertTrue(allScores.size() >= 2);
        assertTrue(allScores.stream().anyMatch(s -> s.getScoreId() == savedScore1.getScoreId()));
        assertTrue(allScores.stream().anyMatch(s -> s.getScoreId() == savedScore2.getScoreId()));
    }

    @Test
    @DisplayName("Should update existing player score successfully")
    void testUpdatePlayerScore() throws SQLException {
        Player player = new Player("testuser_score6_" + System.currentTimeMillis(), "password123", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        PlayerScore score = new PlayerScore();
        score.setUserId(savedPlayer.getId());
        score.setGameType(GameType.TIC_TAC_TOE.name());
        score.setWins(5);
        score.setLosses(2);
        score.setDraws(1);
        score.setLongestStreak(3);
        
        PlayerScore savedScore = playerScoreDao.save(score);
        
        savedScore.setGameType(GameType.TIC_TAC_TOE.name());
        savedScore.setWins(15);
        savedScore.setLosses(8);
        savedScore.setDraws(3);
        savedScore.setLongestStreak(10);
        
        boolean updateResult = playerScoreDao.update(savedScore);
        
        assertTrue(updateResult);
        
        var updatedScore = playerScoreDao.findByUserId(savedPlayer.getId());
        assertTrue(updatedScore.isPresent());
        assertEquals(GameType.TIC_TAC_TOE.name(), updatedScore.get().getGameType());
        assertEquals(15, updatedScore.get().getWins());
        assertEquals(8, updatedScore.get().getLosses());
        assertEquals(3, updatedScore.get().getDraws());
        assertEquals(10, updatedScore.get().getLongestStreak());
    }

    @Test
    @DisplayName("Should return false when updating non-existent player score")
    void testUpdateNonExistentPlayerScore() throws SQLException {
        PlayerScore nonExistentScore = new PlayerScore();
        nonExistentScore.setUserId(99999);
        nonExistentScore.setGameType(GameType.TIC_TAC_TOE.name());
        nonExistentScore.setWins(5);
        nonExistentScore.setLosses(2);
        nonExistentScore.setDraws(1);
        nonExistentScore.setLongestStreak(3);
        
        boolean updateResult = playerScoreDao.update(nonExistentScore);
        
        assertFalse(updateResult);
    }

    @Test
    @DisplayName("Should delete player score by user ID successfully")
    void testDeleteByUserId() throws SQLException {
        Player player = new Player("testuser_score7_" + System.currentTimeMillis(), "password123", "avatar.png", PlayerStatus.ONLINE);
        Player savedPlayer = playerDao.save(player);
        
        PlayerScore score = new PlayerScore();
        score.setUserId(savedPlayer.getId());
        score.setGameType(GameType.TIC_TAC_TOE.name());
        score.setWins(5);
        score.setLosses(2);
        score.setDraws(1);
        score.setLongestStreak(3);
        
        playerScoreDao.save(score);
        int userId = savedPlayer.getId();
        
        assertTrue(playerScoreDao.findByUserId(userId).isPresent());
        
        boolean deleteResult = playerScoreDao.deleteByUserId(userId);
        
        assertTrue(deleteResult);
        assertFalse(playerScoreDao.findByUserId(userId).isPresent());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent player score")
    void testDeleteByUserIdNotFound() throws SQLException {
        boolean deleteResult = playerScoreDao.deleteByUserId(99999);
        
        assertFalse(deleteResult);
    }
}

