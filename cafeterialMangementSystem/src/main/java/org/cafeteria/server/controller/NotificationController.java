package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.Notification;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.User;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;
import java.util.List;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class NotificationController {
    private static INotificationService _notificationService;
    public NotificationController() {
        _notificationService = new NotificationService();
    }
    public String getUserNotification(@NotNull ParsedRequest request) throws SQLException {
        User user = deserializeData(request.getJsonData(), User.class);
        List<Notification> userNotification = _notificationService.getUserNotification(user.getId());
        String response;
        if(userNotification != null) {
            response = createResponse(ResponseCode.OK, serializeData(userNotification));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, null);
        }
        return response;
    }

}
