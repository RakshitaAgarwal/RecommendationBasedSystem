package org.cafeteria.common.model;

public enum ResponseCode {
    OK("200 OK"),
    BAD_REQUEST("400 Bad Request"),
    UNAUTHORIZED("401 Unauthorized"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    EMPTY_RESPONSE("405 Empty Response"),
    INTERNAL_SERVER_ERROR("500 Internal Server Error");

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static ResponseCode fromString(String code) {
        for (ResponseCode rc : ResponseCode.values()) {
            if (rc.code.equalsIgnoreCase(code)) {
                return rc;
            }
        }
        throw new IllegalArgumentException("Unknown response code: " + code);
    }
}