package org.cafeteria.server.repositories;

import org.cafeteria.common.model.MenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IMenuRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuRepository implements IMenuRepository {
    private Connection connection;

    public MenuRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(MenuItem item) throws SQLException {
        String query = "INSERT INTO menu (name, price, isAvailable) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getName());
            statement.setFloat(2, item.getPrice());
            statement.setBoolean(3, item.isAvailable());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public void delete(MenuItem item) {

    }

    @Override
    public boolean update(MenuItem item) throws SQLException {
        String query = "UPDATE menu SET price = ?, isAvailable = ?, lastTimePrepared = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setFloat(1, item.getPrice());
        statement.setBoolean(2, item.isAvailable());
        statement.setTimestamp(3, new java.sql.Timestamp(item.getLastTimePrepared().getTime()));
        statement.setInt(4, item.getId());
        return statement.executeUpdate() > 0;
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
                Date lastTimePrepared = resultSet.getTimestamp("lastTimePrepared");

                MenuItem menuItem = new MenuItem();
                menuItem.setId(id);
                menuItem.setName(name);
                menuItem.setPrice(price);
                menuItem.setAvailable(isAvailable);
                menuItem.setLastTimePrepared(lastTimePrepared);

                menuItems.add(menuItem);
            }
        }

        return menuItems;
    }

    @Override
    public MenuItem getById(int id) {
        return null;
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
                menuItem.setLastTimePrepared(resultSet.getTimestamp("lastTimePrepared"));
                return menuItem;
            }
        }
        return null;
    }
}