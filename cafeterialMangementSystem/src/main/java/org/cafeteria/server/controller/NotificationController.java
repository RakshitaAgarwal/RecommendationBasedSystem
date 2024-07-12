package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.Notification;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.User;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.INotificationService;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.*;

import java.sql.SQLException;
import java.util.List;

public class NotificationController {
    private static INotificationService _notificationService;
    public NotificationController() {
        _notificationService = new NotificationService();
    }
    public String getUserNotification(@NotNull ParsedRequest request) throws SQLException {
        User user = deserializeData(request.getJsonData(), User.class);
        List<Notification> userNotifications = _notificationService.getUserNotification(user.getId());
        String response;
        if(userNotifications != null) {
            response = createResponse(ResponseCode.OK, serializeData(userNotifications));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No Notification Found for: " + user.getName()));
        }
        return response;
    }

    public String sendNotificationToAllEmployees(@NotNull ParsedRequest request) throws SQLException {
        Notification notification = deserializeData(request.getJsonData(), Notification.class);
        String response;
        if(_notificationService.sendNotificationToAllEmployees(notification)) {
            response = createResponse(ResponseCode.OK, serializeData("Notification sent successfully"));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, "Some error occurred while rolling out notification to employees. Please try again.");
        }
        return response;
    }

    public String updateNotificationReadStatus(ParsedRequest request) throws SQLException {
        List<Notification> notifications = deserializeList(request.getJsonData(), Notification.class);
        String response;
        if(_notificationService.updateNotificationReadStatus(notifications)) {
            response = createResponse(ResponseCode.OK, serializeData("Notification Read Status Updated Successfully"));
        } else {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData("Some Error occurred while updating notification read status"));
        }
        return response;
    }
}