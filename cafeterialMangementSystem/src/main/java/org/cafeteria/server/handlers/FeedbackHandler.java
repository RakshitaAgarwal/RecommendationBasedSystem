package org.cafeteria.server.handlers;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.services.FeedbackService;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;

public class FeedbackHandler {
    private static IFeedbackService _feedbackService;
    public FeedbackHandler() {
        _feedbackService = new FeedbackService();
    }

    public String addFeedback(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }

    public String getFeedbackReport(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}
