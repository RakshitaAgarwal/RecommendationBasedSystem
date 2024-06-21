package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.services.DailyMenuService;
import org.cafeteria.server.services.interfaces.IDailyMenuService;

import java.sql.SQLException;

public class DailyMenuController {
    private static IDailyMenuService _dailyMenuService;
    public DailyMenuController() {
        _dailyMenuService = new DailyMenuService();
    }
    public String updateDailyFoodMenu(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}
