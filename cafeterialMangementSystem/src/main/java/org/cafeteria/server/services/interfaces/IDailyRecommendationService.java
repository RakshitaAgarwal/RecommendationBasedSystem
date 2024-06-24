package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemScore;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDailyRecommendationService extends IValidationService<DailyRecommendation>, ICrudService<DailyRecommendation> {
    public Map<MealTypeEnum, List<MenuItemScore>> getDailyRecommendation() throws SQLException;
    public void performSentimentAnalysis();
    public void voteForNextDayMenu();
    public boolean rollOutItemsForNextDayMenu(List<Integer> rolledOutMenuItemIds) throws SQLException;
}
