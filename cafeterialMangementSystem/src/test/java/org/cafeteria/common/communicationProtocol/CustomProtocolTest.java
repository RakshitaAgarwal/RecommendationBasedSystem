package org.cafeteria.common.communicationProtocol;

import org.cafeteria.common.customException.CustomExceptions.InvalidRequestException;
import org.cafeteria.common.customException.CustomExceptions.InvalidResponseException;
import org.cafeteria.common.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomProtocolTest {

    @Test
    public void testCreateRequest() {
        User user = new User(1, "testUser", 2, "password");
        String json = CustomProtocol.serializeData(user);
        String request = CustomProtocol.createRequest(UserAction.LOGIN, json);
        assertEquals("request|LOGIN|" + json, request);
    }

    @Test
    public void testCreateResponse() {
        User user = new User(1, "testUser", 2, "password");
        String json = CustomProtocol.serializeData(user);
        String response = CustomProtocol.createResponse(ResponseCode.OK, json);
        assertEquals("response|200 OK|" + json, response);
    }

    @Test
    public void testParseRequest_validRequest() throws InvalidRequestException {
        User user = new User(1, "testUser", 2, "password");
        String json = CustomProtocol.serializeData(user);
        String request = "request|LOGIN|" + json;
        ParsedRequest actualParsedRequest = CustomProtocol.parseRequest(request);
        ParsedRequest expectedParsedRequest = new ParsedRequest(UserAction.LOGIN, json);
        assertEquals(expectedParsedRequest.getUserAction(), actualParsedRequest.getUserAction());
        assertEquals(expectedParsedRequest.getJsonData(), actualParsedRequest.getJsonData());
    }

    @Test
    public void testParseRequest_invalidRequestFormat() {
        String request = "invalid_request_format";
        Executable executable = () -> CustomProtocol.parseRequest(request);
        assertThrows(InvalidRequestException.class, executable);
    }

    @Test
    public void testParseResponse_validResponse() throws InvalidResponseException {
        User user = new User(1, "testUser", 2, "password");
        String json = CustomProtocol.serializeData(user);
        String response = "response|200 OK|" + json;
        ParsedResponse actualParsedResponse = CustomProtocol.parseResponse(response);
        ParsedResponse expectedParsedResponse = new ParsedResponse(ResponseCode.OK, json);
        assertEquals(expectedParsedResponse.getResponseCode(), actualParsedResponse.getResponseCode());
        assertEquals(expectedParsedResponse.getJsonData(), actualParsedResponse.getJsonData());
    }

    @Test
    public void testParseResponse_invalidResponseFormat() {
        String response = "invalid_response_format";
        Executable executable = () -> CustomProtocol.parseResponse(response);
        assertThrows(InvalidResponseException.class, executable);
    }
}