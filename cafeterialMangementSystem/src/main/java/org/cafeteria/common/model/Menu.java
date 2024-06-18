package org.cafeteria.common.model;

import java.sql.Date;

public class Menu {
    private int id;
    private String name;
    private float price;
    private boolean isAvailable;
    private Date lastTimePrepared;

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

    public Date getLastTimePrepared() {
        return lastTimePrepared;
    }

    public void setLastTimePrepared(Date lastTimePrepared) {
        this.lastTimePrepared = lastTimePrepared;
    }
}
