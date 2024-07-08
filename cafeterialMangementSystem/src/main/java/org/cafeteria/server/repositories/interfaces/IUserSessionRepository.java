package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.server.model.UserSession;

import java.sql.SQLException;

public interface IUserSessionRepository {
    UserSession addUserSession(UserSession userSession) throws SQLException;
    boolean updateUserSession(UserSession userSession) throws SQLException;
}