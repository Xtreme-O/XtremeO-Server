package org.example.xtremo.dao;

import org.example.xtremo.model.entity.GameMove;

import java.util.List;
import java.util.Optional;

public interface GameMoveDao {

    GameMove save(GameMove move);

    Optional<GameMove> findById(int moveId);

    List<GameMove> findByGameId(int gameId);
    
    List<GameMove> findAll();

    boolean deleteById(int moveId);

    boolean deleteByGameId(int gameId);
}

