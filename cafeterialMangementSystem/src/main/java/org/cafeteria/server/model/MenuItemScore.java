package org.cafeteria.server.model;

public class MenuItemScore {
    private int menuItemId;
    private double recommendationScore;

    public MenuItemScore(int menuItemId, double recommendationScore) {
        this.menuItemId = menuItemId;
        this.recommendationScore = recommendationScore;
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