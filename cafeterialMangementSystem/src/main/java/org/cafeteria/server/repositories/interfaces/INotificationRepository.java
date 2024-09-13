package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.Notification;

import java.sql.SQLException;
import java.util.List;

public interface INotificationRepository extends ICrudRepository<Notification> {
    List<Notification> getNotificationByUserId(int id) throws SQLException;
}
