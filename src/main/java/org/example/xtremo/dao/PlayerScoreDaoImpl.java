package org.example.xtremo.dao;

import org.example.xtremo.model.entity.PlayerScore;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerScoreDaoImpl implements PlayerScoreDao {

    private final Connection connection;

    public PlayerScoreDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PlayerScore save(PlayerScore score) {
        String query = "INSERT INTO user_scores (user_id, game_type, wins, losses, draws, longest_streak) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, score.getUserId());
            statement.setString(2, score.getGameType());
            statement.setInt(3, score.getWins());
            statement.setInt(4, score.getLosses());
            statement.setInt(5, score.getDraws());
            statement.setInt(6, score.getLongestStreak());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return mapToUserScore(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user score", e);
        }
        return null;
    }

    @Override
    public Optional<PlayerScore> findByUserId(int userId) {
        String query = "SELECT * FROM user_scores WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToUserScore(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user score with user id : " + userId, e);
        }
        return Optional.empty();
    }

    @Override
    public List<PlayerScore> findAll() {
        String query = "SELECT * FROM user_scores";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
             List<PlayerScore> scores = new ArrayList<>();
            while (resultSet.next()) {
                scores.add(mapToUserScore(resultSet));
            }
            return scores;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all user scores", e);
        }
    }

    @Override
    public boolean update(PlayerScore score) {
        String query = "UPDATE user_scores SET game_type = ?, wins = ?, losses = ?, draws = ?, longest_streak = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, score.getGameType());
            statement.setInt(2, score.getWins());
            statement.setInt(3, score.getLosses());
            statement.setInt(4, score.getDraws());
            statement.setInt(5, score.getLongestStreak());
            statement.setInt(6, score.getUserId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user score", e);
        }
    }

    @Override
    public boolean deleteByUserId(int userId) {
        String query = "DELETE FROM user_scores WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user score with user id : " + userId, e);
        }
    }

    private PlayerScore mapToUserScore(ResultSet resultSet) throws SQLException {
        return new PlayerScore(
            resultSet.getInt("score_id"),
            resultSet.getInt("user_id"),
            resultSet.getString("game_type"),
            resultSet.getInt("wins"),
            resultSet.getInt("losses"),
            resultSet.getInt("draws"),
            resultSet.getInt("longest_streak")
        );
    }
}

