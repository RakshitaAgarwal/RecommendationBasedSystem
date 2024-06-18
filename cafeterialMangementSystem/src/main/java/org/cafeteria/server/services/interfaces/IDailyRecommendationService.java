package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.DailyRecommendation;

public interface IDailyRecommendationService extends IValidationService<DailyRecommendation>, ICrudService<DailyRecommendation> {
    public void getDailyRecommendation();
    public void performSentimentAnalysis();
    public void voteForNextDayMenu();
}
