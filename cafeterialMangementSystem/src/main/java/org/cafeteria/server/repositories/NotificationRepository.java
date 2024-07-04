package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Notification;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.INotificationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

public class NotificationRepository implements INotificationRepository {
    private final Connection connection;

    public NotificationRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(Notification item) throws SQLException {
        String query = "INSERT INTO notification (userId, notificationTypeId, notificationMessage, dateTime, isNotificationRead) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getUserId());
            statement.setInt(2, item.getNotificationTypeId());
            statement.setString(3, item.getNotificationMessage());
            statement.setTimestamp(4, dateToTimestamp(item.getDateTime()));
            statement.setBoolean(5, item.getNotificationRead());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean addBatch(List<Notification> items) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Notification item) {
        return false;
    }

    @Override
    public boolean update(Notification item) throws SQLException {
        String query = "UPDATE notification SET userId = ?, notificationTypeId = ?, notificationMessage = ?, dateTime = ?, isNotificationRead = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getUserId());
            statement.setInt(2, item.getNotificationTypeId());
            statement.setString(3, item.getNotificationMessage());
            statement.setTimestamp(4, dateToTimestamp(item.getDateTime()));
            statement.setBoolean(5, item.getNotificationRead());
            statement.setInt(6, item.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<Notification> GetAll() throws SQLException {
        String query = "SELECT * FROM notification;";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                Notification notification = new Notification();
                notification.setNotificationTypeId(resultSet.getInt("notificationTypeId"));
                notification.setNotificationMessage(resultSet.getString("notificationMessage"));
                notification.setId(resultSet.getInt("id"));
                notification.setUserId(resultSet.getInt("userId"));
                notification.setDateTime(timestampToDate(resultSet.getTimestamp("dateTime")));
                notification.setNotificationRead(resultSet.getBoolean("isNotificationRead"));
                notifications.add(notification);
            }
            return notifications;
        }
    }

    @Override
    public Notification getById(int id) throws SQLException {
        String query = "SELECT * FROM notification WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationMessage(resultSet.getString("notificationMessage"));
                    notification.setNotificationTypeId(resultSet.getInt("notificationTypeId"));
                    notification.setId(resultSet.getInt("id"));
                    notification.setUserId(resultSet.getInt("userId"));
                    notification.setDateTime(timestampToDate(resultSet.getTimestamp("dateTime")));
                    notification.setNotificationRead(resultSet.getBoolean("isNotificationRead"));
                    return notification;
                }
                return null;
            }
        }
    }

    @Override
    public List<Notification> getNotificationByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM notification WHERE userId = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Notification> notifications = new ArrayList<>();
                while (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationTypeId(resultSet.getInt("notificationTypeId"));
                    notification.setNotificationMessage(resultSet.getString("notificationMessage"));
                    notification.setId(resultSet.getInt("id"));
                    notification.setUserId(resultSet.getInt("userId"));
                    notification.setDateTime(resultSet.getTimestamp("dateTime"));
                    notification.setNotificationRead(resultSet.getBoolean("isNotificationRead"));
                    notifications.add(notification);
                }
                return notifications;
            }
        }
    }
}