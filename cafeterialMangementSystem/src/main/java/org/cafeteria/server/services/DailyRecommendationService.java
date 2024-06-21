package org.cafeteria.server.services;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.server.model.MenuItemScore;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;
import org.cafeteria.server.services.interfaces.IFeedbackService;
import org.cafeteria.server.services.interfaces.IMenuService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyRecommendationService implements IDailyRecommendationService {
    private static IFeedbackService _feedbackService;
    private static IMenuService _menuService;

    public DailyRecommendationService() {
        _feedbackService = new FeedbackService();
        _menuService = new MenuService();
    }

    @Override
    public boolean validate(DailyRecommendation item) {
        return false;
    }

    @Override
    public boolean add(DailyRecommendation object) {

        return false;
    }

    @Override
    public boolean update(DailyRecommendation object) {

        return false;
    }

    @Override
    public boolean delete(DailyRecommendation object) {

        return false;
    }

    @Override
    public List<DailyRecommendation> getAll() {

        return null;
    }

    @Override
    public DailyRecommendation getById(int id) {
        return null;
    }


    @Override
    public Map<MealTypeEnum, List<MenuItem>> getDailyRecommendation() throws SQLException {
        RecommendationEngine breakfastEngine = new RecommendationEngine(_feedbackService);
        RecommendationEngine lunchEngine = new RecommendationEngine(_feedbackService);
        RecommendationEngine dinnerEngine = new RecommendationEngine(_feedbackService);

        List<MenuItem> menuItems = _menuService.getAll();

        List<MenuItemScore> breakfastRecommendations = breakfastEngine.getTopRecommendedItems(menuItems, 5);
        List<MenuItemScore> lunchRecommendations = lunchEngine.getTopRecommendedItems(menuItems, 5);
        List<MenuItemScore> dinnerRecommendations = dinnerEngine.getTopRecommendedItems(menuItems, 5);

        List<MenuItem> breakfastRecommendationList = new ArrayList<>();
        List<MenuItem> lunchRecommendationList = new ArrayList<>();
        List<MenuItem> dinnerRecommendationList = new ArrayList<>();

        for (MenuItemScore breakfastRecommendation : breakfastRecommendations) {
            MenuItem menuItem = _menuService.getById(breakfastRecommendation.getMenuItemId());
            breakfastRecommendationList.add(menuItem);
        }
        for (MenuItemScore lunchRecommendation : lunchRecommendations) {
            MenuItem menuItem = _menuService.getById(lunchRecommendation.getMenuItemId());
            lunchRecommendationList.add(menuItem);
        }
        for (MenuItemScore dinnerRecommendation : dinnerRecommendations) {
            MenuItem menuItem = _menuService.getById(dinnerRecommendation.getMenuItemId());
            dinnerRecommendationList.add(menuItem);
        }

        Map<MealTypeEnum, List<MenuItem>> recommendedItemsByMeal = new HashMap<>();
        recommendedItemsByMeal.put(MealTypeEnum.BREAKFAST ,breakfastRecommendationList);
        recommendedItemsByMeal.put(MealTypeEnum.LUNCH, lunchRecommendationList);
        recommendedItemsByMeal.put(MealTypeEnum.DINNER, dinnerRecommendationList);

        return recommendedItemsByMeal;

    }

    @Override
    public void performSentimentAnalysis() {

    }

    @Override
    public void voteForNextDayMenu() {

    }
}
