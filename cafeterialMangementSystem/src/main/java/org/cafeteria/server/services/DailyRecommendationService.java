package org.cafeteria.server.services;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;

import java.util.List;

public class DailyRecommendationService implements IDailyRecommendationService {
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
    public void delete(DailyRecommendation object) {

    }

    @Override
    public List<DailyRecommendation> getAll() {

        return null;
    }

    @Override
    public void getById(DailyRecommendation object) {

    }

    @Override
    public void getDailyRecommendation() {

    }

    @Override
    public void performSentimentAnalysis() {

    }

    @Override
    public void voteForNextDayMenu() {

    }
}
