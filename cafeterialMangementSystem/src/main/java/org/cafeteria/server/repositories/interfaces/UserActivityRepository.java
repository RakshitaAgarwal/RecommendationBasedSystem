package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.server.model.UserActivity;
import org.cafeteria.server.model.UserSession;
import org.cafeteria.server.network.JdbcConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.cafeteria.common.util.Utils.dateToTimestamp;

public class UserActivityRepository implements IUserActivityRepository {

    private final Connection connection;

    public UserActivityRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(UserActivity userActivity) throws SQLException {
        String query = "INSERT INTO UserActivity (sessionId, activityDescription, activityTime) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userActivity.getSessionId());
            statement.setString(2, userActivity.getActivity());
            statement.setTimestamp(3, dateToTimestamp(userActivity.getDatetime()));
            return statement.executeUpdate() > 0;
        }
    }
}