package org.cafeteria.client.services;

import com.google.gson.JsonSyntaxException;
import com.sun.istack.NotNull;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AdminService extends UserManager {

    public AdminService(ServerConnection connection, User user, Scanner scanner) {
        super(connection, user, scanner);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println();
            System.out.println("1. Show Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Delete Menu Item");
            System.out.println("4. Update Menu Item");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> handleDisplayMenu();
                case 2 -> addMenuItem(fetchMenuItemFromUser());
                case 3 -> handleDeleteMenuItem();
                case 4 -> handleUpdateMenuItem();
                case 5 -> {
                    connection.close();
                    return;
                }
            }
        }
    }

    public static void handleDisplayMenu() throws IOException {
        List<MenuItem> menuItems = getMenuItems();
        displayMenuItems(menuItems);
    }

    private static List<MenuItem> getMenuItems() throws IOException {
        String request = createRequest(UserAction.SHOW_MENU, null);
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
        } catch (InvalidResponseException e) {
            System.err.println("Invalid Response Received from Server" + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Error deserializing JSON data: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    private static void displayMenuItems(List<MenuItem> menuItems) {
        System.out.println();
        System.out.println("------------------------Food Item Menu------------------------");
        Map<Integer, List<MenuItem>> groupedItems = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getMealTypeId));

        for (Map.Entry<Integer, List<MenuItem>> entry : groupedItems.entrySet()) {
            MealTypeEnum mealType = getEnumFromOrdinal(MealTypeEnum.class, entry.getKey());
            System.out.println("\nMeal Type: " + (mealType != null ? mealType : "Unknown"));

            for (MenuItem item : entry.getValue()) {
                System.out.println("  ID: " + item.getId() +
                        "  Name: " + item.getName() +
                        "  Price: " + item.getPrice() +
                        "Rs.  Available: " + (item.isAvailable() ? "Yes" : "No") +
                        "  Last Prepared: " + (item.getLastTimePrepared() != null ? item.getLastTimePrepared() : "N/A")
                );
            }
        }
        System.out.println("--------------------------------------------------------------");
    }

    private void addMenuItem(MenuItem menuItem) {
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
        } catch (InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }

    private MenuItem fetchMenuItemFromUser() {
        System.out.println("Enter name of the food item to add in the menu:");
        String name = sc.nextLine();
        System.out.println("Enter price of the food item:");
        float price = sc.nextFloat();
        System.out.println("Enter availability of the food item (true/false):");
        boolean isAvailable = sc.nextBoolean();
        System.out.println("Enter the Meal type of the food item: 1. Lunch, 2. Breakfast, 3. Dinner");
        int mealTypeId = sc.nextInt();
        return new MenuItem(name, price, isAvailable, mealTypeId);
    }

    private void handleUpdateMenuItem() {
        MenuItem menuItem = getUpdatedMenuItem();
        if (menuItem != null) {
            updateMenuItem(menuItem);
        }
    }

    private void handleDeleteMenuItem() {
        System.out.println("Enter name of the food Item you want to delete from menu:");
        String name = sc.nextLine();
        MenuItem menuItem = getFoodItemByName(name);
        if (menuItem != null) {
            deleteMenuItem(menuItem);
        } else {
            System.out.println("No such food item exists in the menu");
        }
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
        } catch (InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
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
        } catch (InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
        return null;
    }


    private void deleteMenuItem(MenuItem menuItem) {
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
        } catch (InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }

    private MenuItem getUpdatedMenuItem() {
        System.out.print("Enter the name of the food item you want to update: ");
        String name = sc.nextLine();
        MenuItem menuItem = getFoodItemByName(name);
        if (menuItem != null) {
            System.out.println("Food item found: " + menuItem.getName());
            System.out.println("1. Update Price");
            System.out.println("2. Update Availability");
            System.out.println("3. Update Last Time Prepared");
            System.out.println("4. Update Meal Type");
            System.out.print("Choose an option to update (1/2/3/4): ");
            int option = sc.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.print("Enter new price: ");
                    float newPrice = sc.nextFloat();
                    menuItem.setPrice(newPrice);
                }
                case 2 -> {
                    System.out.print("Enter availability (true/false): ");
                    boolean isAvailable = sc.nextBoolean();
                    menuItem.setAvailable(isAvailable);
                }
                case 3 -> {
                    System.out.print("Enter new last time prepared (yyyy-MM-dd): ");
                    String date = sc.nextLine();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date newDate = sdf.parse(date);
                        menuItem.setLastTimePrepared(newDate);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format.");
                    }
                }
                case 4 -> {
                    System.out.println("Enter the new Meal Type of the food Item (1. Lunch, 2. Breakfast, 3. Dinner):");
                    int mealTypeId = sc.nextInt();
                    menuItem.setMealTypeId(mealTypeId);
                }
                default -> System.out.println("Invalid choice.");
            }
            return menuItem;

        } else {
            System.out.println("Food item with name '" + name + "' does not exist.");
            return null;
        }
    }

    private void updateMenuItem(MenuItem menuItem) {
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
        } catch (InvalidResponseException e) {
            System.out.println("Invalid Response Received from Server");
        }
    }
}