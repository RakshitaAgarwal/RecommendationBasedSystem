package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.PreparedMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IPreparedMenuItemRepository extends ICrudRepository<PreparedMenuItem> {
    List<PreparedMenuItem> getByDate(String rolledOutDate) throws SQLException;
}
