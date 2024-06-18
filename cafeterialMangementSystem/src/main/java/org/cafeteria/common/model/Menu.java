package org.cafeteria.common.model;

import java.sql.Date;

public class Menu {
    private String menuItemId;
    private String menuItemName;
    private float menuItemPrice;
    private boolean isMenuItemAvailable;
    private Date lastTimePrepared;

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public float getMenuItemPrice() {
        return menuItemPrice;
    }

    public void setMenuItemPrice(float menuItemPrice) {
        this.menuItemPrice = menuItemPrice;
    }

    public boolean isMenuItemAvailable() {
        return isMenuItemAvailable;
    }

    public void setMenuItemAvailable(boolean menuItemAvailable) {
        isMenuItemAvailable = menuItemAvailable;
    }

    public Date getLastTimePrepared() {
        return lastTimePrepared;
    }

    public void setLastTimePrepared(Date lastTimePrepared) {
        this.lastTimePrepared = lastTimePrepared;
    }
}
