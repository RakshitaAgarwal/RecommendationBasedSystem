package org.cafeteria.common.model;

import java.util.Date;

public class RolledOutMenuItem {
    private int id;
    private int menuItemId;
    private Date rolledOutDate;

    public RolledOutMenuItem() {
    }

    public RolledOutMenuItem(int id, int menuItemId, Date rolledOutDate) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.rolledOutDate = rolledOutDate;
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

    public Date getRolledOutDate() {
        return rolledOutDate;
    }

    public void setRolledOutDate(Date rolledOutDate) {
        this.rolledOutDate = rolledOutDate;
    }
}