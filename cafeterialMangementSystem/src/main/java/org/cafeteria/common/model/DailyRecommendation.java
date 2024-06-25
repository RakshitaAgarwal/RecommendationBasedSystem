package org.cafeteria.common.model;

import java.util.Date;

public class DailyRecommendation {
    private int id;
    private int menuItemId;
    private int mealTypeId;
    private int votes;
    private Date rollOutDate;

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

    public int getMealTypeId() {
        return mealTypeId;
    }

    public void setMealTypeId(int mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Date getRollOutDate() {
        return rollOutDate;
    }

    public void setRollOutDate(Date rollOutDate) {
        this.rollOutDate = rollOutDate;
    }
}
