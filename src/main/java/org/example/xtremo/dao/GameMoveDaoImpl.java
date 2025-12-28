package org.example.xtremo.dao;

import org.example.xtremo.model.entity.GameMove;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameMoveDaoImpl implements GameMoveDao {

    private final Connection connection;

    public GameMoveDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public GameMove save(GameMove move) {
        String query = "INSERT INTO game_moves (game_id, player_id, move_number, move_data) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, move.getGameId());
            statement.setInt(2, move.getPlayerId());
            statement.setInt(3, move.getMoveNumber());
            statement.setString(4, move.getMoveData());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return mapToGameMove(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving game move", e);
        }
        return null;
    }

    @Override
    public Optional<GameMove> findById(int moveId) {
        String query = "SELECT * FROM game_moves WHERE move_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, moveId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToGameMove(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding game move with id : " + moveId, e);
        }
        return Optional.empty();
    }

    @Override
    public List<GameMove> findByGameId(int gameId) {
        String query = "SELECT * FROM game_moves WHERE game_id = ? ORDER BY move_number";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, gameId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<GameMove> moves = new ArrayList<>();
                while (resultSet.next()) {
                    moves.add(mapToGameMove(resultSet));
                }
                return moves;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding game moves for game id : " + gameId, e);
        }
    }
    
    
    @Override
    public List<GameMove> findAll() {
        String query = "SELECT * FROM game_moves";   
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<GameMove> moves = new ArrayList<>();
                while (resultSet.next()) {
                    moves.add(mapToGameMove(resultSet));
                }
                return moves;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all game moves", e);
        }
    }

    @Override
    public boolean deleteById(int moveId) {
        String query = "DELETE FROM game_moves WHERE move_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, moveId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting game move with id : " + moveId, e);
        }
    }

    @Override
    public boolean deleteByGameId(int gameId) {
        String query = "DELETE FROM game_moves WHERE game_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, gameId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting game moves for game id : " + gameId, e);
        }
    }

    private GameMove mapToGameMove(ResultSet resultSet) throws SQLException {
        return new GameMove(
            resultSet.getInt("move_id"),
            resultSet.getInt("game_id"),
            resultSet.getInt("player_id"),
            resultSet.getInt("move_number"),
            resultSet.getString("move_data")
        );
    }

}

