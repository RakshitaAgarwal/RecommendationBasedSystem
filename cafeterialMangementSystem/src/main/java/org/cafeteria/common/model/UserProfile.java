package org.cafeteria.common.model;

public class UserProfile {
    private int id;
    private int userId;
    private int dietaryPreferenceId;
    private int spiceLevelId;
    private int favCuisineId;
    private boolean isSweetTooth;

    public UserProfile() {
    }

    public UserProfile(int userId, int dietaryPreferenceId, int spiceLevelId, int favCuisineId, boolean isSweetTooth) {
        this.userId = userId;
        this.dietaryPreferenceId = dietaryPreferenceId;
        this.spiceLevelId = spiceLevelId;
        this.favCuisineId = favCuisineId;
        this.isSweetTooth = isSweetTooth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDietaryPreferenceId() {
        return dietaryPreferenceId;
    }

    public void setDietaryPreferenceId(int dietaryPreferenceId) {
        this.dietaryPreferenceId = dietaryPreferenceId;
    }

    public int getSpiceLevelId() {
        return spiceLevelId;
    }

    public void setSpiceLevelId(int spiceLevelId) {
        this.spiceLevelId = spiceLevelId;
    }

    public int getFavCuisineId() {
        return favCuisineId;
    }

    public void setFavCuisineId(int favCuisineId) {
        this.favCuisineId = favCuisineId;
    }

    public boolean isSweetTooth() {
        return isSweetTooth;
    }

    public void setSweetTooth(boolean sweetTooth) {
        isSweetTooth = sweetTooth;
    }
}