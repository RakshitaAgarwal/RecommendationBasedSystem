package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.RecommendationService;
import org.cafeteria.server.services.interfaces.IRecommendationService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class RecommendationController {
    private static IRecommendationService _recommendationService;

    public RecommendationController() {
        _recommendationService = new RecommendationService();
    }
    public String getRecommendationScoreForMenuItem(@NotNull ParsedRequest request) throws SQLException {
        int menuItemId = deserializeData(request.getJsonData(), Integer.class);
        String response;
        MenuItemRecommendation menuItemRecommendation = _recommendationService.getRecommendationForMenuItem(menuItemId);
        if(menuItemRecommendation != null) {
            response = createResponse(ResponseCode.OK, serializeData(menuItemRecommendation));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No Recommendations"));
        }
        return response;
    }

    public String getRecommendationsForNextDayMenu() throws SQLException {
        Map<MealTypeEnum, List<MenuItemRecommendation>> menuItemByMeals = _recommendationService.getRecommendationForNextDayMenu();
        String response;
        if(menuItemByMeals != null) {
            response = createResponse(ResponseCode.OK, serializeMap(menuItemByMeals));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while fetching recommendations."));
        }
        return response;
    }
}