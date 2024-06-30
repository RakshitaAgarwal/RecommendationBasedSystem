package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.DiscardMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IDiscardMenuItemsRepository {
    public List<DiscardMenuItem> getAll() throws SQLException;
}
