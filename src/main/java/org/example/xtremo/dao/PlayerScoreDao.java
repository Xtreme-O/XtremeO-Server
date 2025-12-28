package org.example.xtremo.dao;

import org.example.xtremo.model.entity.PlayerScore;

import java.util.List;
import java.util.Optional;

public interface PlayerScoreDao {

    PlayerScore save(PlayerScore score);

    Optional<PlayerScore> findByUserId(int userId);

    List<PlayerScore> findAll();

    boolean update(PlayerScore score);

    boolean deleteByUserId(int userId);
}

