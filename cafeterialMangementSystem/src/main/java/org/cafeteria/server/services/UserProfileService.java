package org.cafeteria.server.services;

import org.cafeteria.common.model.UserProfile;
import org.cafeteria.server.repositories.UserProfileRepository;
import org.cafeteria.server.repositories.interfaces.IUserProfileRepository;
import org.cafeteria.server.services.interfaces.IUserProfileService;

import java.sql.SQLException;
import java.util.List;

public class UserProfileService implements IUserProfileService {
    private static IUserProfileRepository _userProfileRepository;

    public UserProfileService() {
        _userProfileRepository = new UserProfileRepository();
    }
    @Override
    public boolean add(UserProfile userProfile) throws SQLException {
        return _userProfileRepository.add(userProfile);
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
        return _userProfileRepository.update(item);
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
        return _userProfileRepository.getByUserId(userId);
    }
}