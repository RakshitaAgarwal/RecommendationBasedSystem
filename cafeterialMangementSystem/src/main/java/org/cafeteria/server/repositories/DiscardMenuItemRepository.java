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
    private static final String TABLE_DISCARD_MENU_ITEM = "DiscardMenuItem";
    public static final String COLUMN_PK_ID = "Id";
    private static final String COLUMN_FK_MENU_ITEM_ID = "menuItemId";
    private static final String COLUMN_AVG_RATING = "avgRating";

    public DiscardMenuItemRepository() {
        this.connection = JdbcConnection.getConnection();
    }
    @Override
    public List<DiscardMenuItem> getAll() throws SQLException {
        String query = "SELECT * FROM " + TABLE_DISCARD_MENU_ITEM;
        List<DiscardMenuItem> discardedMenuItems = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt(COLUMN_PK_ID);
                int menuItemId = resultSet.getInt(COLUMN_FK_MENU_ITEM_ID);
                float rating = resultSet.getFloat(COLUMN_AVG_RATING);

                DiscardMenuItem item = new DiscardMenuItem(id, menuItemId, rating);
                discardedMenuItems.add(item);
            }
        }
        return discardedMenuItems;
    }
}