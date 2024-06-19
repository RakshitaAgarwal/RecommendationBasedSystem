package org.cafeteria.server.services;

import org.cafeteria.common.model.Notification;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.util.List;

public class NotificationService implements INotificationService {
    @Override
    public boolean validate(Notification item) {
        return false;
    }

    @Override
    public boolean add(Notification object) {

        return false;
    }

    @Override
    public boolean update(Notification object) {

        return false;
    }

    @Override
    public void delete(Notification object) {

    }

    @Override
    public List<Notification> getAll() {

        return null;
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
