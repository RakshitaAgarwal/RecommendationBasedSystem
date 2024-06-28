package org.cafeteria.common.model;

import java.util.Date;

public class MenuItem {
    private int id;
    private String name;
    private float price;
    private boolean isAvailable;
    private int mealTypeId;

    private Date lastTimePrepared = null;

    public MenuItem() {}

    public MenuItem(String name, float price, boolean isAvailable, int mealTypeId) {
        this.id = -1;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.mealTypeId = mealTypeId;
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

    public Date getLastTimePrepared() {
        return lastTimePrepared;
    }

    public void setLastTimePrepared(Date lastTimePrepared) {
        this.lastTimePrepared = lastTimePrepared;
    }
}