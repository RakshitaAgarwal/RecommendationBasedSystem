package org.cafeteria.server.repositories;

import org.cafeteria.common.model.User;
import org.cafeteria.common.model.UserRole;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements IUserRepository {
    private Connection connection;

    public UserRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public void add(User item) {
    }

    @Override
    public void delete(User item) {

    }

    @Override
    public void update(User item) {

    }

    @Override
    public User GetAll() {
        return null;
    }

    @Override
    public User getById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE userid = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int userId = resultSet.getInt("userId");
            String name = resultSet.getString("name");
            UserRole userRole = UserRole.valueOf(resultSet.getString("userRole"));
            String password = resultSet.getString("password");
            User user = new User();
            user.setUserId(userId);
            user.setName(name);
            user.setUserRole(userRole);
            user.setPassword(password);
            return user;
        }
        resultSet.close();
        statement.close();
        return null;
    }
}