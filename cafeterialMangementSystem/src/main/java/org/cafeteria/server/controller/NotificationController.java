package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;

public class NotificationController {
    private static INotificationService _notificationService;
    public NotificationController() {
        _notificationService = new NotificationService();
    }
    public String getUserNotification(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}
