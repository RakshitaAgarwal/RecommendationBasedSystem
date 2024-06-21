package org.cafeteria.server.services;

import org.cafeteria.common.model.Notification;
import org.cafeteria.common.model.User;
import org.cafeteria.server.repositories.NotificationRepository;
import org.cafeteria.server.repositories.UserRepository;
import org.cafeteria.server.repositories.interfaces.INotificationRepository;
import org.cafeteria.server.repositories.interfaces.IUserRepository;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class NotificationService implements INotificationService {
    private static INotificationRepository _notificationRepository;
    private static IUserRepository _userRepository;

    public NotificationService() {
        _notificationRepository = new NotificationRepository();
        _userRepository = new UserRepository();
    }

    @Override
    public boolean sendNotificationToAllEmployees(Notification notification) throws SQLException {
        List<User> users = _userRepository.GetAll();
        for(User user:users) {
            notification.setUserId(user.getId());
            notification.setDateTime(Calendar.getInstance().getTime());
            notification.setNotificationRead(false);
            _notificationRepository.add(notification);
        }
        return true;
    }

    @Override
    public List<Notification> getUserNotification(int userId) throws SQLException {
        return _notificationRepository.getNotificationByUserId(userId);
    }

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
    public boolean delete(Notification object) {

        return false;
    }

    @Override
    public List<Notification> getAll() {

        return null;
    }

    @Override
    public Notification getById(int id) {
        return null;
    }

    @Override
    public void markNotificationAsRead() {

    }

    @Override
    public void cleanUpReadNotification() {

    }

    @Override
    public void cleanUpExpiredNotification() {}

}
