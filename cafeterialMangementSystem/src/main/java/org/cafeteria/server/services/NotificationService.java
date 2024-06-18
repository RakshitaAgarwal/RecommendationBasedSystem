package org.cafeteria.server.services;

import org.cafeteria.common.model.Notification;
import org.cafeteria.server.services.interfaces.INotificationService;

public class NotificationService implements INotificationService {
    @Override
    public boolean validate(Notification item) {
        return false;
    }

    @Override
    public void add(Notification object) {

    }

    @Override
    public void update(Notification object) {

    }

    @Override
    public void delete(Notification object) {

    }

    @Override
    public void getAll(Notification object) {

    }

    @Override
    public void getById(Notification object) {

    }

    @Override
    public void markNotificationAsRead() {

    }

    @Override
    public void cleanUpReadNotification() {

    }

    @Override
    public void cleanUpExpiredNotification() {

    }

    @Override
    public void getUserNotification() {

    }
}
