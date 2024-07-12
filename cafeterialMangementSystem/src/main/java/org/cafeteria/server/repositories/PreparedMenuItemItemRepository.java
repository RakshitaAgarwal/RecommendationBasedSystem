package org.cafeteria.server.repositories;

import org.cafeteria.common.model.PreparedMenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IPreparedMenuItemRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

public class PreparedMenuItemItemRepository implements IPreparedMenuItemRepository {
    private final Connection connection;
    private static final String TABLE_PREPARED_MENU_ITEM = "PreparedMenuItem";
    private static final String COLUMN_PK_ID = "id";
    private static final String COLUMN_FK_MENU_ITEM_ID = "menuItemId";
    private static final String COLUMN_DATE_TIME = "dateTime";

    public PreparedMenuItemItemRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean addBatch(List<PreparedMenuItem> preparedMenuItems) throws SQLException {
        String query = "INSERT INTO " + TABLE_PREPARED_MENU_ITEM + " (" + COLUMN_FK_MENU_ITEM_ID + ", " + COLUMN_DATE_TIME + ") VALUES (?, ?)";

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
        String query = "SELECT * FROM " + TABLE_PREPARED_MENU_ITEM + " WHERE DATE(" + COLUMN_DATE_TIME + ") = ?";
        List<PreparedMenuItem> preparedMenuItems;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rolledOutDate);
            ResultSet resultSet = statement.executeQuery();
            preparedMenuItems = new ArrayList<>();
            while (resultSet.next()) {
                PreparedMenuItem rolledOutMenuItem = new PreparedMenuItem();
                rolledOutMenuItem.setId(resultSet.getInt(COLUMN_PK_ID));
                rolledOutMenuItem.setMenuItemId(resultSet.getInt(COLUMN_FK_MENU_ITEM_ID));
                rolledOutMenuItem.setDateTime(timestampToDate(resultSet.getTimestamp(COLUMN_DATE_TIME)));

                preparedMenuItems.add(rolledOutMenuItem);
            }
        }
        return preparedMenuItems;
    }
}