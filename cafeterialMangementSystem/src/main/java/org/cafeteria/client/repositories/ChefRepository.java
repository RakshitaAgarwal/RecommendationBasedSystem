package org.cafeteria.client.repositories;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.deserializeData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChefRepository extends UserRepository{
    public ChefRepository(ServerConnection connection) {
        super(connection);
    }
    public Map<Integer, Integer> getVotingForMenuItem() throws IOException {
        String request = createRequest(UserAction.GET_VOTING_FOR_NEXT_DAY_MENU, serializeData(new Date()));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response != null) {
            try {
                ParsedResponse parsedResponse = parseResponse(response);
                ResponseCode responseCode = parsedResponse.getResponseCode();
                if (responseCode == ResponseCode.OK) {
                    Type mapType = new TypeToken<Map<Integer, Integer>>() {
                    }.getType();
                    return deserializeMap(parsedResponse.getJsonData(), mapType);
                } else {
                    System.out.println("Unexpected Response Code: " + responseCode);
                }
            } catch (CustomExceptions.InvalidResponseException e) {
                System.out.println("Invalid Response Received from Server");
            } catch (JsonSyntaxException e) {
                System.out.println("Error deserializing JSON data: " + e.getMessage());
            }
        } else {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
        return null;
    }

    public Map<MealTypeEnum, List<MenuItemRecommendation>> getRecommendationsForNextDayMenu() {
        String request = createRequest(UserAction.GET_RECOMMENDATION_FOR_NEXT_DAY_MENU, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                Type mapType = new TypeToken<Map<MealTypeEnum, List<MenuItemRecommendation>>>() {
                }.getType();
                return deserializeMap(parsedResponse.getJsonData(), mapType);
            } else System.out.println("Some Error Occurred while getting recommendations!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    public void processRollOutMenuOptions(List<Integer> rolledOutItems) {
        String request = createRequest(UserAction.ROLL_OUT_NEXT_DAY_MENU_OPTIONS, serializeData(rolledOutItems));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                System.out.println("Items rolled out successfully for voting");
            } else System.out.println("Some Error Occurred!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }

    public void processUpdatingFinalMenu(List<Integer> preparedMenuItemIds) throws IOException {
        String request = createRequest(UserAction.UPDATE_NEXT_DAY_FINAL_MENU, serializeData(preparedMenuItemIds));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response != null) {
            try {
                ParsedResponse parsedResponse = parseResponse(response);
                System.out.println(deserializeData(parsedResponse.getJsonData(), String.class));
            } catch (CustomExceptions.InvalidResponseException e) {
                System.out.println("Invalid Response Received from Server");
            } catch (JsonSyntaxException e) {
                System.out.println("Error deserializing JSON data: " + e.getMessage());
            }
        } else {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
    }

    @Override
    public void closeConnection() throws IOException {
        connection.close();
    }
}