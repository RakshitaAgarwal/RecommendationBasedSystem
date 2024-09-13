package org.cafeteria.server.services;

import org.cafeteria.server.model.UserActivity;
import org.cafeteria.server.repositories.interfaces.IUserActivityRepository;
import org.cafeteria.server.repositories.interfaces.UserActivityRepository;
import org.cafeteria.server.services.interfaces.IUserActivityService;

import java.sql.SQLException;

public class UserActivityService implements IUserActivityService {

    private static IUserActivityRepository _userActivityRepository;

    public UserActivityService() {
        _userActivityRepository = new UserActivityRepository();
    }
    @Override
    public boolean recordUserActivity(UserActivity userActivity) throws SQLException {
        return _userActivityRepository.add(userActivity);
    }
}