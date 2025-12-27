package org.example.xtremo.database;

import org.example.xtremo.config.ConfigLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    
    
    private DBConnection() {}


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL Driver not found");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = ConfigLoader.getProperty(DB_URL);
            String username = ConfigLoader.getProperty(DB_USERNAME);
            String password = ConfigLoader.getProperty(DB_PASSWORD);
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }
}
