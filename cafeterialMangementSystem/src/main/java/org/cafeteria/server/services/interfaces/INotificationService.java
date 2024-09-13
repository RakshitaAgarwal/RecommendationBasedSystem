package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Notification;
import java.sql.SQLException;
import java.util.List;

public interface INotificationService extends ICrudService<Notification> {
    boolean sendNotificationToAllEmployees(Notification notification) throws SQLException;
    List<Notification> getUserNotification(int userId) throws SQLException;
    boolean updateNotificationReadStatus(List<Notification> notifications) throws SQLException;
}