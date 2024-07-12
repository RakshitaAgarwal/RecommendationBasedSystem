package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.RolledOutMenuItem;

import java.sql.SQLException;
import java.util.List;

public interface IRolledOutMenuItemRepository {
    List<RolledOutMenuItem> getByDate(String recommendationDate) throws SQLException;

    boolean addBatch(List<RolledOutMenuItem> rolledOutMenuItems) throws SQLException;
}