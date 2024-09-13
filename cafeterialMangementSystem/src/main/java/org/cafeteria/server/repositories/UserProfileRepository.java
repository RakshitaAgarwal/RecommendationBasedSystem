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
    private static final String TABLE_USER_PROFILE = "userProfile";
    private static final String COLUMN_PK_ID = "id";
    private static final String COLUMN_FK_USER_ID = "userId";
    private static final String COLUMN_FK_DIETARY_PREFERENCE_ID = "dietaryPreferenceId";
    private static final String COLUMN_FK_SPICE_LEVEL_ID = "spiceLevelId";
    private static final String COLUMN_FK_FAV_CUISINE_ID = "favCuisineId";
    private static final String COLUMN_IS_SWEET_TOOTH = "isSweetTooth";

    public UserProfileRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(UserProfile userProfile) throws SQLException {
        String query = "INSERT INTO " + TABLE_USER_PROFILE + " (" +
                COLUMN_FK_USER_ID + ", " +
                COLUMN_FK_DIETARY_PREFERENCE_ID + ", " +
                COLUMN_FK_SPICE_LEVEL_ID + ", " +
                COLUMN_FK_FAV_CUISINE_ID + ", " +
                COLUMN_IS_SWEET_TOOTH + ") VALUES (?, ?, ?, ?, ?);";
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
    public boolean update(UserProfile userProfile) throws SQLException {
        String query = "UPDATE " + TABLE_USER_PROFILE + " SET " +
                COLUMN_FK_USER_ID + " = ?, " +
                COLUMN_FK_DIETARY_PREFERENCE_ID + " = ?, " +
                COLUMN_FK_SPICE_LEVEL_ID + " = ?, " +
                COLUMN_FK_FAV_CUISINE_ID + " = ?, " +
                COLUMN_IS_SWEET_TOOTH + " = ? WHERE " + COLUMN_PK_ID + " = ?";
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
    public UserProfile getByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM " + TABLE_USER_PROFILE + " WHERE " + COLUMN_FK_USER_ID + " = ?";
        UserProfile userProfile = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userProfile = new UserProfile();
                    userProfile.setId(resultSet.getInt(COLUMN_PK_ID));
                    userProfile.setUserId(userId);
                    userProfile.setDietaryPreferenceId(resultSet.getInt(COLUMN_FK_DIETARY_PREFERENCE_ID));
                    userProfile.setSpiceLevelId(resultSet.getInt(COLUMN_FK_SPICE_LEVEL_ID));
                    userProfile.setFavCuisineId(resultSet.getInt(COLUMN_FK_FAV_CUISINE_ID));
                    userProfile.setSweetTooth(resultSet.getBoolean(COLUMN_IS_SWEET_TOOTH));
                }
            }
        }
        return userProfile;
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
    public List<UserProfile> getAll() throws SQLException {
        return null;
    }

    @Override
    public UserProfile getById(int id) throws SQLException {
        return null;
    }
}