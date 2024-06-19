package org.cafeteria.server.handlers;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.services.DailyRecommendationService;
import org.cafeteria.server.services.interfaces.IDailyRecommendationService;

import java.sql.SQLException;

public class DailyRecommendationHandler {
    private static IDailyRecommendationService _dailyRecommendationService;
    public DailyRecommendationHandler() {
        _dailyRecommendationService = new DailyRecommendationService();
    }
    public String getDailyRecommendation(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }

    public String voteForNextDayMenu(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}