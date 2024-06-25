package org.cafeteria.server.services;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.server.repositories.DailyRecommendationRepository;
import org.cafeteria.server.repositories.interfaces.IDailyRecommendationRepository;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;
import org.cafeteria.server.services.interfaces.IFeedbackService;
import org.cafeteria.server.services.interfaces.IMenuService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.cafeteria.common.constants.Constants.DATE_FORMAT;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;
import static org.cafeteria.server.services.MenuService.filterMenuItemBasedOnAvailability;
import static org.cafeteria.server.services.MenuService.filterMenuItemsByLastPrepared;

public class DailyRecommendationService implements IDailyRecommendationService {
    private static IDailyRecommendationRepository _dailyRecommendationRepository;
    private static IFeedbackService _feedbackService;
    private static IMenuService _menuService;

    private static RecommendationEngine recommendationEngine;

    public DailyRecommendationService() {
        _dailyRecommendationRepository = new DailyRecommendationRepository();
        _feedbackService = new FeedbackService();
        _menuService = new MenuService();
        recommendationEngine = new RecommendationEngine(_feedbackService);
    }

    @Override
    public Map<MealTypeEnum, List<MenuItemRecommendation>> getDailyRecommendation() throws SQLException {
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

    private List<Integer> filterMealTypeItems(List<MenuItem> mealTypeItems) {
        List<MenuItem> filteredMenuItems = filterMenuItemsByLastPrepared(mealTypeItems);
        filteredMenuItems = filterMenuItemBasedOnAvailability(filteredMenuItems);
        List<Integer> filteredMealTypeItems = new ArrayList<>();
        for(MenuItem menuItem :filteredMenuItems) {
            filteredMealTypeItems.add(menuItem.getId());
        }
        return filteredMealTypeItems;
    }

    @Override
    public boolean rollOutItemsForNextDayMenu(List<Integer> rolledOutMenuItemIds) throws SQLException {
        for (int menuItemId : rolledOutMenuItemIds) {
            DailyRecommendation dailyRecommendation = new DailyRecommendation();
            dailyRecommendation.setMenuItemId(menuItemId);
            dailyRecommendation.setVotes(0);
            dailyRecommendation.setRolledOutDate(new Date());

            _dailyRecommendationRepository.add(dailyRecommendation);
        }
        return true;
    }

    @Override
    public Map<MealTypeEnum, List<MenuItemRecommendation>> getNextDayMenuOptions() throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String currentDate = dateFormat.format(new Date());
        System.out.println(currentDate);
        List<DailyRecommendation> nextDayMenuOptions = _dailyRecommendationRepository.getByDate(currentDate);
        Map<MealTypeEnum, List<MenuItemRecommendation>> result = new HashMap<>();

        for (DailyRecommendation nextDayMenuOption : nextDayMenuOptions) {
            int menuItemId = nextDayMenuOption.getMenuItemId();
            MenuItem menuItem = _menuService.getById(menuItemId);
            MealTypeEnum mealType = getEnumFromOrdinal(MealTypeEnum.class, menuItem.getMealTypeId());
            MenuItemRecommendation menuItemRecommendation = recommendationEngine.evaluateMenuItemRecommendation(menuItemId);
            result.computeIfAbsent(mealType, k -> new ArrayList<>()).add(menuItemRecommendation);
        }
        return result;
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
    public void voteForNextDayMenu() {

    }
}