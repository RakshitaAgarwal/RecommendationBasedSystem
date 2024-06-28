package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IFeedbackRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

public class FeedbackRepository implements IFeedbackRepository {
    private final Connection connection;

    public FeedbackRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(Feedback feedback) throws SQLException {
        String query = "INSERT INTO feedback (userId, menuItemId, rating, comment, dateTime) VALUES (?, ?, ?, ?, ?)";

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
    public List<Feedback> GetAll() {
        return null;
    }

    @Override
    public Feedback getById(int id) throws SQLException {
        String query = "SELECT * FROM feedback WHERE feedback_id = ?";
        Feedback feedback = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                feedback = new Feedback();
                feedback.setFeedbackId(resultSet.getInt("id"));
                feedback.setUserId(resultSet.getInt("userId"));
                feedback.setMenuItemId(resultSet.getInt("menuItemId"));
                feedback.setRating(resultSet.getFloat("rating"));
                feedback.setComment(resultSet.getString("comment"));
                feedback.setDateTime(resultSet.getDate("dateTime"));
            }
        }
        return feedback;
    }

    @Override
    public List<Feedback> getFeedbacksByMenuItem(int menuItemId) throws SQLException {
        String query = "SELECT * FROM Feedback where id = ?";
        List<Feedback> feedbacks = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, menuItemId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(resultSet.getInt("id"));
                feedback.setMenuItemId(menuItemId);
                feedback.setComment(resultSet.getString("comment"));
                feedback.setUserId(resultSet.getInt("userId"));
                feedback.setRating(resultSet.getFloat("rating"));
                Timestamp timestamp = resultSet.getTimestamp("dateTime");
                feedback.setDateTime(timestampToDate(timestamp));

                feedbacks.add(feedback);
            }
            return feedbacks;
        }
    }
}