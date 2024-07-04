package org.cafeteria.server.repositories;

import org.cafeteria.common.model.RolledOutMenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IRolledOutMenuItemRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

public class RolledOutMenuItemRepository implements IRolledOutMenuItemRepository {
    private final Connection connection;

    public RolledOutMenuItemRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(RolledOutMenuItem rolledOutMenuItem) throws SQLException {
        String query = "INSERT INTO RolledOutMenuItem (menuItemId, rolledOutDate) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, rolledOutMenuItem.getMenuItemId());
            statement.setTimestamp(2, dateToTimestamp(rolledOutMenuItem.getRolledOutDate()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean addBatch(List<RolledOutMenuItem> rolledOutMenuItems) throws SQLException {
        String query = "INSERT INTO RolledOutMenuItem (menuItemId, rolledOutDate) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (RolledOutMenuItem rolledOutItem : rolledOutMenuItems) {
                statement.setInt(1, rolledOutItem.getMenuItemId());
                statement.setTimestamp(2, dateToTimestamp(rolledOutItem.getRolledOutDate()));
                statement.addBatch();
            }
            int[] results = statement.executeBatch();
            return results.length == rolledOutMenuItems.size();
        }
    }

    @Override
    public boolean delete(RolledOutMenuItem item) {
        return false;
    }

    @Override
    public boolean update(RolledOutMenuItem item) {
        return false;
    }

    @Override
    public List<RolledOutMenuItem> GetAll() {
        return null;
    }

    @Override
    public RolledOutMenuItem getById(int id) {
        return null;
    }

    @Override
    public List<RolledOutMenuItem> getByDate(String rolledOutDate) throws SQLException {
        String query = "SELECT * FROM RolledOutMenuItem WHERE DATE(rolledOutDate) = ?";
        List<RolledOutMenuItem> rolledOutMenuItems;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rolledOutDate);
            ResultSet resultSet = statement.executeQuery();
            rolledOutMenuItems = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuItemId = resultSet.getInt("menuItemId");
                Date date = timestampToDate(resultSet.getTimestamp("rolledOutDate"));

                RolledOutMenuItem rolledOutMenuItem = new RolledOutMenuItem();
                rolledOutMenuItem.setId(id);
                rolledOutMenuItem.setMenuItemId(menuItemId);
                rolledOutMenuItem.setRolledOutDate(date);

                rolledOutMenuItems.add(rolledOutMenuItem);
            }
        }
        return rolledOutMenuItems;
    }
}