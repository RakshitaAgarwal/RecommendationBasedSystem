package org.cafeteria.server.services;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemScore;
import org.cafeteria.server.repositories.DailyRecommendationRepository;
import org.cafeteria.server.repositories.interfaces.IDailyRecommendationRepository;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;
import org.cafeteria.server.services.interfaces.IFeedbackService;
import org.cafeteria.server.services.interfaces.IMenuService;

import java.sql.SQLException;
import java.util.*;

import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

public class DailyRecommendationService implements IDailyRecommendationService {
    private static IDailyRecommendationRepository _dailyRecommendationRepository;
    private static IFeedbackService _feedbackService;
    private static IMenuService _menuService;

    public DailyRecommendationService() {
        _dailyRecommendationRepository = new DailyRecommendationRepository();
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
    public Map<MealTypeEnum, List<MenuItemScore>> getDailyRecommendation() throws SQLException {
        Map<MealTypeEnum, List<MenuItemScore>> recommendedItemsByMeal = new HashMap<>();
        RecommendationEngine recommendationEngine = new RecommendationEngine(_feedbackService);
        for (int i = 1; i <= MealTypeEnum.values().length; i++) {
            List<MenuItem> mealTypeItems = _menuService.getByMealTypeId(i);
            System.out.println("RecommendationEngine" + mealTypeItems.size());
            List<MenuItemScore> mealRecommendations = recommendationEngine.getTopRecommendedItems(mealTypeItems, 5);
            recommendedItemsByMeal.put(getEnumFromOrdinal(MealTypeEnum.class, i), mealRecommendations);
        }
        return recommendedItemsByMeal;
    }

//    private List<MenuItem> getMealTypeRecommendations(RecommendationEngine recommendationEngine, List<MenuItem> mealTypeItems, int noOfRecommendation) throws SQLException {
//        List<MenuItemScore> mealTypeRecommendations = recommendationEngine.getTopRecommendedItems(mealTypeItems, noOfRecommendation);
//        System.out.println("RecommendationEngine" + mealTypeRecommendations.size());
//        List<MenuItem> breakfastRecommendationList = new ArrayList<>();
//        for (MenuItemScore breakfastRecommendation : mealTypeRecommendations) {
//            MenuItem menuItem = _menuService.getById(breakfastRecommendation.getMenuItemId());
//            breakfastRecommendationList.add(menuItem);
//        }
//        return breakfastRecommendationList;
//    }

    @Override
    public void performSentimentAnalysis() {

    }

    @Override
    public void voteForNextDayMenu() {

    }

    @Override
    public boolean rollOutItemsForNextDayMenu(List<Integer> rolledOutMenuItemIds) throws SQLException {
        for (int menuItemId : rolledOutMenuItemIds) {
            DailyRecommendation dailyRecommendation = new DailyRecommendation();
            dailyRecommendation.setMenuItemId(menuItemId);
            dailyRecommendation.setVotes(0);
            dailyRecommendation.setDateTime(new Date());

            _dailyRecommendationRepository.add(dailyRecommendation);
        }
        return true;
    }
}