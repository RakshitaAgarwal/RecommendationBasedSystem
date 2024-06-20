package org.cafeteria.client.services;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions.InvalidResponseException;
import org.cafeteria.common.customException.CustomExceptions.LoginFailedException;
import org.cafeteria.common.model.ParsedResponse;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.User;
import org.cafeteria.common.model.UserAction;

import java.io.IOException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class AuthenticationService {
    private static ServerConnection connection;

    public AuthenticationService(ServerConnection serverConnection) {
        connection = serverConnection;
    }

    public User login(User user) throws IOException, LoginFailedException {
        String request = createRequest(UserAction.LOGIN, serializeData(user));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                return deserializeData(parsedResponse.getJsonData(), User.class);
            } else throw new LoginFailedException("Login Unsuccessful");
        } catch (InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
            throw new IOException("Server might got disconnected");
        }
    }
}