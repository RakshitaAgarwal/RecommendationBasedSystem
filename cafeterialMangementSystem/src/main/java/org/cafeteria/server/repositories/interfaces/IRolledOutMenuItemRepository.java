package org.cafeteria.server.repositories.interfaces;


import org.cafeteria.common.model.RolledOutMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IRolledOutMenuItemRepository extends ICrudRepository<RolledOutMenuItem> {
    public List<RolledOutMenuItem> getByDate(String recommendationDate) throws SQLException;
}
