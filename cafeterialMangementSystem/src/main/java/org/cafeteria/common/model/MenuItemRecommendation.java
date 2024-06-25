package org.cafeteria.common.model;

public class MenuItemRecommendation {
    private int menuItemId;
    private double recommendationScore;
    private String sentimentOfItem;

    public MenuItemRecommendation(int menuItemId, double recommendationScore, String sentimentOfItem) {
        this.menuItemId = menuItemId;
        this.recommendationScore = recommendationScore;
        this.sentimentOfItem = sentimentOfItem;
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