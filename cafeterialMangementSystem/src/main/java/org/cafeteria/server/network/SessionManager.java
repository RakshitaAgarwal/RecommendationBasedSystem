package org.cafeteria.server.network;

import org.cafeteria.server.model.UserSession;
import org.cafeteria.server.services.UserSessionService;
import org.cafeteria.server.services.interfaces.IUserSessionService;

import java.sql.SQLException;
import java.util.Date;

public class SessionManager {

    private UserSession userSession;
    private static IUserSessionService _userSessionService;

    public SessionManager() {
        _userSessionService = new UserSessionService();
    }

    public boolean createUserSession(int userId) throws SQLException {
        UserSession userSession = new UserSession(userId, new Date(), null);
        this.userSession = _userSessionService.createUserSession(userSession);
        return this.userSession != null;
    }

    public void updateActivity(String sessionId, String activity) {
    }

    public void endUserSession() {
        if (userSession != null) {
            userSession.setLogoutDateTime(new Date());
            try {
                _userSessionService.endUserSession(userSession);
            } catch (SQLException e) {
                System.out.println(e.getMessage());;
            }
        }
    }
}