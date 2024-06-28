package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.PreparedMenu;

import java.sql.SQLException;
import java.util.List;

public interface IPreparedMenuItemService extends IValidationService<PreparedMenu>, ICrudService<PreparedMenu> {
    public void updateDailyMenu();
    public void weeklyMenuCleanUp();
    public boolean addAll(List<Integer> preparedMenuItemIds) throws SQLException;
}
