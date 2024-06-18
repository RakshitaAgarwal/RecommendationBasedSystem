package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.User;

import java.sql.SQLException;

public interface IUserService extends IValidationService<User> {
    public User loginUser(User user) throws SQLException;
    public boolean createUser(User user);
}
