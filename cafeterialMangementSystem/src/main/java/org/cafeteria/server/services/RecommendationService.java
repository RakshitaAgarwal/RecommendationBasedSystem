package org.cafeteria.server.services;

import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.server.repositories.RecommendationEngine;
import org.cafeteria.server.services.interfaces.IFeedbackService;
import org.cafeteria.server.services.interfaces.IMenuService;
import org.cafeteria.server.services.interfaces.IRecommendationService;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;
import static org.cafeteria.server.services.MenuService.filterMenuItemBasedOnAvailability;
import static org.cafeteria.server.services.MenuService.filterMenuItemsByLastPrepared;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class RecommendationService implements IRecommendationService {
    private static IMenuService _menuService;
    private static RecommendationEngine recommendationEngine;

    public RecommendationService() {
        IFeedbackService _feedbackService = new FeedbackService();
        recommendationEngine = new RecommendationEngine(_feedbackService);
        _menuService = new MenuService();
    }

    @Override
    public Map<MealTypeEnum, List<MenuItemRecommendation>> getRecommendationForNextDayMenu() throws SQLException {
        Map<MealTypeEnum, List<MenuItemRecommendation>> recommendedItemsByMeal = new HashMap<>();
        for (int i = 1; i <= MealTypeEnum.values().length; i++) {
            List<MenuItem> mealTypeMenuItems = _menuService.getByMealTypeId(i);
            System.out.println("MealTypeMenuItems size: " + mealTypeMenuItems.size());
            List<MenuItemRecommendation> mealRecommendations = recommendationEngine.getTopRecommendedItems(
                    filterMealTypeItems(mealTypeMenuItems),
                    5
            );
            recommendedItemsByMeal.put(getEnumFromOrdinal(MealTypeEnum.class, i), mealRecommendations);
        }
        return recommendedItemsByMeal;
    }

    @Override
    public MenuItemRecommendation getRecommendationForMenuItem(int menuItemId) throws SQLException {
        return recommendationEngine.evaluateMenuItemRecommendation(menuItemId);
    }

    private List<Integer> filterMealTypeItems(List<MenuItem> mealTypeItems) {
        List<MenuItem> filteredMenuItems = filterMenuItemsByLastPrepared(mealTypeItems);
        filteredMenuItems = filterMenuItemBasedOnAvailability(filteredMenuItems);
        List<Integer> filteredMealTypeItems = new ArrayList<>();
        for(MenuItem menuItem :filteredMenuItems) {
            filteredMealTypeItems.add(menuItem.getId());
        }
        return filteredMealTypeItems;
    }
}