package org.cafeteria.server.repositories.interfaces;


import org.cafeteria.common.model.DailyRecommendation;

import java.sql.SQLException;
import java.util.List;

public interface IDailyRecommendationRepository extends ICrudRepository<DailyRecommendation> {
    public List<DailyRecommendation> getByDate(String recommendationDate) throws SQLException;
}
