package org.cafeteria.server.network;

import java.sql.*;

import static org.cafeteria.common.constants.Constants.*;

public class JdbcConnection {
    private static Connection connection;

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            System.out.println("Database connection successfully made");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

//    public static ResultSet executeStatement(String query) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement(query);
//        preparedStatement.set
//        ResultSet resultSet = statement.executeQuery(query);
//        return resultSet;
//    }
}