package org.cafeteria.client.repositories;

import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.deserializeData;

import java.io.IOException;
import java.util.List;

public class EmployeeRepository extends UserRepository {
    public EmployeeRepository(ServerConnection connection) {
        super(connection);
    }

    public MenuItemRecommendation getRecommendationScoreForMenuItem(int menuItemId) throws InvalidResponseException, IOException, BadResponseException {
        String request = createRequest(UserAction.GET_MENU_ITEM_RECOMMENDATION_SCORE, serializeData(menuItemId));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeData(parsedResponse.getJsonData(), MenuItemRecommendation.class);
        } else throw new BadResponseException("Some Error Occurred while getting Rolled Out Items!!");
    }

    public List<RolledOutMenuItem> getRolledOutMenuItems() throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.GET_NEXT_DAY_MENU_OPTIONS, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeList(parsedResponse.getJsonData(), RolledOutMenuItem.class);
        } else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String voteForMenuItem(Vote userVote) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.VOTE_NEXT_DAY_MENU, serializeData(userVote));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else if (responseCode == ResponseCode.BAD_REQUEST)
            return (deserializeData(parsedResponse.getJsonData(), String.class));
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String provideFeedback(Feedback feedback) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.PROVIDE_FEEDBACK, serializeData(feedback));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public List<Notification> seeNotifications() throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.SEE_NOTIFICATIONS, serializeData(GlobalData.loggedInUser));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeList(parsedResponse.getJsonData(), Notification.class);
        } else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    @Override
    public void closeConnection() throws IOException {
        connection.close();
    }
}