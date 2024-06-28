package org.cafeteria.server.repositories;

import org.cafeteria.common.model.PreparedMenu;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IPreparedMenuItemRepository;
import static org.cafeteria.common.util.Utils.dateToTimestamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PreparedMenuItemItemRepository implements IPreparedMenuItemRepository {
    private final Connection connection;

    public PreparedMenuItemItemRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean addBatch(List<PreparedMenu> preparedMenuItems) throws SQLException {
        String sql = "INSERT INTO PreparedMenuItem (menuItemId, dateTime) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (PreparedMenu preparedMenuItem : preparedMenuItems) {
                statement.setInt(1, preparedMenuItem.getMenuItemId());
                statement.setTimestamp(2, dateToTimestamp(preparedMenuItem.getDate()));
                statement.addBatch();
            }
            int[] results = statement.executeBatch();
            return results.length == preparedMenuItems.size();
        }
    }

    @Override
    public boolean add(PreparedMenu item) {
        return false;
    }

    @Override
    public boolean delete(PreparedMenu item) {
        return false;
    }

    @Override
    public boolean update(PreparedMenu item) {
        return false;
    }

    @Override
    public List<PreparedMenu> GetAll() {
        return null;
    }

    @Override
    public PreparedMenu getById(int id) {
        return null;
    }
}