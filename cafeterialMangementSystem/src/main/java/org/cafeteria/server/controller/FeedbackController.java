package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.services.FeedbackService;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;

public class FeedbackController {
    private static IFeedbackService _feedbackService;
    public FeedbackController() {
        _feedbackService = new FeedbackService();
    }

    public String addFeedback(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }

    public String getFeedbackReport(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}
