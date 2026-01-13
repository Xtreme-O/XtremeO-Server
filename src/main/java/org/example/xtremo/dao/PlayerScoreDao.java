package org.example.xtremo.dao;

import org.example.xtremo.model.entity.PlayerScore;
import org.example.xtremo.model.enums.GameType;

import java.util.List;
import java.util.Optional;

public interface PlayerScoreDao {

    PlayerScore save(PlayerScore score);

    Optional<PlayerScore> findByUserId(int userId);

    Optional<PlayerScore> findByUserIdAndGameType(int userId, GameType gameType);

    Optional<PlayerScore> findById(int id);

    List<PlayerScore> findAll();

    boolean update(PlayerScore score);

    boolean deleteByUserId(int userId);
}

