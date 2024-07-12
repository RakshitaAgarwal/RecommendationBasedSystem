package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.PreparedMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IPreparedMenuItemRepository {
    List<PreparedMenuItem> getByDate(String rolledOutDate) throws SQLException;

    boolean addBatch(List<PreparedMenuItem> preparedMenuItems) throws SQLException;
}