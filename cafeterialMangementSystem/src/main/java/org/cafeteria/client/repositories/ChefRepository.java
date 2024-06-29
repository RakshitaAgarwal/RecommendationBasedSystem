package org.cafeteria.client.repositories;

import com.google.gson.reflect.TypeToken;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.deserializeData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChefRepository extends UserRepository {
    public ChefRepository(ServerConnection connection) {
        super(connection);
    }

    public Map<Integer, Integer> getVotingForMenuItem() throws IOException, BadResponseException, InvalidResponseException {
        String request = createRequest(UserAction.GET_VOTING_FOR_NEXT_DAY_MENU, serializeData(new Date()));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);

        if (response == null) {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            Type mapType = new TypeToken<Map<Integer, Integer>>() {
            }.getType();
            return deserializeMap(parsedResponse.getJsonData(), mapType);
        } else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public Map<MealTypeEnum, List<MenuItemRecommendation>> getRecommendationsForNextDayMenu() throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.GET_RECOMMENDATION_FOR_NEXT_DAY_MENU, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);

        if (response == null) {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            Type mapType = new TypeToken<Map<MealTypeEnum, List<MenuItemRecommendation>>>() {
            }.getType();
            return deserializeMap(parsedResponse.getJsonData(), mapType);
        } else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String processRollOutMenuOptions(List<Integer> rolledOutItems) throws InvalidResponseException, IOException, BadResponseException {
        String request = createRequest(UserAction.ROLL_OUT_NEXT_DAY_MENU_OPTIONS, serializeData(rolledOutItems));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response == null) {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeData(parsedResponse.getJsonData(), String.class);
        } else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String processUpdatingFinalMenu(List<Integer> preparedMenuItemIds) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.UPDATE_NEXT_DAY_FINAL_MENU, serializeData(preparedMenuItemIds));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response == null) {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if(responseCode == ResponseCode.OK) {
            return deserializeData(parsedResponse.getJsonData(), String.class);
        }else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    @Override
    public void closeConnection() throws IOException {
        connection.close();
    }
}