package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.MenuItem;
import java.sql.SQLException;
import java.util.List;

public interface IMenuService extends ICrudService<MenuItem> {
    MenuItem getByName(String name) throws SQLException;
    List<MenuItem> getByMealTypeId(int mealTypeId) throws SQLException;
}