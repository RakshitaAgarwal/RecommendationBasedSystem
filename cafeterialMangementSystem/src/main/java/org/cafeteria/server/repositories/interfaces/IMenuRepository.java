package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.MenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IMenuRepository extends ICrudRepository<MenuItem> {
    public MenuItem getByName(String name) throws SQLException;

    public List<MenuItem> getByMealTypeId(int mealTypeId) throws SQLException;
}
