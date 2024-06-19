package org.cafeteria.server.handlers;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;

public class NotificationHandler {
    private static INotificationService _notificationService;
    public NotificationHandler() {
        _notificationService = new NotificationService();
    }
    public String getUserNotification(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}
