package org.cafeteria.server.services.interfaces;

import org.cafeteria.server.model.UserActivity;

import java.sql.SQLException;

public interface IUserActivityService {
    boolean recordUserActivity(UserActivity userActivity) throws SQLException;
}
