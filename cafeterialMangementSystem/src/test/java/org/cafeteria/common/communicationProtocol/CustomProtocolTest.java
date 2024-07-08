package org.cafeteria.common.communicationProtocol;

import org.cafeteria.common.customException.CustomExceptions.InvalidRequestException;
import org.cafeteria.common.customException.CustomExceptions.InvalidResponseException;
import org.cafeteria.common.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test(expected = InvalidRequestException.class)
    public void testParseRequest_invalidRequestFormat() throws InvalidRequestException {
        String request = "invalid_request_format";
        CustomProtocol.parseRequest(request);
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

    @Test(expected = InvalidResponseException.class)
    public void testParseResponse_invalidResponseFormat() throws InvalidResponseException {
        String response = "invalid_response_format";
        CustomProtocol.parseResponse(response);
    }
}