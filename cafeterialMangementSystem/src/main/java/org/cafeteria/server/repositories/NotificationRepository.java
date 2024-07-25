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
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String COLUMN_PK_ID = "id";
    private static final String COLUMN_FK_USER_ID = "userId";
    private static final String COLUMN_FK_NOTIFICATION_TYPE_ID = "notificationTypeId";
    private static final String COLUMN_NOTIFICATION_MESSAGE = "notificationMessage";
    private static final String COLUMN_DATE_TIME = "dateTime";
    private static final String COLUMN_IS_NOTIFICATION_READ = "isNotificationRead";

    public NotificationRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(Notification item) throws SQLException {
        String query = "INSERT INTO " + TABLE_NOTIFICATION + " (" +
                COLUMN_FK_USER_ID + ", " +
                COLUMN_FK_NOTIFICATION_TYPE_ID + ", " +
                COLUMN_NOTIFICATION_MESSAGE + ", " +
                COLUMN_DATE_TIME + ", " +
                COLUMN_IS_NOTIFICATION_READ + ") VALUES (?, ?, ?, ?, ?);";
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
        String query = "UPDATE " + TABLE_NOTIFICATION + " SET " +
                COLUMN_FK_USER_ID + " = ?, " +
                COLUMN_FK_NOTIFICATION_TYPE_ID + " = ?, " +
                COLUMN_NOTIFICATION_MESSAGE + " = ?, " +
                COLUMN_DATE_TIME + " = ?, " +
                COLUMN_IS_NOTIFICATION_READ + " = ? WHERE " + COLUMN_PK_ID + " = ?;";
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
    public List<Notification> getAll() throws SQLException {
        String query = "SELECT * FROM " + TABLE_NOTIFICATION + ";";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                Notification notification = new Notification();
                notification.setNotificationTypeId(resultSet.getInt(COLUMN_FK_NOTIFICATION_TYPE_ID));
                notification.setNotificationMessage(resultSet.getString(COLUMN_NOTIFICATION_MESSAGE));
                notification.setId(resultSet.getInt(COLUMN_PK_ID));
                notification.setUserId(resultSet.getInt(COLUMN_FK_USER_ID));
                notification.setDateTime(timestampToDate(resultSet.getTimestamp(COLUMN_DATE_TIME)));
                notification.setNotificationRead(resultSet.getBoolean(COLUMN_IS_NOTIFICATION_READ));
                notifications.add(notification);
            }
            return notifications;
        }
    }

    @Override
    public Notification getById(int id) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + COLUMN_PK_ID + " = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationTypeId(resultSet.getInt(COLUMN_FK_NOTIFICATION_TYPE_ID));
                    notification.setNotificationMessage(resultSet.getString(COLUMN_NOTIFICATION_MESSAGE));
                    notification.setId(resultSet.getInt(COLUMN_PK_ID));
                    notification.setUserId(resultSet.getInt(COLUMN_FK_USER_ID));
                    notification.setDateTime(timestampToDate(resultSet.getTimestamp(COLUMN_DATE_TIME)));
                    notification.setNotificationRead(resultSet.getBoolean(COLUMN_IS_NOTIFICATION_READ));
                    return notification;
                }
                return null;
            }
        }
    }

    @Override
    public List<Notification> getNotificationByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + COLUMN_FK_USER_ID + " = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Notification> notifications = new ArrayList<>();
                while (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationTypeId(resultSet.getInt(COLUMN_FK_NOTIFICATION_TYPE_ID));
                    notification.setNotificationMessage(resultSet.getString(COLUMN_NOTIFICATION_MESSAGE));
                    notification.setId(resultSet.getInt(COLUMN_PK_ID));
                    notification.setUserId(resultSet.getInt(COLUMN_FK_USER_ID));
                    notification.setDateTime(timestampToDate(resultSet.getTimestamp(COLUMN_DATE_TIME)));
                    notification.setNotificationRead(resultSet.getBoolean(COLUMN_IS_NOTIFICATION_READ));
                    notifications.add(notification);
                }
                return notifications;
            }
        }
    }
}