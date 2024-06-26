package org.cafeteria.server.services;

import org.cafeteria.common.model.User;
import org.cafeteria.server.repositories.UserRepository;
import org.cafeteria.server.repositories.interfaces.IUserRepository;
import org.cafeteria.server.services.interfaces.IUserService;

import java.sql.SQLException;

public class UserService implements IUserService {
    private static IUserRepository _userRepository;

    public UserService() {
        _userRepository = new UserRepository();
    }

    @Override
    public User loginUser(User user) throws SQLException {
        return _userRepository.getById(user.getUserId());
    }

    @Override
    public boolean createUser(User user) {
        return true;
    }

    @Override
    public boolean validate(User item) {
        return false;
    }
}
