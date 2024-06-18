package org.cafeteria.common.model;

import java.sql.Date;

public class Notification {
    private String notificationId;
    private String notificationTypeId;
    private String notificationMessage;
    private Date dateTime;
    private Boolean isNotificationRead;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(String notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getNotificationRead() {
        return isNotificationRead;
    }

    public void setNotificationRead(Boolean notificationRead) {
        isNotificationRead = notificationRead;
    }
}
