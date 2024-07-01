package org.cafeteria.common.model;

import java.util.Date;

public class MenuItem {
    private int id;
    private String name;
    private float price;
    private boolean isAvailable;
    private int mealTypeId;

    private int menuItemTypeId;
    private int sweetContentLevelId;
    private int spiceContentLevelId;
    private int cuisineTypeId;
    private Date lastTimePrepared = null;

    public MenuItem() {}

    public MenuItem(String name, float price, boolean isAvailable, int mealTypeId, int menuItemTypeId, int sweetContentLevelId, int spiceContentLevelId, int cuisineTypeId) {
        this.id = -1;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.mealTypeId = mealTypeId;
        this.menuItemTypeId = menuItemTypeId;
        this.sweetContentLevelId = sweetContentLevelId;
        this.spiceContentLevelId = spiceContentLevelId;
        this.cuisineTypeId = cuisineTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getMealTypeId() {
        return mealTypeId;
    }

    public void setMealTypeId(int mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

    public int getMenuItemTypeId() {
        return menuItemTypeId;
    }

    public void setMenuItemTypeId(int menuItemTypeId) {
        this.menuItemTypeId = menuItemTypeId;
    }

    public int getSweetContentLevelId() {
        return sweetContentLevelId;
    }

    public void setSweetContentLevelId(int sweetContentLevelId) {
        this.sweetContentLevelId = sweetContentLevelId;
    }

    public int getSpiceContentLevelId() {
        return spiceContentLevelId;
    }

    public void setSpiceContentLevelId(int spiceContentLevelId) {
        this.spiceContentLevelId = spiceContentLevelId;
    }

    public int getCuisineTypeId() {
        return cuisineTypeId;
    }

    public void setCuisineTypeId(int cuisineTypeId) {
        this.cuisineTypeId = cuisineTypeId;
    }

    public Date getLastTimePrepared() {
        return lastTimePrepared;
    }

    public void setLastTimePrepared(Date lastTimePrepared) {
        this.lastTimePrepared = lastTimePrepared;
    }
}