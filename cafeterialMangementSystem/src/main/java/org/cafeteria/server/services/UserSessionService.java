package org.cafeteria.server.services;

import org.cafeteria.server.model.UserSession;
import org.cafeteria.server.repositories.UserSessionRepository;
import org.cafeteria.server.repositories.interfaces.IUserSessionRepository;
import org.cafeteria.server.services.interfaces.IUserSessionService;

import java.sql.SQLException;

public class UserSessionService implements IUserSessionService {
    private static IUserSessionRepository _userSessionRepository;

    public UserSessionService() {
        _userSessionRepository = new UserSessionRepository();
    }
    @Override
    public UserSession createUserSession(UserSession userSession) throws SQLException {
        return _userSessionRepository.addUserSession(userSession);
    }

    @Override
    public UserSession getUserSession(int userId) throws SQLException {
        return null;
    }

    @Override
    public boolean endUserSession(UserSession userSession) throws SQLException {
        return _userSessionRepository.updateUserSession(userSession);
    }
}