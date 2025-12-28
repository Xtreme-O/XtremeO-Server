package org.example.xtremo.dao;

import org.example.xtremo.model.entity.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDao {

    Player save(Player player);

    Optional<Player> findById(int playerId);

    Optional<Player> findByUsername(String username);

    List<Player> findAll();

    boolean update(Player player);

    boolean deleteById(int playerId);
}
