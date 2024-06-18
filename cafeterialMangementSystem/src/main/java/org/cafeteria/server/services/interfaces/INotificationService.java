package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Notification;

public interface INotificationService extends IValidationService<Notification>, ICrudService<Notification> {
    public void markNotificationAsRead();

    public void cleanUpReadNotification();

    public void cleanUpExpiredNotification();

    public void getUserNotification();

}
