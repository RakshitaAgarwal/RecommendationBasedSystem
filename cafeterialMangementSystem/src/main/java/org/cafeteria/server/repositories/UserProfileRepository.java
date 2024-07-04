package org.cafeteria.server.repositories;

import org.cafeteria.common.model.UserProfile;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IUserProfileRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserProfileRepository implements IUserProfileRepository {
    private final Connection connection;

    public UserProfileRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(UserProfile userProfile) throws SQLException {
        String query = "INSERT INTO userProfile (userId, dietaryPreferenceId, spiceLevelId, favCuisineId, isSweetTooth) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userProfile.getUserId());
            statement.setInt(2, userProfile.getDietaryPreferenceId());
            statement.setInt(3, userProfile.getSpiceLevelId());
            statement.setInt(4, userProfile.getFavCuisineId());
            statement.setBoolean(5, userProfile.isSweetTooth());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean addBatch(List<UserProfile> items) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(UserProfile item) throws SQLException {
        return false;
    }

    @Override
    public boolean update(UserProfile userProfile) throws SQLException {
        String query = "UPDATE UserProfile SET userId = ?, dietaryPreferenceId = ?, spiceLevelId = ?, favCuisineId = ?, isSweetTooth = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userProfile.getUserId());
            statement.setInt(2, userProfile.getDietaryPreferenceId());
            statement.setInt(3, userProfile.getSpiceLevelId());
            statement.setInt(4, userProfile.getFavCuisineId());
            statement.setBoolean(5, userProfile.isSweetTooth());
            statement.setInt(6, userProfile.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<UserProfile> GetAll() throws SQLException {
        return null;
    }

    @Override
    public UserProfile getById(int id) throws SQLException {
        return null;
    }


    @Override
    public UserProfile getByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM userProfile WHERE userId = ?";
        UserProfile userProfile = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int dietaryPreferenceId = resultSet.getInt("dietaryPreferenceId");
                    int favCuisineId = resultSet.getInt("favCuisineId");
                    int spiceLevelId = resultSet.getInt("spiceLevelId");
                    boolean isSweetTooth = resultSet.getBoolean("isSweetTooth");

                    userProfile = new UserProfile();
                    userProfile.setId(id);
                    userProfile.setUserId(userId);
                    userProfile.setDietaryPreferenceId(dietaryPreferenceId);
                    userProfile.setSpiceLevelId(spiceLevelId);
                    userProfile.setFavCuisineId(favCuisineId);
                    userProfile.setSweetTooth(isSweetTooth);
                }
            }
        }
        return userProfile;
    }
}