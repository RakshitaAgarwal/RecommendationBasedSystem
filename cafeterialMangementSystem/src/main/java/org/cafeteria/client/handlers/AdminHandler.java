package org.cafeteria.client.handlers;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.AdminRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import static org.cafeteria.client.repositories.AdminRepository.sendNotificationToAllEmployees;
import static org.cafeteria.common.constants.Constants.DETAILED_FEEDBACK_MESSAGE;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AdminHandler extends UserManager {

    private static AdminRepository adminRepository;

    public AdminHandler(ServerConnection connection, User user, Scanner scanner) {
        super(user, scanner);
        adminRepository = new AdminRepository(connection);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println();
            System.out.println("1. Show Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Delete Menu Item");
            System.out.println("4. Update Menu Item");
            System.out.println("5. See Discarded Menu Items");
            System.out.println("6. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> handleDisplayMenu();
                    case 2 -> handleAddMenuItem();
                    case 3 -> handleDeleteMenuItem();
                    case 4 -> handleUpdateMenuItem();
                    case 5 -> handleDiscardMenuItems();
                    case 6 -> {
                        adminRepository.closeConnection();
                        return;
                    }
                }
            } catch (InvalidResponseException | BadResponseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void handleAddMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        System.out.println(adminRepository.addMenuItem(fetchMenuItemFromUser()));
    }

    public static void handleDisplayMenu() throws IOException, InvalidResponseException, BadResponseException {
        List<MenuItem> menuItems = AdminRepository.getMenuItems();
        displayMenuItems(menuItems);
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

    private void handleUpdateMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        MenuItem menuItem = takeUserInputForItemUpdate();
        if (menuItem != null) {
            System.out.println(adminRepository.updateMenuItem(menuItem));
        }
    }

    private void handleDeleteMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        System.out.println("Enter name of the food Item you want to delete from menu:");
        String name = sc.nextLine();
        MenuItem menuItem = AdminRepository.getFoodItemByName(name);
        if (menuItem != null) {
            System.out.println(adminRepository.deleteMenuItem(menuItem));
        } else {
            System.out.println("No such food item exists in the menu");
        }
    }

    private MenuItem takeUserInputForItemUpdate() throws IOException, InvalidResponseException, BadResponseException {
        System.out.print("Enter the name of the food item you want to update: ");
        String name = sc.nextLine();
        MenuItem menuItem = AdminRepository.getFoodItemByName(name);
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

    public void handleDiscardMenuItems() throws IOException {
        try {
            List<DiscardMenuItem> discardedMenuItems = adminRepository.getDiscardMenuItems();
            displayDiscardedMenuItems(discardedMenuItems);
            boolean isContinue;
            do {
                System.out.println("Enter the Menu Item Id you want to perform action for:");
                int menuItemId = sc.nextInt();
                MenuItem menuItem = adminRepository.getMenuItemById(menuItemId);
                if (menuItem != null && isValidDiscardMenuItemId(menuItemId, discardedMenuItems)) {
                    handleDiscardMenuItemAction(menuItem);
                } else {
                    System.out.println("Invalid Discard Menu Item Id.");
                }
                System.out.println("Do you wish to Continue for other Discard Menu Item? true/false");
                isContinue = sc.nextBoolean();
            } while (isContinue);
        } catch (InvalidResponseException | BadResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isValidDiscardMenuItemId(int menuItemId, List<DiscardMenuItem> discardedMenuItems) {
        for (DiscardMenuItem discardMenuItem : discardedMenuItems) {
            if (discardMenuItem.getMenuItemId() == menuItemId) return true;
        }
        return false;
    }

    private void handleDiscardMenuItemAction(MenuItem menuItem) throws BadResponseException, IOException, InvalidResponseException {
        System.out.println("Choose action to perform:");
        System.out.println("1. Remove the Food Item from Menu");
        System.out.println("2. Get Detailed Feedback");
        int choice = sc.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Are you sure you want to permanently remove food item from menu. true/false");
                if (sc.nextBoolean()) adminRepository.deleteMenuItem(menuItem);
            }
            case 2 -> handleGetDetailedFeedback(menuItem.getName());
            default -> System.out.println("Invalid choice selected");
        }
    }

    private void handleGetDetailedFeedback(String menuItemName) throws IOException, InvalidResponseException, BadResponseException {
        String notificationMessage = String.format(DETAILED_FEEDBACK_MESSAGE, menuItemName, menuItemName, menuItemName);
        Notification notification = new Notification(
                NotificationTypeEnum.GET_DETAILED_FEEDBACK.ordinal() + 1,
                notificationMessage,
                new Date());
        System.out.println(sendNotificationToAllEmployees(notification));
    }

    private void displayDiscardedMenuItems(List<DiscardMenuItem> items) {
        System.out.println("---------------------------------------------------------");
        System.out.printf("%-5s | %-10s | %-10s%n", "ID", "MenuItemID", "Avg. Rating");
        System.out.println("---------------------------------------------------------");
        for (DiscardMenuItem item : items) {
            System.out.printf("%-5d | %-10d | %-10.2f%n",
                    item.getId(), item.getMenuItemId(), item.getAvgRating());
        }
        System.out.println("----------------------------------------------------------");
    }
}