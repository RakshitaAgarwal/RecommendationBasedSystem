package org.cafeteria.server.repositories;

import org.cafeteria.common.model.UserProfile;
import org.cafeteria.server.repositories.interfaces.IUserProfileRepository;

import java.sql.SQLException;
import java.util.List;

public class UserProfileRepository implements IUserProfileRepository {
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