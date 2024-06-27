package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.FeedbackService;
import org.cafeteria.server.services.RecommendationEngine;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class RecommendationController {
    private static RecommendationEngine recommendationEngine;

    public RecommendationController() {
        IFeedbackService _feedbackService = new FeedbackService();
        recommendationEngine = new RecommendationEngine(_feedbackService);
    }
    public String getRecommendationScoreForMenuItem(@NotNull ParsedRequest request) throws SQLException {
        int menuItemId = deserializeData(request.getJsonData(), Integer.class);
        MenuItemRecommendation menuItemRecommendation = recommendationEngine.evaluateMenuItemRecommendation(menuItemId);
        return createResponse(ResponseCode.OK, serializeData(menuItemRecommendation));
    }
}