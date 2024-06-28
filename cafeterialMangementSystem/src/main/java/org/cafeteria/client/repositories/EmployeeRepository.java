package org.cafeteria.client.repositories;

import com.google.gson.JsonSyntaxException;
import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;

import java.io.IOException;
import java.util.List;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.deserializeData;

public class EmployeeRepository extends UserRepository{
    public EmployeeRepository(ServerConnection connection) {
        super(connection);
    }

    public MenuItemRecommendation getRecommendationScoreForMenuItem(int menuItemId) {
        String request = createRequest(UserAction.GET_MENU_ITEM_RECOMMENDATION_SCORE, serializeData(menuItemId));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                return deserializeData(parsedResponse.getJsonData(), MenuItemRecommendation.class);
            } else System.out.println("Some Error Occurred while getting Rolled Out Items!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    public List<RolledOutMenuItem> getRolledOutMenuItems() {
        String request = createRequest(UserAction.GET_NEXT_DAY_MENU_OPTIONS, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                return deserializeList(parsedResponse.getJsonData(), RolledOutMenuItem.class);
            } else System.out.println("Some Error Occurred while getting Rolled Out Items!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    public void voteForMenuItem(Vote userVote) throws IOException {
        String request = createRequest(UserAction.VOTE_NEXT_DAY_MENU, serializeData(userVote));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response != null) {
            try {
                ParsedResponse parsedResponse = parseResponse(response);
                ResponseCode responseCode = parsedResponse.getResponseCode();
                if (responseCode == ResponseCode.OK) {
                    System.out.println("Your vote has been successfully recorded.");
                } else if (responseCode == ResponseCode.BAD_REQUEST) {
                    System.out.println(deserializeData(parsedResponse.getJsonData(), String.class));
                } else {
                    System.out.println("Some error occurred while casting the vote");
                }
            } catch (CustomExceptions.InvalidResponseException e) {
                System.out.println("Invalid Response Received from Server");
            } catch (JsonSyntaxException e) {
                System.out.println("Error deserializing JSON data: " + e.getMessage());
            }
        } else {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
    }

    public void provideFeedback(Feedback feedback) {
        if (feedback != null) {
            String request = createRequest(UserAction.PROVIDE_FEEDBACK, serializeData(feedback));
            System.out.println("request that is sent to server: " + request);
            String response = connection.sendData(request);
            System.out.println("response that is received from server: " + response);
            try {
                ParsedResponse parsedResponse = parseResponse(response);
                ResponseCode responseCode = parsedResponse.getResponseCode();
                if (responseCode == ResponseCode.OK)
                    System.out.println("Feedback updated Successfully.");
                else System.out.println("Some Error Occurred!!");
            } catch (CustomExceptions.InvalidResponseException e) {
                System.out.println("Invalid Response Received from Server");
            }
        }
    }

    public List<Notification> seeNotifications() {
        String request = createRequest(UserAction.SEE_NOTIFICATIONS, serializeData(GlobalData.loggedInUser));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                return deserializeList(parsedResponse.getJsonData(), Notification.class);
            } else System.out.println("Some Error Occurred!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    @Override
    public void closeConnection() throws IOException {
        connection.close();
    }
}
