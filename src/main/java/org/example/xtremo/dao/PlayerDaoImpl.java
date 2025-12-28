package org.example.xtremo.dao;

import org.example.xtremo.model.entity.Player;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerDaoImpl implements PlayerDao {

    private final Connection connection;

    public PlayerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
public Player save(Player player) {
    String query = "INSERT INTO users (username, password_hash, avatar_url, status) VALUES (?, ?, ?, ?)";

    try (PreparedStatement statement =
             connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        statement.setString(1, player.getUsername());
        statement.setString(2, player.getPasswordHash());
        statement.setString(3, player.getAvatarUrl());
        statement.setString(4, player.getStatus());

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                return findById(generatedId)
                        .orElseThrow(() -> new SQLException("User not found after insert"));
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error saving player", e);
    }

    return null;
}


    @Override
    public Optional<Player> findById(int playerId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToPlayer(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding player with id : " + playerId, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToPlayer(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding player with username : " + username, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Player> findAll() {
        String query = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            List<Player> players = new ArrayList<>();
            while (resultSet.next()) {
                players.add(mapToPlayer(resultSet));
            }
            return players;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all players", e);
        }
    }

    @Override
    public boolean update(Player player) {
        String query = "UPDATE users SET username = ?, password_hash = ?, avatar_url = ?, status = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUsername());
            statement.setString(2, player.getPasswordHash());
            statement.setString(3, player.getAvatarUrl());
            statement.setString(4, player.getStatus());
            statement.setInt(5, player.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating player", e);
        }
    }

    @Override
    public boolean deleteById(int playerId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting player with id : " + playerId, e);
        }
    }

    private Player mapToPlayer(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTs = resultSet.getTimestamp("created_at");
        Timestamp lastLoginTs = resultSet.getTimestamp("last_login");
        return new Player(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                resultSet.getString("avatar_url"),
                resultSet.getString("status"),
                createdAtTs != null ? createdAtTs.toLocalDateTime() : null,
                lastLoginTs != null ? lastLoginTs.toLocalDateTime() : null
        );
    }
}
