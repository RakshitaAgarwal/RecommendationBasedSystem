package org.cafeteria.common.model;

public class NotificationType {
    private String notificationTypeId;
    private String notificationType;
    private float expirationTimeInHrs;
    private int priority;

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(String notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public float getExpirationTimeInHrs() {
        return expirationTimeInHrs;
    }

    public void setExpirationTimeInHrs(float expirationTimeInHrs) {
        this.expirationTimeInHrs = expirationTimeInHrs;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
