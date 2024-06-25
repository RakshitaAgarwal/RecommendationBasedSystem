package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItemRecommendation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDailyRecommendationService extends IValidationService<DailyRecommendation>, ICrudService<DailyRecommendation> {
    public Map<MealTypeEnum, List<MenuItemRecommendation>> getDailyRecommendation() throws SQLException;
    public void voteForNextDayMenu();
    public boolean rollOutItemsForNextDayMenu(List<Integer> rolledOutMenuItemIds) throws SQLException;

    public Map<MealTypeEnum, List<MenuItemRecommendation>> getNextDayMenuOptions() throws SQLException;
}
