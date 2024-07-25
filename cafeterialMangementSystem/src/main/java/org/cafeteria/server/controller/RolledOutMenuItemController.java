package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.enums.NotificationTypeEnum;
import org.cafeteria.server.helper.UserActionHandler;
import org.cafeteria.server.services.RolledOutMenuItemService;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.IRolledOutMenuItemService;
import org.cafeteria.server.services.interfaces.INotificationService;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.deserializeList;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.serializeData;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class RolledOutMenuItemController extends BaseController {
    private static IRolledOutMenuItemService _rolledOutMenuItemService;
    private static INotificationService _notificationService;

    public RolledOutMenuItemController() {
        _rolledOutMenuItemService = new RolledOutMenuItemService();
        _notificationService = new NotificationService();
    }

    @UserActionHandler(UserAction.ROLL_OUT_NEXT_DAY_MENU_OPTIONS)
    public String rollOutNextDayMenuOptions(@NotNull ParsedRequest request) throws SQLException {
        List<Integer> nextDayMenuOptions = deserializeList(request.getJsonData(), Integer.class);
        String response;
        try {
            if (_rolledOutMenuItemService.rollOutNextDayMenuOptions(nextDayMenuOptions)) {
                response = createResponse(ResponseCode.OK, serializeData("Next Day Menu Options Successfully Rolled Out."));
                Notification notification = new Notification(NotificationTypeEnum.NEXT_DAY_OPTIONS.ordinal() + 1, "Next Day Menu options are updated. Please Cast your vote for the day", new Date());
                _notificationService.sendNotificationToAllEmployees(notification);
            } else {
                response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while rolling out next day menu options. Please Try again."));
            }
        } catch (CustomExceptions.DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData(e.getMessage()));
        }
        return response;
    }

    @UserActionHandler(UserAction.GET_NEXT_DAY_MENU_OPTIONS)
    public String getNextDayMenuOptions(@NotNull ParsedRequest request) throws SQLException {
        List<RolledOutMenuItem> recommendations = _rolledOutMenuItemService.getNextDayMenuOptions();
        String response;
        if (recommendations != null) {
            response = createResponse(ResponseCode.OK, serializeData(recommendations));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while fetching next day menu options. Please Try again."));
        }
        return response;
    }
}