package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.server.services.PreparedMenuService;
import org.cafeteria.server.services.interfaces.IPreparedMenuService;

import java.sql.SQLException;

public class PreparedMenuController {
    private static IPreparedMenuService _dailyMenuService;
    public PreparedMenuController() {
        _dailyMenuService = new PreparedMenuService();
    }
    public String updateDailyFoodMenu(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}
