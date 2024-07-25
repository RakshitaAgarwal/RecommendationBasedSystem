package org.cafeteria.common.model;

import java.util.Date;

public class DetailedFeedbackRequest {
    private int id;
    private int menuItemId;
    private Date dateTime;

    public DetailedFeedbackRequest() {
    }

    public DetailedFeedbackRequest(int menuItemId, Date dateTime) {
        this.menuItemId = menuItemId;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}