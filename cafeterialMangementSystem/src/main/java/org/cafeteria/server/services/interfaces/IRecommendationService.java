package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.enums.MealTypeEnum;
import org.cafeteria.common.model.MenuItemRecommendation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IRecommendationService {
    public Map<MealTypeEnum, List<MenuItemRecommendation>> getRecommendationForNextDayMenu() throws SQLException;

    public MenuItemRecommendation getRecommendationForMenuItem(int menuItemId) throws SQLException;
}
