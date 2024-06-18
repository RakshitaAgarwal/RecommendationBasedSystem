package org.cafeteria.common.model;

public class MealType {
    private int id;
    private String mealType;

    public MealType() {
    }

    public MealType(int id, String mealType) {
        this.id = id;
        this.mealType = mealType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
