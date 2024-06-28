package org.cafeteria.client.repositories;

import com.google.gson.JsonSyntaxException;
import com.sun.istack.NotNull;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.ParsedResponse;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.UserAction;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class AdminRepository extends UserRepository{

    public AdminRepository(ServerConnection connection) {
        super(connection);
    }

    public void addMenuItem(MenuItem menuItem) {
        String request = createRequest(UserAction.ADD_MENU_ITEM, serializeData(menuItem));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK)
                System.out.println("Food Item Added Successfully in the Menu.");
            else System.out.println("Some Error Occurred!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }

    public void deleteMenuItem(MenuItem menuItem) {
        String request = createRequest(UserAction.DELETE_MENU_ITEM, serializeData(menuItem));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK)
                System.out.println("Food Item Deleted Successfully from the Menu.");
            else System.out.println("Some Error Occurred!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }

    public void updateMenuItem(MenuItem menuItem) {
        String request = createRequest(UserAction.UPDATE_MENU_ITEM, serializeData(menuItem));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK)
                System.out.println("Food Item Updated Successfully in the Menu.");
            else System.out.println("Some Error Occurred!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }
    public static MenuItem getMenuItemById(@NotNull int id) {
        String request = createRequest(UserAction.GET_MENU_ITEM_BY_ID, serializeData(id));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK)
                return deserializeData(parsedResponse.getJsonData(), MenuItem.class);
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    public static MenuItem getFoodItemByName(@NotNull String name) {
        String request = createRequest(UserAction.GET_MENU_ITEM_BY_NAME, serializeData(name));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK)
                return deserializeData(parsedResponse.getJsonData(), MenuItem.class);
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    public static List<MenuItem> getMenuItems() throws IOException {
        String request = createRequest(UserAction.GET_ALL_MENU_ITEMS, null);
        System.out.println("request that is sent to server: " + request);

        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);

        if (response == null) {
            throw new IOException("Server got disconnected. Please try again.");
        }

        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                return deserializeList(parsedResponse.getJsonData(), MenuItem.class);
            } else {
                System.out.println("Some Error Occurred");
            }
        } catch (CustomExceptions.InvalidResponseException e) {
            System.err.println("Invalid Response Received from Server" + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Error deserializing JSON data: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public void closeConnection() throws IOException {
        connection.close();
    }
}