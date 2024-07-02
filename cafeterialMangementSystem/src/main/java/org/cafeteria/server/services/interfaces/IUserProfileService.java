package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.UserProfile;
import org.cafeteria.server.repositories.interfaces.ICrudRepository;

import java.sql.SQLException;

public interface IUserProfileService extends ICrudRepository<UserProfile> {
    UserProfile getByUserId(int userId) throws SQLException;
}
