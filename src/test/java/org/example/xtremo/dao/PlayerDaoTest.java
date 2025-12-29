package org.example.xtremo.dao;

import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlayerDao Tests")
class PlayerDaoTest {

    private PlayerDao playerDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DBConnection.getConnection();
        playerDao = new PlayerDaoImpl(connection);
    }

    @Test
    @DisplayName("Should save player and return entity with generated ID")
    void testSavePlayer() throws SQLException {
        Player newPlayer = new Player("testuser_" + System.currentTimeMillis(), "password123", "avatar.png", "online");
        
        Player savedPlayer = playerDao.save(newPlayer);
        
        assertNotNull(savedPlayer, "Saved player should not be null");
        assertTrue(savedPlayer.getId() > 0, "Saved player should have a generated ID");
        assertEquals(newPlayer.getUsername(), savedPlayer.getUsername(), "Username should match");
        assertEquals("online", savedPlayer.getStatus(), "Status should match");
    }

    @Test
    @DisplayName("Should find player by ID after saving")
    void testFindById() throws SQLException {
        Player newPlayer = new Player("findbyid_" + System.currentTimeMillis(), "pass123", "avatar.png", "offline");
        Player savedPlayer = playerDao.save(newPlayer);
        
        var retrievedPlayer = playerDao.findById(savedPlayer.getId());
        
        assertTrue(retrievedPlayer.isPresent(), "Player should be found by ID");
        assertEquals(savedPlayer.getId(), retrievedPlayer.get().getId(), "IDs should match");
        assertEquals(newPlayer.getUsername(), retrievedPlayer.get().getUsername(), "Username should match");
    }

    @Test
    @DisplayName("Should find player by username")
    void testFindByUsername() throws SQLException {
        String uniqueUsername = "findbyusername_" + System.currentTimeMillis();
        Player newPlayer = new Player(uniqueUsername, "pass456", "avatar.png", "online");
        Player savedPlayer = playerDao.save(newPlayer);
        
        var retrievedPlayer = playerDao.findByUsername(uniqueUsername);
        
        assertTrue(retrievedPlayer.isPresent(), "Player should be found by username");
        assertEquals(savedPlayer.getId(), retrievedPlayer.get().getId(), "IDs should match");
        assertEquals(uniqueUsername, retrievedPlayer.get().getUsername(), "Username should match");
    }

    @Test
    @DisplayName("Should return empty Optional when player not found by ID")
    void testFindByIdNotFound() throws SQLException {
        var retrievedPlayer = playerDao.findById(99999);
        
        assertFalse(retrievedPlayer.isPresent(), "Player should not be found");
    }

    @Test
    @DisplayName("Should return empty Optional when player not found by username")
    void testFindByUsernameNotFound() throws SQLException {
        var retrievedPlayer = playerDao.findByUsername("nonexistentuser");
        
        assertFalse(retrievedPlayer.isPresent(), "Player should not be found");
    }

    @Test
    @DisplayName("Should find all players")
    void testFindAll() throws SQLException {
        Player player1 = playerDao.save(new Player("findall1_" + System.currentTimeMillis(), "pass1", "avatar1.png", "online"));
        Player player2 = playerDao.save(new Player("findall2_" + System.currentTimeMillis(), "pass2", "avatar2.png", "offline"));
        Player player3 = playerDao.save(new Player("findall3_" + System.currentTimeMillis(), "pass3", "avatar3.png", "online"));
        
        var allPlayers = playerDao.findAll();
        
        assertNotNull(allPlayers, "List should not be null");
        assertTrue(allPlayers.size() >= 3, "Should find at least the 3 players we created");
        
        assertTrue(allPlayers.stream().anyMatch(p -> p.getId() == player1.getId()), "Player1 should be in the list");
        assertTrue(allPlayers.stream().anyMatch(p -> p.getId() == player2.getId()), "Player2 should be in the list");
        assertTrue(allPlayers.stream().anyMatch(p -> p.getId() == player3.getId()), "Player3 should be in the list");
    }

    @Test
    @DisplayName("Should update existing player successfully")
    void testUpdatePlayer() throws SQLException {
        Player newPlayer = new Player("updateuser_" + System.currentTimeMillis(), "oldpass", "oldavatar.png", "offline");
        Player savedPlayer = playerDao.save(newPlayer);
        
        String updatedUsername = "updateduser_" + System.currentTimeMillis();
        savedPlayer.setUsername(updatedUsername);
        savedPlayer.setPasswordHash("newpass");
        savedPlayer.setAvatarUrl("newavatar.png");
        savedPlayer.setStatus("online");
        
        boolean updateResult = playerDao.update(savedPlayer);
        
        assertTrue(updateResult, "Update should return true");
        
        var updatedPlayer = playerDao.findById(savedPlayer.getId());
        assertTrue(updatedPlayer.isPresent(), "Updated player should be found");
        assertEquals(updatedUsername, updatedPlayer.get().getUsername(), "Username should be updated");
        assertEquals("newpass", updatedPlayer.get().getPasswordHash(), "Password should be updated");
        assertEquals("newavatar.png", updatedPlayer.get().getAvatarUrl(), "Avatar should be updated");
        assertEquals("online", updatedPlayer.get().getStatus(), "Status should be updated");
    }

    @Test
    @DisplayName("Should return false when updating non-existent player")
    void testUpdateNonExistentPlayer() throws SQLException {
        Player nonExistentPlayer = new Player("nonexistent_" + System.currentTimeMillis(), "pass", "avatar.png", "online");
        nonExistentPlayer.setId(99999);
        
        boolean updateResult = playerDao.update(nonExistentPlayer);
        
        assertFalse(updateResult, "Update should return false for non-existent player");
    }

    @Test
    @DisplayName("Should delete player by ID successfully")
    void testDeleteById() throws SQLException {
        Player newPlayer = new Player("deleteuser_" + System.currentTimeMillis(), "pass123", "avatar.png", "online");
        Player savedPlayer = playerDao.save(newPlayer);
        int playerId = savedPlayer.getId();
        
        assertTrue(playerDao.findById(playerId).isPresent(), "Player should exist before deletion");
        
        boolean deleteResult = playerDao.deleteById(playerId);
        
        assertTrue(deleteResult, "Delete should return true");
        
        assertFalse(playerDao.findById(playerId).isPresent(), "Player should not be found after deletion");
    }

    @Test
    @DisplayName("Should return false when deleting non-existent player")
    void testDeleteByIdNotFound() throws SQLException {
        boolean deleteResult = playerDao.deleteById(99999);
        
        assertFalse(deleteResult, "Delete should return false for non-existent player");
    }

    @Test
    @DisplayName("Should throw exception when saving player with duplicate username")
    void testSaveDuplicateUsername() throws SQLException {
        String duplicateUsername = "duplicateuser_" + System.currentTimeMillis();
        
        Player firstPlayer = new Player(duplicateUsername, "pass1", "avatar1.png", "online");
        Player savedPlayer = playerDao.save(firstPlayer);
        assertNotNull(savedPlayer, "First player should be saved successfully");
        
        Player duplicatePlayer = new Player(duplicateUsername, "pass2", "avatar2.png", "offline");
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playerDao.save(duplicatePlayer);
        }, "Should throw RuntimeException when saving duplicate username");
        
        assertNotNull(exception.getMessage(), "Exception should have a message");
        assertTrue(exception.getMessage().contains("Error saving player") || 
                   exception.getCause() instanceof SQLException, 
                   "Exception should wrap SQLException");
    }

}

