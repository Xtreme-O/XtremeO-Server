/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.example.xtremo.dao.PlayerDao;
import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.entity.Player;

/**
 *
 * @author wahid
 */
public class PlayerService implements PlayerDao {

    private PlayerDao playerDaoImpl = null;
    private static PlayerService playerService = null;
    private Connection connection = null;

    private PlayerService() throws SQLException {
        if (connection == null) {
            connection = DBConnection.getConnection();
        }
        if (playerDaoImpl == null) {
            playerDaoImpl = new PlayerDaoImpl(connection);
        }

    }
    
    public static PlayerService getPlayerService() throws SQLException{
        if (playerService == null) {
            playerService = new PlayerService();
        }
        
        return playerService;
    }

    @Override
    public Player save(Player player) {
        return playerDaoImpl.save(player);
    }

    @Override
    public Optional<Player> findById(int playerId) {
        return playerDaoImpl.findById(playerId);
    }

    @Override
    public Optional<Player> findByUsername(String username) {

        return playerDaoImpl.findByUsername(username);
    }

    @Override
    public List<Player> findAll() {

        return playerDaoImpl.findAll();
    }

    @Override
    public boolean update(Player player) {

        return playerDaoImpl.update(player);
    }

    @Override
    public boolean deleteById(int playerId) {

        return playerDaoImpl.deleteById(playerId);
    }

}
