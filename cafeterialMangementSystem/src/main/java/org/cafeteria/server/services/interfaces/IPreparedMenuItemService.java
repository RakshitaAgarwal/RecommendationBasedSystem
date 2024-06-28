package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.PreparedMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IPreparedMenuItemService extends IValidationService<PreparedMenuItem>, ICrudService<PreparedMenuItem> {
    public void updateDailyMenu();
    public void weeklyMenuCleanUp();
    public boolean addPreparedMenuItems(List<Integer> preparedMenuItemIds) throws SQLException, CustomExceptions.DuplicateEntryFoundException;
}
