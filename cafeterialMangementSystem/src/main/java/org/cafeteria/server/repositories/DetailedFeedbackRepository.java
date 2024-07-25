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
    private static final String TABLE_DETAILED_FEEDBACK = "DetailedFeedback";
    private static final String COLUMN_FK_USER_ID = "userId";
    private static final String COLUMN_FK_MENU_ITEM_ID = "menuItemId";
    private static final String COLUMN_FK_FEEDBACK_QUESTION_ID = "feedbackQuestionId";
    private static final String COLUMN_ANSWER = "answer";
    private static final String COLUMN_DATE_TIME = "dateTime";

    public DetailedFeedbackRepository() {
        connection = JdbcConnection.getConnection();
    }

    public boolean addBatch(List<DetailedFeedback> answers) throws SQLException {
        String query = "INSERT INTO " + TABLE_DETAILED_FEEDBACK +
                " (" + COLUMN_FK_USER_ID + ", " +
                COLUMN_FK_MENU_ITEM_ID + ", " +
                COLUMN_FK_FEEDBACK_QUESTION_ID + ", " +
                COLUMN_ANSWER + ", " +
                COLUMN_DATE_TIME + ") " +
                "VALUES (?, ?, ?, ?, ?)";

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