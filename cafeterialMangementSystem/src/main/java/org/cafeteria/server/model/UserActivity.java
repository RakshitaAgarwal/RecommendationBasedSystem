package org.cafeteria.server.model;

import java.util.Date;

public class UserActivity {
    int id;
    int sessionId;
    String activity;
    Date datetime;

    public UserActivity() {
    }

    public UserActivity(int sessionId, String activity, Date datetime) {
        this.sessionId = sessionId;
        this.activity = activity;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
