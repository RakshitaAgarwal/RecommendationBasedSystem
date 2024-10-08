package org.cafeteria.server.services;

import org.cafeteria.common.model.Notification;
import org.cafeteria.common.model.User;
import org.cafeteria.common.model.enums.UserRoleEnum;
import org.cafeteria.server.repositories.NotificationRepository;
import org.cafeteria.server.repositories.UserRepository;
import org.cafeteria.server.repositories.interfaces.INotificationRepository;
import org.cafeteria.server.repositories.interfaces.IUserRepository;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;
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
        List<User> employees = _userRepository.getByUserRoleId(UserRoleEnum.EMP.ordinal() + 1);
        for (User employee : employees) {
            notification.setUserId(employee.getId());
            _notificationRepository.add(notification);
        }
        return true;
    }

    @Override
    public List<Notification> getUserNotification(int userId) throws SQLException {
        return _notificationRepository.getNotificationByUserId(userId);
    }

    @Override
    public boolean updateNotificationReadStatus(List<Notification> notifications) throws SQLException {
        boolean status = true;
        for (Notification notification : notifications) {
            if (!update(notification)) {
                status = false;
            }
        }
        return status;
    }

    @Override
    public boolean add(Notification object) {
        return false;
    }

    @Override
    public boolean update(Notification object) throws SQLException {
        return _notificationRepository.update(object);
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
}