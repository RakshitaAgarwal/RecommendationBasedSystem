package org.cafeteria.common.model;

public class ParsedRequest {
    private UserAction userAction;
    private String jsonData;

    public UserAction getUserAction() {
        return userAction;
    }

    public void setUserAction(UserAction userAction) {
        this.userAction = userAction;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public ParsedRequest(UserAction userAction, String jsonData) {
        this.userAction = userAction;
        this.jsonData = jsonData;
    }
}