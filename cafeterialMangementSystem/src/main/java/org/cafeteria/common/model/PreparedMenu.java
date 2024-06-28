package org.cafeteria.common.model;

import java.util.Date;

public class PreparedMenu {
    private int id;
    private int menuItemId;
    private Date date;

    public PreparedMenu(int menuItemId, Date date) {
        this.menuItemId = menuItemId;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}