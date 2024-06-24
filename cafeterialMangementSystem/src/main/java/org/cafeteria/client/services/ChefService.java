package org.cafeteria.client.services;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

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
            System.out.println("4. Update Next Day final Menu Items");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> displayMenuFromServer();
                case 2 -> seeMonthlyReport();
                case 3 -> rollOutNextDayMenuOptions();
                case 4 -> updateNextDayMenuItems();
                case 5 -> {
                    connection.close();
                    return;
                }
            }
        }
    }

    private void updateNextDayMenuItems() {
    }

    @Override
    public void displayMenuFromServer() throws IOException {
        String request = createRequest(UserAction.SHOW_MENU, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        if (response != null) {
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

    public void seeMonthlyReport() throws IOException {
        String response = connection.sendData("SEE_MONTHLY_REPORT");
        System.out.println(response);
    }

    public void rollOutNextDayMenuOptions() {
        Map<MealTypeEnum, List<MenuItem>> recommendedItems = getRecommendationsForNextDayMenu();
        displayRecommendations(recommendedItems);
        Map<MealTypeEnum, List<MenuItem>> rolledOutItems = getRolledOutItems(recommendedItems, 5);

        String request = createRequest(UserAction.ROLL_OUT_NEXT_DAY_MENU_OPTIONS, serializeMap(rolledOutItems));
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

    public static Map<MealTypeEnum, List<MenuItem>> getRolledOutItems(Map<MealTypeEnum, List<MenuItem>> recommendedItems, int noOfItemsToRollOut) {
        Map<MealTypeEnum, List<MenuItem>> rolledOutItems = new HashMap<>();
        Random random = new Random();

        for (Map.Entry<MealTypeEnum, List<MenuItem>> entry : recommendedItems.entrySet()) {
            List<MenuItem> recommendedList = entry.getValue();
            List<MenuItem> selectedItems = new ArrayList<>();

            if (recommendedList.size() >= noOfItemsToRollOut) {
                Set<Integer> selectedIndices = new HashSet<>();
                while (selectedIndices.size() < noOfItemsToRollOut) {
                    int randomIndex = random.nextInt(recommendedList.size());
                    selectedIndices.add(randomIndex);
                }
                for (int index : selectedIndices) {
                    selectedItems.add(recommendedList.get(index));
                }
            } else {
                selectedItems.addAll(recommendedList);
            }

            rolledOutItems.put(entry.getKey(), selectedItems);
        }

        return rolledOutItems;
    }

    private Map<MealTypeEnum, List<MenuItem>> getRecommendationsForNextDayMenu() {
        String request = createRequest(UserAction.GET_RECCOMENDATION_FOR_NEXT_DAY_MENU, null);
        System.out.println("request that is sent to server: " + request);
        String response = connection.sendData(request);
        System.out.println("response that is received from server: " + response);
        try {
            ParsedResponse parsedResponse = parseResponse(response);
            ResponseCode responseCode = parsedResponse.getResponseCode();
            if (responseCode == ResponseCode.OK) {
                Type mapType = new TypeToken<Map<MealTypeEnum, List<MenuItem>>>() {
                }.getType();
                Map<MealTypeEnum, List<MenuItem>> recommendedItems = deserializeMap(parsedResponse.getJsonData(), mapType);
                return recommendedItems;
            } else System.out.println("Some Error Occurred while getting recommendations!!");
        } catch (CustomExceptions.InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }

    public static void displayRecommendations(Map<MealTypeEnum, List<MenuItem>> menuItemsByMealType) {
        for (Map.Entry<MealTypeEnum, List<MenuItem>> entry : menuItemsByMealType.entrySet()) {
            MealTypeEnum mealType = entry.getKey();
            List<MenuItem> menuItems = entry.getValue();

            System.out.println("Meal Type: " + mealType);
            System.out.println("Menu Items:");
            for (MenuItem menuItem : menuItems) {
                System.out.println("  " + menuItem.getName());
            }
            System.out.println();
        }
    }
}