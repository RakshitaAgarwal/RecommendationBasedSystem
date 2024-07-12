package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IFeedbackRepository;
import static org.cafeteria.common.util.Utils.dateToTimestamp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository implements IFeedbackRepository {
    private final Connection connection;
    private static final String TABLE_FEEDBACK = "Feedback";
    private static final String COLUMN_PK_ID = "id";
    private static final String COLUMN_FK_USER_ID = "userId";
    private static final String COLUMN_FK_MENU_ITEM_ID = "menuItemId";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_COMMENT = "comment";
    private static final String COLUMN_DATE_TIME = "dateTime";

    public FeedbackRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(Feedback feedback) throws SQLException {
        String query = "INSERT INTO " + TABLE_FEEDBACK +
                " (" + COLUMN_FK_USER_ID + ", " +
                COLUMN_FK_MENU_ITEM_ID + ", " +
                COLUMN_RATING + ", " +
                COLUMN_COMMENT + ", " +
                COLUMN_DATE_TIME + ") " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, feedback.getUserId());
            statement.setInt(2, feedback.getMenuItemId());
            statement.setFloat(3, feedback.getRating());
            statement.setString(4, feedback.getComment());
            statement.setTimestamp(5, dateToTimestamp(feedback.getDateTime()));

            return statement.executeUpdate()>0;
        }
    }

    @Override
    public boolean addBatch(List<Feedback> items) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Feedback item) {
        return false;
    }

    @Override
    public boolean update(Feedback item) {
        return false;
    }

    @Override
    public List<Feedback> getAll() {
        return null;
    }

    @Override
    public Feedback getById(int id) throws SQLException {
        String query = "SELECT * FROM " + TABLE_FEEDBACK + " WHERE " + COLUMN_PK_ID + " = ?";
        Feedback feedback = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                feedback = new Feedback();
                feedback.setFeedbackId(resultSet.getInt(COLUMN_PK_ID));
                feedback.setMenuItemId(resultSet.getInt(COLUMN_FK_MENU_ITEM_ID));
                feedback.setComment(resultSet.getString(COLUMN_COMMENT));
                feedback.setUserId(resultSet.getInt(COLUMN_FK_USER_ID));
                feedback.setRating(resultSet.getFloat(COLUMN_RATING));
                feedback.setDateTime(resultSet.getTimestamp(COLUMN_DATE_TIME));
            }
        }
        return feedback;
    }

    @Override
    public List<Feedback> getFeedbacksByMenuItemId(int menuItemId) throws SQLException {
        String query = "SELECT * FROM " + TABLE_FEEDBACK + " WHERE " + COLUMN_FK_MENU_ITEM_ID + " = ?";
        List<Feedback> feedbacks = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, menuItemId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(resultSet.getInt(COLUMN_PK_ID));
                feedback.setMenuItemId(resultSet.getInt(COLUMN_FK_MENU_ITEM_ID));
                feedback.setComment(resultSet.getString(COLUMN_COMMENT));
                feedback.setUserId(resultSet.getInt(COLUMN_FK_USER_ID));
                feedback.setRating(resultSet.getFloat(COLUMN_RATING));
                feedback.setDateTime(resultSet.getTimestamp(COLUMN_DATE_TIME));

                feedbacks.add(feedback);
            }
        }
        return feedbacks;
    }
}