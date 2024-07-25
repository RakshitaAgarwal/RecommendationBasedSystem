package org.cafeteria.common.model;

public class ParsedRequest {
    private UserAction userAction;
    private String jsonData;

    public UserAction getUserAction() {
        return userAction;
    }

    public String getJsonData() {
        return jsonData;
    }

    public ParsedRequest(UserAction userAction, String jsonData) {
        this.userAction = userAction;
        this.jsonData = jsonData;
    }
}