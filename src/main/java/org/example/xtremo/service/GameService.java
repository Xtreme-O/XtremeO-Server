/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.example.xtremo.dao.GameDao;
import org.example.xtremo.dao.GameDaoImpl;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.entity.Game;

/**
 *
 * @author wahid
 */
public class GameService implements GameDao {

    private GameDao gameDao;
    public static GameService gameService = null;

    public static GameService getGameService() throws SQLException {
        if (gameService == null) {
            gameService = new GameService();
        }
        return gameService;
    }

    private GameService() throws SQLException {
        if (gameService == null) {
            gameDao = new GameDaoImpl(DBConnection.getConnection());
        }
    }

    @Override
    public Game save(Game game) {
        return gameDao.save(game);
    }

    @Override
    public Optional<Game> findById(int gameId) {
        return gameDao.findById(gameId);
    }

    @Override
    public List<Game> findByPlayerId(int playerId) {
        return gameDao.findByPlayerId(playerId);
    }

    @Override
    public List<Game> findAll() {
        return gameDao.findAll();
    }

    @Override
    public boolean update(Game game) {
        return gameDao.update(game);
    }

    @Override
    public boolean deleteById(int gameId) {
        return gameDao.deleteById(gameId);
    }

}
