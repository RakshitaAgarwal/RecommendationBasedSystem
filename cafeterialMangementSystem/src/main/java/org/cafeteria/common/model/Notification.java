package org.cafeteria.common.model;

import java.util.Date;

public class Notification {
    private int id;
    private int notificationTypeId;
    private String notificationMessage;
    private Date dateTime;
    private Boolean isNotificationRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
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
