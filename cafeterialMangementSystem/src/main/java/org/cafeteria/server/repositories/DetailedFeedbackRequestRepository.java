package org.cafeteria.server.repositories;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
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
    private static final String TABLE_DETAILED_FEEDBACK_REQUEST = "DetailedFeedbackRequest";
    public static final String COLUMN_PK_ID = "Id";
    private static final String COLUMN_FK_MENU_ITEM_ID = "menuItemId";
    private static final String COLUMN_DATE_TIME = "dateTime";

    public DetailedFeedbackRequestRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(DetailedFeedbackRequest feedbackRequest) throws SQLException, DuplicateEntryFoundException {
        String query = "INSERT INTO " + TABLE_DETAILED_FEEDBACK_REQUEST +
                " (" + COLUMN_FK_MENU_ITEM_ID + ", " +
                COLUMN_DATE_TIME + ") " +
                "VALUES (?, ?)";
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
        String query = "SELECT * FROM " + TABLE_DETAILED_FEEDBACK_REQUEST;
        List<DetailedFeedbackRequest> detailedFeedbackRequests = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt(COLUMN_PK_ID);
                int menuItemId = resultSet.getInt(COLUMN_FK_MENU_ITEM_ID);
                Date dateTime = resultSet.getTimestamp(COLUMN_DATE_TIME);

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
        String query = "SELECT * FROM " + TABLE_DETAILED_FEEDBACK_REQUEST +
                " WHERE " + COLUMN_FK_MENU_ITEM_ID + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, menuItemId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                DetailedFeedbackRequest detailedFeedbackRequest = new DetailedFeedbackRequest();
                detailedFeedbackRequest.setId(resultSet.getInt(COLUMN_PK_ID));
                detailedFeedbackRequest.setMenuItemId(resultSet.getInt(COLUMN_FK_MENU_ITEM_ID));
                detailedFeedbackRequest.setDateTime(resultSet.getTimestamp(COLUMN_DATE_TIME));
                return detailedFeedbackRequest;
            }
        }
        return null;
    }
}