package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.Notification;

import java.sql.SQLException;
import java.util.List;

public interface INotificationRepository extends ICrudRepository<Notification> {
    public List<Notification> getNotificationByUserId(int id) throws SQLException;
}
