package org.cafeteria.server.repositories;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.DetailedFeedbackRequest;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IDetailedFeedbackRequestRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.cafeteria.common.util.Utils.dateToTimestamp;

public class DetailedFeedbackRequestRepository implements IDetailedFeedbackRequestRepository {
    private final Connection connection;

    public DetailedFeedbackRequestRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(DetailedFeedbackRequest feedbackRequest) throws SQLException, CustomExceptions.DuplicateEntryFoundException {
        String query = "INSERT INTO DetailedFeedbackRequest (menuItemId, dateTime) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, feedbackRequest.getMenuItemId());
            statement.setTimestamp(2, dateToTimestamp(feedbackRequest.getDateTime()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(DetailedFeedbackRequest object) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(DetailedFeedbackRequest object) throws SQLException {
        return false;
    }

    @Override
    public List<DetailedFeedbackRequest> getAll() throws SQLException {
        return null;
    }

    @Override
    public DetailedFeedbackRequest getById(int id) throws SQLException {
        return null;
    }
}