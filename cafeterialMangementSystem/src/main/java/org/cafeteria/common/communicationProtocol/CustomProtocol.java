package org.cafeteria.common.communicationProtocol;

import org.cafeteria.common.customException.CustomExceptions.InvalidRequestException;
import org.cafeteria.common.customException.CustomExceptions.InvalidResponseException;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ParsedResponse;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.UserAction;

public class CustomProtocol {
    private static final String DELIMITER = "|";
    private static final String PARSE_DELIMITER = "\\|";
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";


    public static String createRequest(UserAction userAction, String serializedJson) {
        return REQUEST + DELIMITER + userAction.toString() + DELIMITER + serializedJson;
    }

    public static String createResponse(ResponseCode responseCode, String serializedJson) {
        return RESPONSE + DELIMITER + responseCode.toString() + DELIMITER + serializedJson;
    }

    public static ParsedRequest parseRequest(String request) throws InvalidRequestException {
        if(request != null) {
            String[] requestArr = request.split(PARSE_DELIMITER, 3);
            if(requestArr.length == 3 && REQUEST.equalsIgnoreCase(requestArr[0])){
                return new ParsedRequest(UserAction.valueOf(requestArr[1]), requestArr[2]);
            } else {
                throw new InvalidRequestException("Invalid Request Message");
            }
        } else {
            throw new InvalidRequestException("Invalid Request Message");
        }
    }

    public static ParsedResponse parseResponse(String response) throws InvalidResponseException {
        if(response != null) {
            String[] responseArr = response.split(PARSE_DELIMITER, 3);
            if(responseArr.length == 3 && RESPONSE.equalsIgnoreCase(responseArr[0])){
                return new ParsedResponse(ResponseCode.fromString(responseArr[1]), responseArr[2]);
            } else {
                throw new InvalidResponseException("Invalid Response Message");
            }
        } else {
            throw new InvalidResponseException("Invalid Response Message");
        }
    }
}