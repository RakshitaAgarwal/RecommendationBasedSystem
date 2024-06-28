package org.cafeteria.client.services;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;
import static org.cafeteria.client.services.AdminService.displayMenu;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ChefService extends UserManager {

    public ChefService(ServerConnection connection, User user, Scanner sc) {
        super(connection, user, sc);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. See Monthly Report");
            System.out.println("3. Roll Out Items for Next Day Menu");
            System.out.println("4. See Voting for Rolled out Menu Items");
            System.out.println("5. Update Next Day final Menu Items");
            System.out.println("6. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> displayMenu();
                case 2 -> seeMonthlyReport();
                case 3 -> handleRollOutNextDayMenuOptions();
                case 4 -> seeVotingForRolledOutItems();
                case 5 -> updateNextDayMenuItems();
                case 6 -> {
                    connection.close();
                    return;
                }
            }
        }
    }

    private void seeVotingForRolledOutItems() throws IOException {
        Map<Integer, Integer> nextDayVoting = getVotingForMenuItem();
        displayVoting(nextDayVoting);
    }

    private Map<Integer, Integer> getVotingForMenuItem() throws IOException {
        String request = createRequest(UserAction.GET_VOTING_FOR_NEXT_DAY_MENU, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response != null) {
            try {
                ParsedResponse parsedResponse = parseResponse(response);
                ResponseCode responseCode = parsedResponse.getResponseCode();
                if (responseCode == ResponseCode.OK) {
                    Type mapType = new TypeToken<Map<Integer, Integer>>() {}.getType();
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

    private void displayVoting(Map<Integer, Integer> nextDayVoting) {
        System.out.println();
        System.out.println("--------Votes For Next Day menu--------");
        for (Map.Entry<Integer, Integer> entry : nextDayVoting.entrySet()) {
            int menuItemId = entry.getKey();
            int votes = entry.getValue();

            System.out.println(menuItemId + " No of votes : " + votes);
        }
        System.out.println("------------------------------");
    }

    public void seeMonthlyReport() {
        String response = connection.sendData("SEE_MONTHLY_REPORT");
        System.out.println(response);
    }

    public void handleRollOutNextDayMenuOptions() {
        Map<MealTypeEnum, List<MenuItemRecommendation>> recommendedItems = getRecommendationsForNextDayMenu();
        List<Integer> rolledOutItems = new ArrayList<>();
        if(recommendedItems==null || recommendedItems.isEmpty()) {
            rollOutRandomItems();
        } else {
            displayMenuItemsRecommendationByMealType(recommendedItems);
            rolledOutItems = getTopRolledOutMenuItemIds(recommendedItems, 5);
        }
        if(!rolledOutItems.isEmpty()) {
            processRollOutMenuOptions(rolledOutItems);
        }
    }

    private Map<MealTypeEnum, List<MenuItemRecommendation>> getRecommendationsForNextDayMenu() {
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

    private void rollOutRandomItems() {
    }

    public static void displayMenuItemsRecommendationByMealType(Map<MealTypeEnum, List<MenuItemRecommendation>> menuItemsByMealType) {
        for (Map.Entry<MealTypeEnum, List<MenuItemRecommendation>> entry : menuItemsByMealType.entrySet()) {
            MealTypeEnum mealType = entry.getKey();
            List<MenuItemRecommendation> menuItems = entry.getValue();
            System.out.println("Meal Type: " + mealType);
            System.out.println("Menu Items:");
            for (MenuItemRecommendation menuItemRecommendation : menuItems) {
                System.out.println("  " + menuItemRecommendation.getMenuItemId() + " " + menuItemRecommendation.getRecommendationScore() + " " + menuItemRecommendation.getSentimentOfItem());
            }
            System.out.println();
        }
    }

    public static List<Integer> getTopRolledOutMenuItemIds(Map<MealTypeEnum, List<MenuItemRecommendation>> menuItemScoresMap, int topCount) {
        List<Integer> menuItemIds = new ArrayList<>();

        for (Map.Entry<MealTypeEnum, List<MenuItemRecommendation>> entry : menuItemScoresMap.entrySet()) {
            List<MenuItemRecommendation> menuItemRecommendations = entry.getValue();

            List<MenuItemRecommendation> topMenuItemRecommendations = menuItemRecommendations.stream()
                    .limit(topCount)
                    .toList();
            for (MenuItemRecommendation menuItemRecommendation : topMenuItemRecommendations) {
                menuItemIds.add(menuItemRecommendation.getMenuItemId());
            }
        }

        return menuItemIds;
    }

    private void processRollOutMenuOptions(List<Integer> rolledOutItems) {
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

    private void updateNextDayMenuItems() {
    }
}