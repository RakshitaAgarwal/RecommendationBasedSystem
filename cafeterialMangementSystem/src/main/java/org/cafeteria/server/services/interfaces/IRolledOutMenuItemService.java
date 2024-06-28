package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.RolledOutMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IRolledOutMenuItemService extends IValidationService<RolledOutMenuItem>, ICrudService<RolledOutMenuItem> {
    public boolean rollOutNextDayMenuOptions(List<Integer> rolledOutMenuItemIds) throws SQLException, CustomExceptions.DuplicateEntryFoundException;

    public List<RolledOutMenuItem> getNextDayMenuOptions() throws SQLException;
}
