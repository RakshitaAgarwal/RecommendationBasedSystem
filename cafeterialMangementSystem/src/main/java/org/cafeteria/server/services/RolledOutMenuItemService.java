package org.cafeteria.server.services;

import org.cafeteria.common.model.RolledOutMenuItem;
import org.cafeteria.server.repositories.RolledOutMenuItemRepository;
import org.cafeteria.server.repositories.interfaces.IRolledOutMenuItemRepository;
import org.cafeteria.server.services.interfaces.IRolledOutMenuItemService;
import static org.cafeteria.common.constants.Constants.DATE_FORMAT;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RolledOutMenuItemService implements IRolledOutMenuItemService {
    private static IRolledOutMenuItemRepository _rollOutMenuItemRepository;

    public RolledOutMenuItemService() {
        _rollOutMenuItemRepository = new RolledOutMenuItemRepository();
    }

    @Override
    public boolean rollOutNextDayMenuOptions(List<Integer> rolledOutMenuItemIds) throws SQLException {
        for (int menuItemId : rolledOutMenuItemIds) {
            RolledOutMenuItem dailyRecommendation = new RolledOutMenuItem();
            dailyRecommendation.setMenuItemId(menuItemId);
            dailyRecommendation.setRolledOutDate(new Date());

            _rollOutMenuItemRepository.add(dailyRecommendation);
        }
        return true;
    }

    @Override
    public List<RolledOutMenuItem> getNextDayMenuOptions() throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String currentDate = dateFormat.format(new Date());
        System.out.println(currentDate);
        return _rollOutMenuItemRepository.getByDate(currentDate);
    }

    @Override
    public boolean validate(RolledOutMenuItem item) {
        return false;
    }

    @Override
    public boolean add(RolledOutMenuItem object) {
        return false;
    }

    @Override
    public boolean update(RolledOutMenuItem object) {
        return false;
    }

    @Override
    public boolean delete(RolledOutMenuItem object) {
        return false;
    }

    @Override
    public List<RolledOutMenuItem> getAll() {
        return null;
    }

    @Override
    public RolledOutMenuItem getById(int id) {
        return null;
    }
}

//    @Override
//    public Map<MealTypeEnum, List<MenuItemRecommendation>> getDailyRecommendation() throws SQLException {
//        Map<MealTypeEnum, List<MenuItemRecommendation>> recommendedItemsByMeal = new HashMap<>();
//        for (int i = 1; i <= MealTypeEnum.values().length; i++) {
//            List<MenuItem> mealTypeMenuItems = _menuService.getByMealTypeId(i);
//            System.out.println("MealTypeMenuItems size: " + mealTypeMenuItems.size());
//            List<MenuItemRecommendation> mealRecommendations = recommendationEngine.getTopRecommendedItems(
//                    filterMealTypeItems(mealTypeMenuItems),
//                    5
//            );
//            recommendedItemsByMeal.put(getEnumFromOrdinal(MealTypeEnum.class, i), mealRecommendations);
//        }
//        return recommendedItemsByMeal;
//    }

//    private List<Integer> filterMealTypeItems(List<MenuItem> mealTypeItems) {
//        List<MenuItem> filteredMenuItems = filterMenuItemsByLastPrepared(mealTypeItems);
//        filteredMenuItems = filterMenuItemBasedOnAvailability(filteredMenuItems);
//        List<Integer> filteredMealTypeItems = new ArrayList<>();
//        for(MenuItem menuItem :filteredMenuItems) {
//            filteredMealTypeItems.add(menuItem.getId());
//        }
//        return filteredMealTypeItems;
//    }

//    private Map<MealTypeEnum, List<MenuItemRecommendation>> getRecommendationForNextDayMenuOptions(List<RolledOutMenuItem> nextDayMenuOptions) throws SQLException {
//        Map<MealTypeEnum, List<MenuItemRecommendation>> result = new HashMap<>();
//
//        for (RolledOutMenuItem nextDayMenuOption : nextDayMenuOptions) {
//            int menuItemId = nextDayMenuOption.getMenuItemId();
//            MenuItem menuItem = _menuService.getById(menuItemId);
//            MealTypeEnum mealType = getEnumFromOrdinal(MealTypeEnum.class, menuItem.getMealTypeId());
//            MenuItemRecommendation menuItemRecommendation = recommendationEngine.evaluateMenuItemRecommendation(menuItemId);
//            result.computeIfAbsent(mealType, k -> new ArrayList<>()).add(menuItemRecommendation);
//        }
//        return result;
//    }