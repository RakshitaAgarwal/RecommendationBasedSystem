package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.DiscardMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IDiscardMenuItemService {
    public List<DiscardMenuItem> getAll() throws SQLException;
}
