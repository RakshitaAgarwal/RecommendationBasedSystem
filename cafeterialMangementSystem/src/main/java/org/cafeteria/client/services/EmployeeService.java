package org.cafeteria.client.services;

import com.google.gson.JsonSyntaxException;
import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;

import java.io.IOException;
import java.util.*;

import static org.cafeteria.client.services.AdminService.*;
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
                case 3 -> handleNextDayMealVoting();
                case 4 -> provideFeedback();
                case 5 -> {
                    connection.close();
                    return;
                }
            }
        }
    }

    public void seeNotifications() {
        String request = createRequest(UserAction.SEE_NOTIFICATIONS, serializeData(GlobalData.loggedInUser));
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
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

    public void handleNextDayMealVoting() throws IOException {
        List<RolledOutMenuItem> rolledOutItems = getRolledOutMenuItems();
        int isContinue;
        do {
            System.out.println("\nSelect the Meal Type index you want to cast vote for (1. Lunch 2. Breakfast 3. Dinner):");
            int mealTypeId = sc.nextInt();

            Map<Integer, String> rolledOutItemsMap = new HashMap<>();

            for (RolledOutMenuItem rolledOutItem : rolledOutItems) {
                int menuItemId = rolledOutItem.getMenuItemId();
                MenuItem menuItem = getMenuItemById(menuItemId);
                if (menuItem.getMealTypeId() == mealTypeId) {
                    MenuItemRecommendation menuItemRecommendation = getRecommendationScoreForMenuItem(menuItemId);

                    String menuItemRecommendationScore = menuItem.getName() + " " + menuItemRecommendation.getRecommendationScore() + " " + menuItemRecommendation.getSentimentOfItem();
                    rolledOutItemsMap.put(menuItemId, menuItemRecommendationScore);
                }
            }

            for (Map.Entry<Integer, String> entry : rolledOutItemsMap.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue());
            }

            System.out.println("Enter the index of the recommendation you want to vote for:");
            int selectedIndex = sc.nextInt();

            if (rolledOutItemsMap.containsKey(selectedIndex)) {
                MenuItemUserVote userVote = new MenuItemUserVote(selectedIndex, GlobalData.loggedInUser.getId(), new Date());
                voteForMenuItem(userVote);
            } else {
                System.out.println("Invalid selection");
            }
            System.out.println("Do you wish to cast vote for another Meal Type? Enter 1-Yes / 0-No.");
            isContinue = sc.nextInt();
        } while (isContinue == 1);
    }

    private MenuItemRecommendation getRecommendationScoreForMenuItem(int menuItemId) {
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

    private List<RolledOutMenuItem> getRolledOutMenuItems() {
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

    private void voteForMenuItem(MenuItemUserVote userVote) throws IOException {
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

    public void provideFeedback() {
        Feedback feedback = takeUserFeedback();
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

    private Feedback takeUserFeedback() {
        Feedback feedback = new Feedback();
        System.out.println("Enter the food item you want to provide feedback for:");
        String foodItemName = sc.nextLine();
        MenuItem menuItem = getFoodItemByName(foodItemName);
        if (menuItem != null) {
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
}