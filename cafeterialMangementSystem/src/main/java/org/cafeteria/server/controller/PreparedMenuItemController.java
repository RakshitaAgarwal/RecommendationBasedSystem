package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.Notification;
import org.cafeteria.common.model.enums.NotificationTypeEnum;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.PreparedMenuItemService;
import org.cafeteria.server.services.interfaces.INotificationService;
import org.cafeteria.server.services.interfaces.IPreparedMenuItemService;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.*;
import static org.cafeteria.common.util.Utils.extractDate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PreparedMenuItemController {
    private static IPreparedMenuItemService _preparedMenuItemService;
    private static INotificationService _notificationService;
    public PreparedMenuItemController() {
        _preparedMenuItemService = new PreparedMenuItemService();
        _notificationService = new NotificationService();
    }

    public String updateDailyFoodMenu(@NotNull ParsedRequest request) throws SQLException {
        List<Integer> menuItemIds = deserializeList(request.getJsonData(), Integer.class);
        String response;
        try {
            if(_preparedMenuItemService.addPreparedMenuItems(menuItemIds)) {
                response = createResponse(ResponseCode.OK, serializeData("Final Menu Updated for " + extractDate(new Date())));
                Notification notification = new Notification(NotificationTypeEnum.NEXT_DAY_FINAL_MENU.ordinal()+1, "Final Menu Updated for " + extractDate(new Date()), new Date());
                _notificationService.sendNotificationToAllEmployees(notification);
            } else {
                response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while updating Next Day Final Menu. Please Try Again."));
            }
        } catch (DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData(e.getMessage()));
        }
        return response;
    }
}