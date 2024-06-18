package org.cafeteria.common.model;

public class ParsedResponse {
    private ResponseCode responseCode;
    private String jsonData;

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getJsonData() {
        return jsonData;
    }

    public ParsedResponse(ResponseCode responseCode, String jsonData) {
        this.responseCode = responseCode;
        this.jsonData = jsonData;
    }
}
