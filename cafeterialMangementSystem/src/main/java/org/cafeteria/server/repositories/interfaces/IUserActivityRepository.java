package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.server.model.UserActivity;

import java.sql.SQLException;

public interface IUserActivityRepository {
    boolean add(UserActivity userActivity) throws SQLException;
}