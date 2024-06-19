package org.cafeteria.server.services.interfaces;
import org.cafeteria.common.model.MenuItem;

import java.sql.SQLException;

public interface IMenuService extends IValidationService<MenuItem>, ICrudService<MenuItem> {
    public MenuItem getByName(String name) throws SQLException;
}
