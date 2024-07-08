package org.cafeteria.server.network;

import org.cafeteria.server.model.UserActivity;
import org.cafeteria.server.model.UserSession;
import org.cafeteria.server.services.UserActivityService;
import org.cafeteria.server.services.UserSessionService;
import org.cafeteria.server.services.interfaces.IUserActivityService;
import org.cafeteria.server.services.interfaces.IUserSessionService;

import java.sql.SQLException;
import java.util.Date;

public class SessionManager {

    private UserSession userSession;
    private static IUserSessionService _userSessionService;
    private static IUserActivityService _userActivityService;

    public SessionManager() {
        _userSessionService = new UserSessionService();
        _userActivityService = new UserActivityService();
    }

    public boolean createUserSession(int userId) throws SQLException {
        UserSession userSession = new UserSession(userId, new Date(), null);
        this.userSession = _userSessionService.createUserSession(userSession);
        return this.userSession != null;
    }

    public void recordUserActivity(String activity) {
        UserActivity userActivity = new UserActivity(
                userSession.getId(),
                activity,
                new Date()
        );
        try {
            if(_userActivityService.recordUserActivity(userActivity)) {
                System.out.println("User activity recorded successfully.");
            } else {
                System.out.println("Some Error occurred in recording user activity");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void endUserSession() {
        if (userSession != null) {
            userSession.setLogoutDateTime(new Date());
            try {
                _userSessionService.endUserSession(userSession);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}