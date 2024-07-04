package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.User;

import java.sql.SQLException;

public interface IUserService {
    User loginUser(User user) throws SQLException;
}