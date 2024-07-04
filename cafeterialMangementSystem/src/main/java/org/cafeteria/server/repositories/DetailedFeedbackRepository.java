package org.cafeteria.server.repositories;

import org.cafeteria.common.model.DetailedFeedback;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IDetailedFeedbackRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.cafeteria.common.util.Utils.dateToTimestamp;

public class DetailedFeedbackRepository implements IDetailedFeedbackRepository {
    private final Connection connection;

    public DetailedFeedbackRepository() {
        connection = JdbcConnection.getConnection();
    }

    public boolean addBatch(List<DetailedFeedback> answers) throws SQLException {
        String query = "INSERT INTO DetailedFeedback (userId, menuItemId, feedbackQuestionId, answer, dateTime) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (DetailedFeedback detailedFeedback : answers) {
                statement.setInt(1, detailedFeedback.getUserId());
                statement.setInt(2, detailedFeedback.getMenuItemId());
                statement.setInt(3, detailedFeedback.getFeedbackQuestionId());
                statement.setString(4, detailedFeedback.getAnswer());
                statement.setTimestamp(5, dateToTimestamp(detailedFeedback.getDateTime()));
                statement.addBatch();
            }
            int[] results = statement.executeBatch();
            return results.length == answers.size();
        }
    }
}