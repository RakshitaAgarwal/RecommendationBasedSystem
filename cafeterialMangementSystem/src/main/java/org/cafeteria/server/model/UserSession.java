package org.cafeteria.server.model;

import java.util.Date;

public class UserSession {
    int id;
    int userId;
    Date loginDateTime;
    Date logoutDateTime;

    public UserSession(int id, int userId, Date loginDateTime, Date logoutDateTime) {
        this.id = id;
        this.userId = userId;
        this.loginDateTime = loginDateTime;
        this.logoutDateTime = logoutDateTime;
    }

    public UserSession(int userId, Date loginDateTime, Date logoutDateTime) {
        this.userId = userId;
        this.loginDateTime = loginDateTime;
        this.logoutDateTime = logoutDateTime;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Date getLoginDateTime() {
        return loginDateTime;
    }

    public Date getLogoutDateTime() {
        return logoutDateTime;
    }

    public void setLogoutDateTime(Date logoutDateTime) {
        this.logoutDateTime = logoutDateTime;
    }
}