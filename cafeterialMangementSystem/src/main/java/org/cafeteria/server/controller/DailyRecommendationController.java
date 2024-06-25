package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.*;
import org.cafeteria.server.services.DailyRecommendationService;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;
import java.util.Date;
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

    public String getRecommendationsForNextDayMenu() throws SQLException {
        Map<MealTypeEnum, List<MenuItemScore>> menuItemByMeals = _dailyRecommendationService.getDailyRecommendation();
        String response;
        if(menuItemByMeals != null) {
            response = createResponse(ResponseCode.OK, serializeMap(menuItemByMeals));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
        }
        return response;
    }

    public String rollOutNextDayMenuOptions(@NotNull ParsedRequest request) throws SQLException {
        List<Integer> nextDayMenuOptions = deserializeList(request.getJsonData(), Integer.class);
        String response;
        if(_dailyRecommendationService.rollOutItemsForNextDayMenu(nextDayMenuOptions)) {
            response = createResponse(ResponseCode.OK, null);
            Notification notification = new Notification(NotificationTypeEnum.NEXT_DAY_OPTIONS.ordinal(), "Next Day Menu options are updated. Please Cast your vote for the day", new Date());
            _notificationService.sendNotificationToAllEmployees(notification);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
        }
        return response;
    }

    public String getNextDayMenuOptions(@NotNull ParsedRequest request) throws SQLException {
        String response = "";
        return response;
    }
    public String voteForNextDayMenu() throws SQLException {
        return "";
    }
}