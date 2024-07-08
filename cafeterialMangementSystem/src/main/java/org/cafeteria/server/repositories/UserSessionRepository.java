package org.cafeteria.server.repositories;

import org.cafeteria.server.model.UserSession;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IUserSessionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.cafeteria.common.util.Utils.dateToTimestamp;

public class UserSessionRepository implements IUserSessionRepository {
    private final Connection connection;

    public UserSessionRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public UserSession addUserSession(UserSession userSession) throws SQLException {
        String query = "INSERT INTO UserSession (userId, loginDateTime) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userSession.getUserId());
            statement.setTimestamp(2, dateToTimestamp(userSession.getLoginDateTime()));
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int generatedId = resultSet.getInt(1);
                    return new UserSession(generatedId, userSession.getUserId(), userSession.getLoginDateTime(), userSession.getLogoutDateTime());
                } else {
                    throw new SQLException("Creating user session failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public boolean updateUserSession(UserSession userSession) throws SQLException {
        String query = "Update UserSession SET logoutDateTime = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, dateToTimestamp(userSession.getLogoutDateTime()));
            statement.setInt(2, userSession.getId());
            return statement.executeUpdate() > 0;
        }
    }
}