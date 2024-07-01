package org.cafeteria.server.services;

import org.cafeteria.common.model.UserProfile;
import org.cafeteria.server.services.interfaces.IUserProfileService;

import java.sql.SQLException;
import java.util.List;

public class UserProfileService implements IUserProfileService {
    @Override
    public boolean add(UserProfile item) throws SQLException {
        return false;
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
    public boolean update(UserProfile item) throws SQLException {
        return false;
    }

    @Override
    public List<UserProfile> GetAll() throws SQLException {
        return null;
    }

    @Override
    public UserProfile getById(int id) throws SQLException {
        return null;
    }
}
