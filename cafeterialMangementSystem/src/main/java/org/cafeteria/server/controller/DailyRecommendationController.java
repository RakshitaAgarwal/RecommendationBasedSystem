package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.*;
import org.cafeteria.server.services.DailyRecommendationService;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class DailyRecommendationController {
    private static IDailyRecommendationService _dailyRecommendationService;

    public DailyRecommendationController() {
        _dailyRecommendationService = new DailyRecommendationService();
    }

    public String getDailyRecommendation() throws SQLException {
        Map<MealTypeEnum, List<MenuItem>> menuItemByMeals = _dailyRecommendationService.getDailyRecommendation();
        String response;
        if(menuItemByMeals != null) {
            response = createResponse(ResponseCode.OK, serializeMap(menuItemByMeals));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
        }
        return response;
    }

    public String provideNextDayMenuOptions(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }

    public String voteForNextDayMenu(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}