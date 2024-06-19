package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Notification;
import org.cafeteria.server.repositories.interfaces.INotificationRepository;

import java.util.List;

public class NotificationRepository implements INotificationRepository {
    @Override
    public boolean add(Notification item) {

        return false;
    }

    @Override
    public boolean delete(Notification item) {

        return false;
    }

    @Override
    public boolean update(Notification item) {

        return false;
    }

    @Override
    public List<Notification> GetAll() {
        return null;
    }

    @Override
    public Notification getById(int id) {
        return null;
    }
}