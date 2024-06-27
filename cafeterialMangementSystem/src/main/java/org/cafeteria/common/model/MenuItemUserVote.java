package org.cafeteria.common.model;

import java.util.Date;

public class MenuItemUserVote {
    private int id;
    private int menuItemId;
    private int userId;

    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public MenuItemUserVote() {}

    public MenuItemUserVote(int menuItemId, int userId, Date dateTime) {
        this.menuItemId = menuItemId;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
