package org.cafeteria.server.services.interfaces;

import org.cafeteria.server.model.UserSession;

import java.sql.SQLException;

public interface IUserSessionService {
    UserSession createUserSession(UserSession userSession) throws SQLException;
    UserSession getUserSession(int userId) throws SQLException;
    boolean endUserSession(UserSession userSession) throws SQLException;
}