package org.cafeteria.common.model;

public class DiscardMenuItem {
    private int id;
    private int menuItemId;
    private float avgRating;

    public DiscardMenuItem() {}

    public DiscardMenuItem(int id, int menuItemId, float rating) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.avgRating = rating;
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

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    @Override
    public String toString() {
        return "DiscardMenuItem{" +
                "id=" + id +
                ", menuItemId=" + menuItemId +
                '}';
    }
}