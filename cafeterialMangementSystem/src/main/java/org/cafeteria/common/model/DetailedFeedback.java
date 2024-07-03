package org.cafeteria.common.model;

import java.util.Date;

public class DetailedFeedback {
    private int id;
    private int userId;
    private int menuItemId;
    private Date dateTime;

    public DetailedFeedback() {
    }

    public DetailedFeedback(int id, int userId, int menuItemId, Date dateTime) {
        this.id = id;
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
