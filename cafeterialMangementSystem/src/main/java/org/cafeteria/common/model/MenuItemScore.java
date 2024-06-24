package org.cafeteria.common.model;

public class MenuItemScore {
    private int menuItemId;
    private String menuItemName;
    private double recommendationScore;
    private String sentimentOfItem;

    public MenuItemScore(int menuItemId, String menuItemName, double recommendationScore, String sentimentOfItem) {
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.recommendationScore = recommendationScore;
        this.sentimentOfItem = sentimentOfItem;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getSentimentOfItem() {
        return sentimentOfItem;
    }

    public void setSentimentOfItem(String sentimentOfItem) {
        this.sentimentOfItem = sentimentOfItem;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public double getRecommendationScore() {
        return recommendationScore;
    }

    public void setRecommendationScore(double recommendationScore) {
        this.recommendationScore = recommendationScore;
    }

    @Override
    public String toString() {
        return "MenuItemScore{" +
                "menuItemId=" + menuItemId +
                ", recommendationScore=" + recommendationScore +
                '}';
    }
}