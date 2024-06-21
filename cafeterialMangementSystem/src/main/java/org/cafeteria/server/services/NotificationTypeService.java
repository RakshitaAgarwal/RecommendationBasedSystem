package org.cafeteria.server.services;

import org.cafeteria.common.model.NotificationType;
import org.cafeteria.server.services.interfaces.INotificationTypeService;

import java.util.List;

public class NotificationTypeService implements INotificationTypeService {
    @Override
    public boolean validate(NotificationType item) {
        return false;
    }

    @Override
    public boolean add(NotificationType object) {

        return false;
    }

    @Override
    public boolean update(NotificationType object) {

        return false;
    }

    @Override
    public boolean delete(NotificationType object) {

        return false;
    }

    @Override
    public List<NotificationType> getAll() {

        return null;
    }

    @Override
    public NotificationType getById(int id) {
        return null;
    }
}