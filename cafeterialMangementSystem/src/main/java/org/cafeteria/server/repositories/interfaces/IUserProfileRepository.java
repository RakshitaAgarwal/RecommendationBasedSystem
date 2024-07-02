package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.UserProfile;

import java.sql.SQLException;

public interface IUserProfileRepository extends ICrudRepository<UserProfile> {
    public UserProfile getByUserId(int userId) throws SQLException;
}