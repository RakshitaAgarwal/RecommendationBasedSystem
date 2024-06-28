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

    public MenuRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(MenuItem item) throws SQLException {
        String query = "INSERT INTO menu (name, price, isAvailable, mealTypeId) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getName());
            statement.setFloat(2, item.getPrice());
            statement.setBoolean(3, item.isAvailable());
            statement.setInt(4, item.getMealTypeId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean addBatch(List<MenuItem> items) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(MenuItem item) throws SQLException {
        String query = "DELETE FROM menu WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    @Override
    public boolean update(MenuItem item) throws SQLException {
        String query = "UPDATE menu SET price = ?, isAvailable = ?, lastTimePrepared = ?, mealTypeId = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setFloat(1, item.getPrice());
            statement.setBoolean(2, item.isAvailable());
            statement.setTimestamp(3, dateToTimestamp(item.getLastTimePrepared()));
            statement.setInt(4, item.getMealTypeId());
            statement.setInt(5, item.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<MenuItem> GetAll() throws SQLException {
        String query = "SELECT * FROM menu";
        List<MenuItem> menuItems = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                boolean isAvailable = resultSet.getBoolean("isAvailable");
                Timestamp lastTimePreparedTimestamp = resultSet.getTimestamp("lastTimePrepared");
                int mealTypeId = resultSet.getInt("mealTypeId");

                MenuItem menuItem = new MenuItem();
                menuItem.setId(id);
                menuItem.setName(name);
                menuItem.setPrice(price);
                menuItem.setAvailable(isAvailable);
                menuItem.setLastTimePrepared(timestampToDate(lastTimePreparedTimestamp));
                menuItem.setMealTypeId(mealTypeId);

                menuItems.add(menuItem);
            }
        }

        return menuItems;
    }

    @Override
    public MenuItem getById(int id) throws SQLException {
        String query = "SELECT * FROM menu WHERE id = ?";
        MenuItem menuItem = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    float price = resultSet.getFloat("price");
                    boolean isAvailable = resultSet.getBoolean("isAvailable");
                    Timestamp lastTimePreparedTimestamp = resultSet.getTimestamp("lastTimePrepared");
                    int mealTypeId = resultSet.getInt("mealTypeId");

                    menuItem = new MenuItem();
                    menuItem.setId(id);
                    menuItem.setName(name);
                    menuItem.setPrice(price);
                    menuItem.setAvailable(isAvailable);
                    menuItem.setLastTimePrepared(timestampToDate(lastTimePreparedTimestamp));
                    menuItem.setMealTypeId(mealTypeId);
                }
            }
        }

        return menuItem;

    }

    @Override
    public MenuItem getByName(String name) throws SQLException {
        String query = "SELECT * FROM menu WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(resultSet.getInt("id"));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setPrice(resultSet.getFloat("price"));
                menuItem.setAvailable(resultSet.getBoolean("isAvailable"));
                menuItem.setMealTypeId(resultSet.getInt("mealTypeId"));
                Timestamp timestamp = resultSet.getTimestamp("lastTimePrepared");
                menuItem.setLastTimePrepared(timestampToDate(timestamp));

                return menuItem;
            }
        }
        return null;
    }

    @Override
    public List<MenuItem> getByMealTypeId(int mealTypeId) throws SQLException {
        String query = "SELECT * FROM menu WHERE mealTypeId = ?";
        List<MenuItem> menuItems = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,mealTypeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                boolean isAvailable = resultSet.getBoolean("isAvailable");
                Timestamp lastTimePreparedTimestamp = resultSet.getTimestamp("lastTimePrepared");

                MenuItem menuItem = new MenuItem();
                menuItem.setId(id);
                menuItem.setName(name);
                menuItem.setPrice(price);
                menuItem.setAvailable(isAvailable);
                menuItem.setLastTimePrepared(timestampToDate(lastTimePreparedTimestamp));
                menuItem.setMealTypeId(mealTypeId);

                menuItems.add(menuItem);
            }
        }

        return menuItems;
    }
}