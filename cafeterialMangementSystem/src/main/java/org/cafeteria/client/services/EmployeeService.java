package org.cafeteria.client.services;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.istack.NotNull;
import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.cafeteria.client.services.ChefService.displayMenuItemsRecommendationByMealType;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class EmployeeService extends UserManager {

    public EmployeeService(ServerConnection connection, User user, Scanner sc) {
        super(connection, user, sc);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. See Notifications");
            System.out.println("3. Vote For Next Day Menu");
            System.out.println("4. Provide Feedback");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> displayMenuFromServer();
                case 2 -> seeNotifications();
                case 3 -> voteForNextDayMenu();
                case 4 -> provideFeedback();
                case 5 -> {
                    connection.close();
                    return;
                }
            }
        }
    }

    @Override
    public void displayMenuFromServer() throws IOException {
        String request = createRequest(UserAction.SHOW_MENU, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if( response != null) {
            try {
                ParsedResponse parsedResponse = parseResponse(response);
                ResponseCode responseCode = parsedResponse.getResponseCode();
                if (responseCode == ResponseCode.OK) {
                    List<MenuItem> menu = deserializeList(parsedResponse.getJsonData(), MenuItem.class);
                    displayMenu(menu);
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
    }

    private void displayMenu(List<MenuItem> menu) {
        System.out.println();
        System.out.println("--------Food Item Menu--------");
        for (MenuItem item : menu) {
            System.out.println(item.getName() + "  " + item.getPrice() + " Rs  Available Status: " + item.isAvailable());
        }
        System.out.println("------------------------------");
    }

    public void seeNotifications() {
        String request = createRequest(UserAction.SEE_NOTIFICATIONS, serializeData(GlobalData.loggedInUser));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK)
            {
                displayUserNotifications(deserializeList(parsedResponse.getJsonData(), Notification.class));
            } else System.out.println("Some Error Occurred!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }

    public void displayUserNotifications(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            System.out.println("No notifications to display.");
            return;
        }

        for (Notification notification : notifications) {
            System.out.println("-----------------------------------");
            System.out.println("Notification ID: " + notification.getId());
            System.out.println("User ID: " + notification.getUserId());
            System.out.println("Notification Type ID: " + notification.getNotificationTypeId());
            System.out.println("Notification Message: " + notification.getNotificationMessage());
            System.out.println("Date and Time: " + notification.getDateTime());
            System.out.println("Is Notification Read: " + (notification.getNotificationRead() ? "Yes" : "No"));
            System.out.println("-----------------------------------");
        }
    }

    public void voteForNextDayMenu() {
        Map<MealTypeEnum, List<MenuItemRecommendation>> rolledOutItems = getRolledOutMenuItems();
        displayMenuItemsRecommendationByMealType(rolledOutItems);
        chooseItemToVote();
        voteForMenuItem();
    }

    private Map<MealTypeEnum, List<MenuItemRecommendation>> getRolledOutMenuItems() {
        String request = createRequest(UserAction.GET_NEXT_DAY_MENU_OPTIONS, null);
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
            } else System.out.println("Some Error Occurred while getting Rolled Out Items!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    private void displayRolledOutMenuItems() {
    }

    private void chooseItemToVote() {
    }

    private void voteForMenuItem() {
    }

    public void provideFeedback() {
        Feedback feedback = takeUserFeedback();
        if( feedback != null) {
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

    private Feedback takeUserFeedback() {
        Feedback feedback = new Feedback();
        System.out.println("Enter the food item you want to provide feedback for:");
        String foodItemName = sc.nextLine();
        MenuItem menuItem = getFoodItemByName(foodItemName);
        if(menuItem != null) {
            System.out.println("Enter Feedback/Comment for " + foodItemName);
            String comment = sc.nextLine();
            System.out.println("Enter Rating for the " + foodItemName + " out of 5");
            float rating = sc.nextFloat();
            feedback.setMenuItemId(menuItem.getId());
            feedback.setComment(comment);
            feedback.setRating(rating);
            feedback.setUserId(GlobalData.loggedInUser.getId());
            feedback.setDateTime(new Date());
            return feedback;
        } else {
            System.out.println("No Such food item exists in the menu");
        }
        return null;
    }

    private MenuItem getFoodItemByName(@NotNull String name) {
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
}