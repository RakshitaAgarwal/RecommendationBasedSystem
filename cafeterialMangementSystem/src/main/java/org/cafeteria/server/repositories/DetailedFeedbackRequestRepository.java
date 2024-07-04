package org.cafeteria.server.repositories;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.DetailedFeedbackRequest;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IDetailedFeedbackRequestRepository;
import static org.cafeteria.common.util.Utils.dateToTimestamp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String query = "SELECT * FROM DetailedFeedbackRequest";
        List<DetailedFeedbackRequest> detailedFeedbackRequests = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuItemId = resultSet.getInt("menuItemId");
                Date dateTime = resultSet.getTimestamp("dateTime");

                DetailedFeedbackRequest detailedFeedbackRequest = new DetailedFeedbackRequest();
                detailedFeedbackRequest.setId(id);
                detailedFeedbackRequest.setMenuItemId(menuItemId);
                detailedFeedbackRequest.setDateTime(dateTime);

                detailedFeedbackRequests.add(detailedFeedbackRequest);
            }
        }
        return detailedFeedbackRequests;
    }

    @Override
    public DetailedFeedbackRequest getById(int id) throws SQLException {
        return null;
    }

    @Override
    public DetailedFeedbackRequest getByMenuItemId(int menuItemId) throws SQLException {
        String query = "SELECT * FROM DetailedFeedbackRequest WHERE menuItemId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, menuItemId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                DetailedFeedbackRequest detailedFeedbackRequest = new DetailedFeedbackRequest();
                detailedFeedbackRequest.setId(resultSet.getInt("id"));
                detailedFeedbackRequest.setMenuItemId(resultSet.getInt("menuItemId"));
                detailedFeedbackRequest.setDateTime(resultSet.getTimestamp("dateTime"));
                return detailedFeedbackRequest;
            }
        }
        return null;
    }
}