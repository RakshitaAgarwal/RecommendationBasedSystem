package org.cafeteria.common.model;

import java.sql.Date;

public class DailyRecommendation {
    private String dailyRecommendationId;
    private String menuItemId;
    private MealType mealType;
    private int votes;
    private Date dateTime;

    public String getDailyRecommendationId() {
        return dailyRecommendationId;
    }

    public void setDailyRecommendationId(String dailyRecommendationId) {
        this.dailyRecommendationId = dailyRecommendationId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
