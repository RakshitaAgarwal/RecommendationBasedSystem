package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDailyRecommendationService extends IValidationService<DailyRecommendation>, ICrudService<DailyRecommendation> {
    public Map<MealTypeEnum, List<MenuItem>> getDailyRecommendation() throws SQLException;
    public void performSentimentAnalysis();
    public void voteForNextDayMenu();
    public boolean rollOutItemsForNextDayMenu(Map<MealTypeEnum, List<MenuItem>> nextDayMenuOptions) throws SQLException;
}
