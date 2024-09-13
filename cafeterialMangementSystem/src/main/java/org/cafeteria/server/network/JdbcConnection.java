package org.cafeteria.server.network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {
    private static JdbcConnection instance;
    private static Connection connection;

    private JdbcConnection() {
    }

    public static JdbcConnection getInstance() {
        if (instance == null) {
            instance = new JdbcConnection();
        }
        return instance;
    }

    public void connectToDatabase(String databaseUrl, String databaseUser, String databasePassword) {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
                System.out.println("Database connection successfully made");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}