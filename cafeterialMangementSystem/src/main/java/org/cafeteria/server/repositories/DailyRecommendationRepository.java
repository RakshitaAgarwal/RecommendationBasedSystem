package org.cafeteria.server.repositories;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.server.repositories.interfaces.IDailyRecommendationRepository;

import java.util.List;

public class DailyRecommendationRepository implements IDailyRecommendationRepository {
    @Override
    public boolean add(DailyRecommendation item) {

        return false;
    }

    @Override
    public boolean delete(DailyRecommendation item) {

        return false;
    }

    @Override
    public boolean update(DailyRecommendation item) {

        return false;
    }

    @Override
    public List<DailyRecommendation> GetAll() {
        return null;
    }

    @Override
    public DailyRecommendation getById(int id) {
        return null;
    }
}