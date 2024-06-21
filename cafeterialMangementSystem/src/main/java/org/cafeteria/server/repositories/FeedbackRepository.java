package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IFeedbackRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.cafeteria.common.util.Utils.timestampToDate;

public class FeedbackRepository implements IFeedbackRepository {
    private final Connection connection;

    public FeedbackRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(Feedback item) {

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
    public Feedback getById(int id) {
        return null;
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