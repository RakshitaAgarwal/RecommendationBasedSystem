package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserRepository extends ICrudRepository<User> {
    List<User> getByUserRoleId(int userRoleId) throws SQLException;
}