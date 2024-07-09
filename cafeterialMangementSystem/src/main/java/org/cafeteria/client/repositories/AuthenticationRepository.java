package org.cafeteria.client.repositories;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions.BadResponseException;
import org.cafeteria.common.customException.CustomExceptions.InvalidResponseException;
import org.cafeteria.common.customException.CustomExceptions.LoginFailedException;
import org.cafeteria.common.model.ParsedResponse;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.User;
import org.cafeteria.common.model.UserAction;

import java.io.IOException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class AuthenticationRepository {
    private static ServerConnection connection;

    public AuthenticationRepository(ServerConnection serverConnection) {
        connection = serverConnection;
    }

    public User login(User user) throws LoginFailedException, IOException {
        String request = createRequest(UserAction.LOGIN, serializeData(user));
        String response = connection.sendData(request);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                return deserializeData(parsedResponse.getJsonData(), User.class);
            } else throw new LoginFailedException("Login Unsuccessful");
        } catch (InvalidResponseException e) {
            throw new IOException("Invalid Response Received from Server. Server might got disconnected");
        }
    }

    public String logout(User user) throws IOException, BadResponseException {
        String request = createRequest(UserAction.LOGOUT, serializeData(user));
        String response = connection.sendData(request);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if(responseCode == ResponseCode.OK) return deserializeData(parsedResponse.getJsonData(), String.class);
            else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
        } catch (InvalidResponseException e) {
            throw new IOException("Invalid Response Received from Server. Server might got disconnected");
        }
    }
}