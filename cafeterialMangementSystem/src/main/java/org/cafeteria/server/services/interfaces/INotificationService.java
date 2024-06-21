package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Notification;

import java.sql.SQLException;
import java.util.List;

public interface INotificationService extends IValidationService<Notification>, ICrudService<Notification> {
    public boolean sendNotificationToAllEmployees(Notification notification) throws SQLException;
    public List<Notification> getUserNotification(int userId) throws SQLException;
    public void markNotificationAsRead();

    public void cleanUpReadNotification();

    public void cleanUpExpiredNotification();

}
