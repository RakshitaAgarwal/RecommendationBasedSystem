package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.RolledOutMenuItem;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItemRecommendation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IRolledOutMenuItemService extends IValidationService<RolledOutMenuItem>, ICrudService<RolledOutMenuItem> {
    public Map<MealTypeEnum, List<MenuItemRecommendation>> getDailyRecommendation() throws SQLException;
    public boolean rollOutItemsForNextDayMenu(List<Integer> rolledOutMenuItemIds) throws SQLException;

    public List<RolledOutMenuItem> getNextDayMenuOptions() throws SQLException;
}
