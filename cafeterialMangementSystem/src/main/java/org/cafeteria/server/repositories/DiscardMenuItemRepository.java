package org.cafeteria.server.repositories;

import org.cafeteria.common.model.DiscardMenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IDiscardMenuItemsRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscardMenuItemRepository implements IDiscardMenuItemsRepository {
    private final Connection connection;
    public DiscardMenuItemRepository() {
        this.connection = JdbcConnection.getConnection();
    }
    @Override
    public List<DiscardMenuItem> getAll() throws SQLException {
        String query = "SELECT * FROM DiscardMenuItem";
        List<DiscardMenuItem> discardedMenuItems = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuItemId = resultSet.getInt("menuItemId");
                float rating = resultSet.getFloat("avgRating");

                DiscardMenuItem item = new DiscardMenuItem(id, menuItemId, rating);
                discardedMenuItems.add(item);
            }
        }
        return discardedMenuItems;
    }
}
