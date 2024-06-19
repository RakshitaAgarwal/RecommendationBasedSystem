package org.cafeteria.server.repositories;

import org.cafeteria.common.model.NotificationType;
import org.cafeteria.server.repositories.interfaces.INotificationTypeRepository;

import java.util.List;

public class NotificationTypeRepository implements INotificationTypeRepository {
    @Override
    public boolean add(NotificationType item) {

        return false;
    }

    @Override
    public boolean delete(NotificationType item) {

        return false;
    }

    @Override
    public boolean update(NotificationType item) {

        return false;
    }

    @Override
    public List<NotificationType> GetAll() {
        return null;
    }

    @Override
    public NotificationType getById(int id) {
        return null;
    }
}