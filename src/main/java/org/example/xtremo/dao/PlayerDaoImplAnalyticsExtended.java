/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.entity.Player;
import org.example.xtremo.model.enums.PlayerStatus;

/**
 *
 * @author wahid
 */
public class PlayerDaoImplAnalyticsExtended extends PlayerDaoAnalyticsExtended{
    private final Connection connection;
   
    public PlayerDaoImplAnalyticsExtended() throws SQLException {
        this.connection = DBConnection.getConnection();
    }
    @Override
    public int countByStatus(PlayerStatus status) {
        String query = "SELECT COUNT(*) FROM users WHERE status = ?";
        try(PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1,status.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting players by status: " + status, e);
        }
        return 0;
    }
}
