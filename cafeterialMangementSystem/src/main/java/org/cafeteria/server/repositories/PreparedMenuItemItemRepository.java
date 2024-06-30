package org.cafeteria.server.repositories;

import org.cafeteria.common.model.PreparedMenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IPreparedMenuItemRepository;
import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreparedMenuItemItemRepository implements IPreparedMenuItemRepository {
    private final Connection connection;

    public PreparedMenuItemItemRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean addBatch(List<PreparedMenuItem> preparedMenuItems) throws SQLException {
        String query = "INSERT INTO PreparedMenuItem (menuItemId, dateTime) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (PreparedMenuItem preparedMenuItem : preparedMenuItems) {
                statement.setInt(1, preparedMenuItem.getMenuItemId());
                statement.setTimestamp(2, dateToTimestamp(preparedMenuItem.getDateTime()));
                statement.addBatch();
            }
            int[] results = statement.executeBatch();
            return results.length == preparedMenuItems.size();
        }
    }

    public List<PreparedMenuItem> getByDate(String rolledOutDate) throws SQLException {
        String query = "SELECT * FROM PreparedMenuItem WHERE DATE(dateTime) = ?";
        List<PreparedMenuItem> preparedMenuItems;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rolledOutDate);
            ResultSet resultSet = statement.executeQuery();
            preparedMenuItems = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuItemId = resultSet.getInt("menuItemId");
                Date date = timestampToDate(resultSet.getTimestamp("dateTime"));

                PreparedMenuItem rolledOutMenuItem = new PreparedMenuItem();
                rolledOutMenuItem.setId(id);
                rolledOutMenuItem.setMenuItemId(menuItemId);
                rolledOutMenuItem.setDateTime(date);

                preparedMenuItems.add(rolledOutMenuItem);
            }
        }
        return preparedMenuItems;
    }

    @Override
    public boolean add(PreparedMenuItem item) {
        return false;
    }

    @Override
    public boolean delete(PreparedMenuItem item) {
        return false;
    }

    @Override
    public boolean update(PreparedMenuItem item) {
        return false;
    }

    @Override
    public List<PreparedMenuItem> GetAll() {
        return null;
    }

    @Override
    public PreparedMenuItem getById(int id) {
        return null;
    }
}