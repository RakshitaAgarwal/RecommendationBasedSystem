package org.cafeteria.server.repositories;

import org.cafeteria.common.model.MenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IMenuRepository;
import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRepository implements IMenuRepository {
    private final Connection connection;
    private static final String TABLE_MENU_ITEM = "MenuItem";
    private static final String COLUMN_PK_ID = "Id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IS_AVAILABLE = "isAvailable";
    private static final String COLUMN_LAST_TIME_PREPARED = "lastTimePrepared";
    private static final String COLUMN_FK_MEAL_TYPE_ID = "mealTypeId";
    private static final String COLUMN_FK_MENU_ITEM_TYPE_ID = "menuItemTypeId";
    private static final String COLUMN_FK_SWEET_CONTENT_LEVEL_ID = "sweetContentLevelId";
    private static final String COLUMN_FK_SPICE_CONTENT_LEVEL_ID = "spiceContentLevelId";
    private static final String COLUMN_FK_CUISINE_TYPE_ID = "cuisineTypeId";

    public MenuRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(MenuItem item) throws SQLException {
        String query = "INSERT INTO " + TABLE_MENU_ITEM + " (" +
                COLUMN_NAME + ", " +
                COLUMN_PRICE + ", " +
                COLUMN_IS_AVAILABLE + ", " +
                COLUMN_FK_MEAL_TYPE_ID + ", " +
                COLUMN_FK_MENU_ITEM_TYPE_ID + ", " +
                COLUMN_FK_SWEET_CONTENT_LEVEL_ID + ", " +
                COLUMN_FK_SPICE_CONTENT_LEVEL_ID + ", " +
                COLUMN_FK_CUISINE_TYPE_ID + ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getName());
            statement.setFloat(2, item.getPrice());
            statement.setBoolean(3, item.isAvailable());
            statement.setInt(4, item.getMealTypeId());
            statement.setInt(5, item.getMenuItemTypeId());
            statement.setInt(6, item.getSweetContentLevelId());
            statement.setInt(7, item.getSpiceContentLevelId());
            statement.setInt(8, item.getCuisineTypeId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean addBatch(List<MenuItem> items) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(MenuItem item) throws SQLException {
        String query = "DELETE FROM " + TABLE_MENU_ITEM + " WHERE " + COLUMN_PK_ID + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    @Override
    public boolean update(MenuItem item) throws SQLException {
        String query = "UPDATE " + TABLE_MENU_ITEM + " SET " +
                COLUMN_PRICE + " = ?, " +
                COLUMN_IS_AVAILABLE + " = ?, " +
                COLUMN_LAST_TIME_PREPARED + " = ?, " +
                COLUMN_FK_MEAL_TYPE_ID + " = ?, " +
                COLUMN_FK_MENU_ITEM_TYPE_ID + " = ?, " +
                COLUMN_FK_SWEET_CONTENT_LEVEL_ID + " = ?, " +
                COLUMN_FK_SPICE_CONTENT_LEVEL_ID + " = ?, " +
                COLUMN_FK_CUISINE_TYPE_ID + " = ? " +
                "WHERE " + COLUMN_PK_ID + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setFloat(1, item.getPrice());
            statement.setBoolean(2, item.isAvailable());
            statement.setTimestamp(3, dateToTimestamp(item.getLastTimePrepared()));
            statement.setInt(4, item.getMealTypeId());
            statement.setInt(5, item.getMenuItemTypeId());
            statement.setInt(6, item.getSweetContentLevelId());
            statement.setInt(7, item.getSpiceContentLevelId());
            statement.setInt(8, item.getCuisineTypeId());
            statement.setInt(9, item.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<MenuItem> getAll() throws SQLException {
        String query = "SELECT * FROM menuItem";
        List<MenuItem> menuItems = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(resultSet.getInt(COLUMN_PK_ID));
                menuItem.setName(resultSet.getString(COLUMN_NAME));
                menuItem.setPrice(resultSet.getFloat(COLUMN_PRICE));
                menuItem.setAvailable(resultSet.getBoolean(COLUMN_IS_AVAILABLE));
                menuItem.setMealTypeId(resultSet.getInt(COLUMN_FK_MEAL_TYPE_ID));
                Timestamp timestamp = resultSet.getTimestamp(COLUMN_LAST_TIME_PREPARED);
                menuItem.setLastTimePrepared(timestampToDate(timestamp));
                menuItem.setMenuItemTypeId(resultSet.getInt(COLUMN_FK_MENU_ITEM_TYPE_ID));
                menuItem.setSweetContentLevelId(resultSet.getInt(COLUMN_FK_SWEET_CONTENT_LEVEL_ID));
                menuItem.setSpiceContentLevelId(resultSet.getInt(COLUMN_FK_SPICE_CONTENT_LEVEL_ID));
                menuItem.setCuisineTypeId(resultSet.getInt(COLUMN_FK_CUISINE_TYPE_ID));

                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }

    @Override
    public MenuItem getById(int id) throws SQLException {
        String query = "SELECT * FROM menuItem WHERE id = ?";
        MenuItem menuItem = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    menuItem = new MenuItem();
                    menuItem.setId(resultSet.getInt(COLUMN_PK_ID));
                    menuItem.setName(resultSet.getString(COLUMN_NAME));
                    menuItem.setPrice(resultSet.getFloat(COLUMN_PRICE));
                    menuItem.setAvailable(resultSet.getBoolean(COLUMN_IS_AVAILABLE));
                    menuItem.setMealTypeId(resultSet.getInt(COLUMN_FK_MEAL_TYPE_ID));
                    Timestamp timestamp = resultSet.getTimestamp(COLUMN_LAST_TIME_PREPARED);
                    menuItem.setLastTimePrepared(timestampToDate(timestamp));
                    menuItem.setMenuItemTypeId(resultSet.getInt(COLUMN_FK_MENU_ITEM_TYPE_ID));
                    menuItem.setSweetContentLevelId(resultSet.getInt(COLUMN_FK_SWEET_CONTENT_LEVEL_ID));
                    menuItem.setSpiceContentLevelId(resultSet.getInt(COLUMN_FK_SPICE_CONTENT_LEVEL_ID));
                    menuItem.setCuisineTypeId(resultSet.getInt(COLUMN_FK_CUISINE_TYPE_ID));
                }
            }
        }
        return menuItem;
    }

    @Override
    public MenuItem getByName(String name) throws SQLException {
        String query = "SELECT * FROM menuItem WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(resultSet.getInt(COLUMN_PK_ID));
                menuItem.setName(resultSet.getString(COLUMN_NAME));
                menuItem.setPrice(resultSet.getFloat(COLUMN_PRICE));
                menuItem.setAvailable(resultSet.getBoolean(COLUMN_IS_AVAILABLE));
                menuItem.setMealTypeId(resultSet.getInt(COLUMN_FK_MEAL_TYPE_ID));
                Timestamp timestamp = resultSet.getTimestamp(COLUMN_LAST_TIME_PREPARED);
                menuItem.setLastTimePrepared(timestampToDate(timestamp));
                menuItem.setMenuItemTypeId(resultSet.getInt(COLUMN_FK_MENU_ITEM_TYPE_ID));
                menuItem.setSweetContentLevelId(resultSet.getInt(COLUMN_FK_SWEET_CONTENT_LEVEL_ID));
                menuItem.setSpiceContentLevelId(resultSet.getInt(COLUMN_FK_SPICE_CONTENT_LEVEL_ID));
                menuItem.setCuisineTypeId(resultSet.getInt(COLUMN_FK_CUISINE_TYPE_ID));

                return menuItem;
            }
        }
        return null;
    }

    @Override
    public List<MenuItem> getByMealTypeId(int mealTypeId) throws SQLException {
        String query = "SELECT * FROM menuItem WHERE mealTypeId = ?";
        List<MenuItem> menuItems = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,mealTypeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(resultSet.getInt(COLUMN_PK_ID));
                menuItem.setName(resultSet.getString(COLUMN_NAME));
                menuItem.setPrice(resultSet.getFloat(COLUMN_PRICE));
                menuItem.setAvailable(resultSet.getBoolean(COLUMN_IS_AVAILABLE));
                menuItem.setMealTypeId(resultSet.getInt(COLUMN_FK_MEAL_TYPE_ID));
                Timestamp timestamp = resultSet.getTimestamp(COLUMN_LAST_TIME_PREPARED);
                menuItem.setLastTimePrepared(timestampToDate(timestamp));
                menuItem.setMenuItemTypeId(resultSet.getInt(COLUMN_FK_MENU_ITEM_TYPE_ID));
                menuItem.setSweetContentLevelId(resultSet.getInt(COLUMN_FK_SWEET_CONTENT_LEVEL_ID));
                menuItem.setSpiceContentLevelId(resultSet.getInt(COLUMN_FK_SPICE_CONTENT_LEVEL_ID));
                menuItem.setCuisineTypeId(resultSet.getInt(COLUMN_FK_CUISINE_TYPE_ID));

                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }
}