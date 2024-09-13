package org.cafeteria.server.repositories;

import org.cafeteria.common.model.RolledOutMenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IRolledOutMenuItemRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

public class RolledOutMenuItemRepository implements IRolledOutMenuItemRepository {
    private final Connection connection;
    private static final String TABLE_ROLLED_OUT_MENU_ITEM = "RolledOutMenuItem";
    private static final String COLUMN_PK_ID = "id";
    private static final String COLUMN_FK_MENU_ITEM_ID = "menuItemId";
    private static final String COLUMN_ROLLED_OUT_DATE = "rolledOutDate";
    public RolledOutMenuItemRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean addBatch(List<RolledOutMenuItem> rolledOutMenuItems) throws SQLException {
        String query = "INSERT INTO " + TABLE_ROLLED_OUT_MENU_ITEM + " (" +
                COLUMN_FK_MENU_ITEM_ID + ", " + COLUMN_ROLLED_OUT_DATE + ") VALUES (?, ?)";
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
    public List<RolledOutMenuItem> getByDate(String rolledOutDate) throws SQLException {
        String query = "SELECT * FROM " + TABLE_ROLLED_OUT_MENU_ITEM + " WHERE DATE(" + COLUMN_ROLLED_OUT_DATE + ") = ?";
        List<RolledOutMenuItem> rolledOutMenuItems;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rolledOutDate);
            ResultSet resultSet = statement.executeQuery();
            rolledOutMenuItems = new ArrayList<>();
            while (resultSet.next()) {
                RolledOutMenuItem rolledOutMenuItem = new RolledOutMenuItem();
                rolledOutMenuItem.setId(resultSet.getInt(COLUMN_PK_ID));
                rolledOutMenuItem.setMenuItemId(resultSet.getInt(COLUMN_FK_MENU_ITEM_ID));
                rolledOutMenuItem.setRolledOutDate(timestampToDate(resultSet.getTimestamp(COLUMN_ROLLED_OUT_DATE)));

                rolledOutMenuItems.add(rolledOutMenuItem);
            }
        }
        return rolledOutMenuItems;
    }
}