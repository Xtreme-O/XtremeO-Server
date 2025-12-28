package org.example.xtremo.dao;

import org.example.xtremo.model.entity.Game;

import java.util.List;
import java.util.Optional;

public interface GameDao {

    Game save(Game game);

    Optional<Game> findById(int gameId);

    List<Game> findByPlayerId(int playerId);

    List<Game> findAll();

    boolean update(Game game);

    boolean deleteById(int gameId);
}

