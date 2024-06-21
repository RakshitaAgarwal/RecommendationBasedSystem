package org.cafeteria.server.controller;

import com.google.gson.reflect.TypeToken;
import com.sun.istack.NotNull;
import org.cafeteria.common.model.*;
import org.cafeteria.server.services.DailyRecommendationService;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class DailyRecommendationController {
    private static IDailyRecommendationService _dailyRecommendationService;
    private static INotificationService _notificationService;

    public DailyRecommendationController() {
        _dailyRecommendationService = new DailyRecommendationService();
        _notificationService = new NotificationService();
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

    public String rollOutNextDayMenuOptions(@NotNull ParsedRequest request) throws SQLException {
        Type mapType = new TypeToken<Map<MealTypeEnum, List<MenuItem>>>() {
        }.getType();
        Map<MealTypeEnum, List<MenuItem>> nextDayMenuOptions = deserializeMap(request.getJsonData(), mapType);
        String response;
        if(_dailyRecommendationService.rollOutItemsForNextDayMenu(nextDayMenuOptions)) {
            response = createResponse(ResponseCode.OK, serializeMap(nextDayMenuOptions));
            Notification notification = new Notification(4, "Next Day Menu options are updated. Please Cast your vote for the day");
            _notificationService.sendNotificationToAllEmployees(notification);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
        }
        return response;
    }

    public String voteForNextDayMenu(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}