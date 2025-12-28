package org.example.xtremo.dao;

import org.example.xtremo.model.entity.Game;
import utils.JdbcUtils;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameDaoImpl implements GameDao {

    private final Connection connection;

    public GameDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Game save(Game game) {
        try {
            String query = "INSERT INTO games "
                    + "(game_type, player1_id, player2_id, winner_id, started_at, ended_at, is_recorded, record_file_path) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, game.getGameType());
            statement.setInt(2, game.getPlayer1Id());
            JdbcUtils.setNullableInt(statement, 3, game.getPlayer2Id());
            JdbcUtils.setNullableInt(statement, 4, game.getWinnerId());
            statement.setTimestamp(5, Timestamp.valueOf(game.getStartedAt()));
            JdbcUtils.setNullableTimestamp(statement, 6, game.getEndedAt());
            statement.setBoolean(7, game.isRecorded());
            JdbcUtils.setNullableString(statement, 8, game.getRecordFilePath());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return mapToGame(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving game", e);
        }
        return null;
    }

    @Override
    public Optional<Game> findById(int gameId) {
        try {
            String query = "SELECT * FROM games WHERE game_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToGame(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding game with id : " + gameId, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Game> findByPlayerId(int playerId) {
        try {
            String query = "SELECT * FROM games WHERE player1_id = ? OR player2_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, playerId);
            statement.setInt(2, playerId);
            ResultSet resultSet = statement.executeQuery();
            List<Game> games = new ArrayList<>();
            while (resultSet.next()) {
                games.add(mapToGame(resultSet));
            }
            return games;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding games for player id : " + playerId, e);
        }
    }

    @Override
    public List<Game> findAll() {
        try {
            String query = "SELECT * FROM games";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            List<Game> games = new ArrayList<>();
            while (resultSet.next()) {
                games.add(mapToGame(resultSet));
            }
            return games;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all games", e);
        }
    }

    @Override
    public boolean update(Game game) {
        try {
            String query = "UPDATE games SET game_type = ?, player1_id = ?, player2_id = ?, winner_id = ?, started_at = ?, ended_at = ?, is_recorded = ?, record_file_path = ? WHERE game_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, game.getGameType());
            statement.setInt(2, game.getPlayer1Id());
            JdbcUtils.setNullableInt(statement, 3, game.getPlayer2Id());
            JdbcUtils.setNullableInt(statement, 4, game.getWinnerId());
            statement.setTimestamp(5, Timestamp.valueOf(game.getStartedAt()));
            JdbcUtils.setNullableTimestamp(statement, 6, game.getEndedAt());
            statement.setBoolean(7, game.isRecorded());
            JdbcUtils.setNullableString(statement, 8, game.getRecordFilePath());
            statement.setInt(9, game.getGameId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating game", e);
        }
    }

    @Override
    public boolean deleteById(int gameId) {
        try {
            String query = "DELETE FROM games WHERE game_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gameId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting game with id : " + gameId, e);
        }
    }

    private Game mapToGame(ResultSet resultSet) throws SQLException {
        Integer player2Id = resultSet.getObject("player2_id", Integer.class);
        Integer winnerId = resultSet.getObject("winner_id", Integer.class);
        Timestamp endedAtTimestamp = resultSet.getTimestamp("ended_at");
        LocalDateTime endedAt = endedAtTimestamp != null ? endedAtTimestamp.toLocalDateTime() : null;
        String recordFilePath = resultSet.getString("record_file_path");
        
        return new Game(
            resultSet.getInt("game_id"),
            resultSet.getString("game_type"),
            resultSet.getInt("player1_id"),
            player2Id,
            winnerId,
            resultSet.getTimestamp("started_at").toLocalDateTime(),
            endedAt,
            resultSet.getBoolean("is_recorded"),
            recordFilePath
        );
    }
}

