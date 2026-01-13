/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.example.xtremo.dao.PlayerDao;
import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.dao.PlayerScoreDao;
import org.example.xtremo.dao.PlayerScoreDaoImpl;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.mapper.PlayerMapper;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.model.entity.Player;
import org.example.xtremo.model.entity.PlayerScore;
import org.example.xtremo.model.enums.GameType;
import org.example.xtremo.model.enums.PlayerStatus;
import org.example.xtremo.utils.PasswordUtils;

/**
 *
 * @author monaz
 */
public class AuthService {

    private final PlayerDao playerDao;
    private final PlayerScoreDao playerScoreDao;
    private static final AuthService authService = null;

    private AuthService() throws SQLException {
        playerDao = new PlayerDaoImpl();
        playerScoreDao = new PlayerScoreDaoImpl(DBConnection.getConnection());
    }
    
    public static AuthService getAuthService() throws SQLException{
        if (authService != null) {
            return authService;
        }
        return new AuthService();
    }
    

    public PlayerDTO login(String username, String password) throws Exception {

        Player player = playerDao.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid username or password"));

        String incomingPasswordHash = PasswordUtils.hashPassword(password);

        if (!incomingPasswordHash.equals(player.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        player.setLastLogin(LocalDateTime.now());
        player.setStatus(
                PlayerStatus.ONLINE
        );
        playerDao.update(player);

        return PlayerMapper.toDto(player);

    }

    public PlayerDTO register(String username, String password, String avatarUrl) throws Exception {
        System.out.println("In register");
        if (playerDao.findByUsername(username).isPresent()) {
            System.out.println("Username already exists");
            throw new RuntimeException("Username already exists");
        }

        String passwordHash = PasswordUtils.hashPassword(password);

        Player player = new Player(
                username,
                passwordHash,
                avatarUrl,
                PlayerStatus.ONLINE
        );

        Player savedPlayer = playerDao.save(player);

        PlayerScore emptyScore = new PlayerScore(
                0,
                savedPlayer.getId(),
                GameType.TIC_TAC_TOE.name(),
                0,
                0,
                0,
                0
        );
        playerScoreDao.save(emptyScore);

        return PlayerMapper.toDto(savedPlayer);
    }

    public boolean logout(String username) throws Exception {
        if (!playerDao.findByUsername(username).isPresent()) {
            throw new Exception("Username not found");
        }
        Optional<Player> player = playerDao.findByUsername(username);

        if (player.isPresent()) {
            Player pl = player.get();
            
            pl.setStatus(PlayerStatus.OFFLINE);
            return playerDao.update(pl);
        } else {
            return false;
        }
    }

}
