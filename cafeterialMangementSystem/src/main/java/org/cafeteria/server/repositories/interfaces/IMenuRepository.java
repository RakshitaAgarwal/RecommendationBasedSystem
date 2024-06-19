package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.MenuItem;

import java.sql.SQLException;

public interface IMenuRepository extends ICrudRepository<MenuItem> {
    public MenuItem getByName(String name) throws SQLException;
}
