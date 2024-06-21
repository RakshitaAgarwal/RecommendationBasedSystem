package org.cafeteria.common.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuItemByMeals {
    private Map<MealType, List<MenuItem>> menuItemsByMealType;

    public MenuItemByMeals() {
        this.menuItemsByMealType = new HashMap<>();
    }

    public Map<MealType, List<MenuItem>> getMenuItemsByMealType() {
        return menuItemsByMealType;
    }

    public void setMenuItemsByMealType(Map<MealType, List<MenuItem>> menuItemsByMealType) {
        this.menuItemsByMealType = menuItemsByMealType;
    }
}
