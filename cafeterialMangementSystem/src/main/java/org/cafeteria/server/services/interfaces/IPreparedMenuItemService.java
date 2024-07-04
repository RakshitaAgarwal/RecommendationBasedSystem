package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.PreparedMenuItem;
import java.sql.SQLException;
import java.util.List;

public interface IPreparedMenuItemService extends ICrudService<PreparedMenuItem> {
    boolean addPreparedMenuItems(List<Integer> preparedMenuItemIds) throws SQLException, CustomExceptions.DuplicateEntryFoundException;
}