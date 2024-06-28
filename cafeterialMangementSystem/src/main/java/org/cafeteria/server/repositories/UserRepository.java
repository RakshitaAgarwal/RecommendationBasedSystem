package org.cafeteria.server.repositories;

import org.cafeteria.common.model.User;
import org.cafeteria.common.model.UserRoleEnum;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private Connection connection;

    public UserRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(User item) {
        return false;
    }

    @Override
    public boolean addBatch(List<User> items) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(User item) {

        return false;
    }

    @Override
    public boolean update(User item) {

        return false;
    }

    @Override
    public List<User> GetAll() throws SQLException {
        String query = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int userRoleId = resultSet.getInt("userRoleId");
                String password = resultSet.getString("password");

                User user = new User();
                user.setId(userId);
                user.setName(name);
                user.setUserRoleId(userRoleId);
                user.setPassword(password);
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public List<User> getByUserRoleId(int userRoleId) throws SQLException {
        String query = "SELECT * FROM user where userRoleId=?";
        List<User> employees = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userRoleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");

                User user = new User();
                user.setId(userId);
                user.setName(name);
                user.setUserRoleId(userRoleId);
                user.setPassword(password);
                employees.add(user);
            }
        }
        return employees;
    }

    @Override
    public User getById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int userRoleId = resultSet.getInt("userRoleId");
                    String password = resultSet.getString("password");

                    User user = new User();
                    user.setId(userId);
                    user.setName(name);
                    user.setUserRoleId(userRoleId);
                    user.setPassword(password);

                    return user;
                }
            }
        }
        return null;
    }
}