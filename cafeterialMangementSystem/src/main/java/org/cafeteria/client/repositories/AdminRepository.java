package org.cafeteria.client.repositories;

import com.sun.istack.NotNull;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

import java.io.IOException;
import java.util.List;

public class AdminRepository extends UserRepository {

    public AdminRepository(ServerConnection connection) {
        super(connection);
    }

    public String addMenuItem(MenuItem menuItem) throws InvalidResponseException, IOException, BadResponseException {
        String request = createRequest(UserAction.ADD_MENU_ITEM, serializeData(menuItem));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String deleteMenuItem(MenuItem menuItem) throws InvalidResponseException, IOException, BadResponseException {
        String request = createRequest(UserAction.DELETE_MENU_ITEM, serializeData(menuItem));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String updateMenuItem(MenuItem menuItem) throws InvalidResponseException, IOException, BadResponseException {
        String request = createRequest(UserAction.UPDATE_MENU_ITEM, serializeData(menuItem));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public static MenuItem getMenuItemById(@NotNull int id) throws InvalidResponseException, IOException, BadResponseException {
        String request = createRequest(UserAction.GET_MENU_ITEM_BY_ID, serializeData(id));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), MenuItem.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public static MenuItem getFoodItemByName(@NotNull String name) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.GET_MENU_ITEM_BY_NAME, serializeData(name));
        String response = connection.sendData(request);
        if (response == null) {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), MenuItem.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public static List<MenuItem> getMenuItems() throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.GET_ALL_MENU_ITEMS, null);
        String response = connection.sendData(request);

        if (response == null)
            throw new IOException("Server got disconnected. Please try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeList(parsedResponse.getJsonData(), MenuItem.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public List<DiscardMenuItem> getDiscardMenuItems() throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.GET_DISCARD_MENU_ITEMS, null);
        String response = connection.sendData(request);

        if (response == null) {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeList(parsedResponse.getJsonData(), DiscardMenuItem.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public static String sendNotificationToAllEmployees(Notification notification) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.SEND_NOTIFICATION_TO_EMPLOYEES, serializeData(notification));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String createDetailedFeedbackRequest(int menuItemId) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.CREATE_DETAILED_FEEDBACK_REQUEST, serializeData(menuItemId));
        String response = connection.sendData(request);
        if (response == null) {
            throw new IOException("Server Got Disconnected. Please Try again.");
        }
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeData(parsedResponse.getJsonData(), String.class);
        } else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }
}