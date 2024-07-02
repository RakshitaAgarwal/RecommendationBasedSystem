package org.cafeteria.client.handlers;

import org.cafeteria.client.consoleManager.AdminConsoleManager;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.AdminRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.enums.*;
import static org.cafeteria.client.repositories.AdminRepository.sendNotificationToAllEmployees;
import static org.cafeteria.common.constants.Constants.DETAILED_FEEDBACK_MESSAGE;

import java.io.IOException;
import java.util.*;

public class AdminHandler extends UserHandler {
    private static AdminRepository adminRepository;
    private static AdminConsoleManager adminConsoleManager;

    public AdminHandler(ServerConnection connection, User user, Scanner sc) {
        super(user);
        adminRepository = new AdminRepository(connection);
        adminConsoleManager = new AdminConsoleManager(sc);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            adminConsoleManager.displayUserActionItems();
            int choice = adminConsoleManager.takeUserChoice("Enter your Choice: ");
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
                    default -> adminConsoleManager.displayMessage("Invalid Choice");
                }
            } catch (InvalidResponseException | BadResponseException e) {
                adminConsoleManager.displayMessage(e.getMessage());
            }
        }
    }

    public static void handleDisplayMenu() throws IOException, InvalidResponseException, BadResponseException {
        List<MenuItem> menuItems = AdminRepository.getMenuItems();
        adminConsoleManager.displayMenuItems(menuItems);
    }

    public void handleAddMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        MenuItem menuItem = adminConsoleManager.takeMenuItemFromUser();
        String response = adminRepository.addMenuItem(menuItem);
        adminConsoleManager.displayMessage(response);
    }

    private void handleDeleteMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        String menuItemName = adminConsoleManager.takeUserStringInput("Enter name of the food Item you want to delete from menu:");
        MenuItem menuItem = AdminRepository.getFoodItemByName(menuItemName);
        if (menuItem != null) {
            adminConsoleManager.displayMessage(adminRepository.deleteMenuItem(menuItem));
        } else {
            adminConsoleManager.displayMessage("No such food item exists in the menu");
        }
    }

    private void handleUpdateMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        MenuItem menuItem = getUpdatedMenuItem();
        if (menuItem != null) {
            adminConsoleManager.displayMessage(adminRepository.updateMenuItem(menuItem));
        }
    }

    private MenuItem getUpdatedMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        String menuItemName = adminConsoleManager.takeUserStringInput("Enter the name of the food item you want to update: ");
        MenuItem menuItem = AdminRepository.getFoodItemByName(menuItemName);
        if (menuItem != null) {
            adminConsoleManager.displayMessage("Food item found: " + menuItem.getName());
            adminConsoleManager.displayMenuItemUpdateOptions();
            int option = adminConsoleManager.takeUserChoice("Choose an option to update (1/2/3/4/5/6/7/8): ");
            switch (option) {
                case 1 -> {
                    float newPrice = adminConsoleManager.takeUserFloatInput("Enter new price: ");
                    menuItem.setPrice(newPrice);
                }
                case 2 -> {
                    boolean isAvailable = adminConsoleManager.takeUserBooleanInput("Enter availability:");
                    menuItem.setAvailable(isAvailable);
                }
                case 3 -> {
                    Date date = adminConsoleManager.takeUserDateInput("Enter new last time prepared (yyyy-MM-dd): ");
                    menuItem.setLastTimePrepared(date);
                }
                case 4 -> {
                    int mealTypeId = adminConsoleManager.takeMealTypeId();
                    menuItem.setMealTypeId(mealTypeId);
                }
                case 5 -> {
                    int menuItemTypeId = adminConsoleManager.takeDietaryPreferenceId();
                    menuItem.setMenuItemTypeId(menuItemTypeId);
                }
                case 6 -> {
                    int sweetContentLevelId = adminConsoleManager.takeSweetLevelId();
                    menuItem.setSweetContentLevelId(sweetContentLevelId);
                }
                case 7 -> {
                    int spiceContentLevelId = adminConsoleManager.takeSpiceLevelId();
                    menuItem.setSpiceContentLevelId(spiceContentLevelId);
                }
                case 8 -> {
                    int cuisineTypeId = adminConsoleManager.takeCuisineId();
                    menuItem.setCuisineTypeId(cuisineTypeId);
                }
                default -> adminConsoleManager.displayMessage("Invalid choice.");
            }
            return menuItem;

        } else {
            adminConsoleManager.displayMessage("Food item with name '" + menuItemName + "' does not exist.");
            return null;
        }
    }

    public void handleDiscardMenuItems() throws IOException, BadResponseException, InvalidResponseException {
        List<DiscardMenuItem> discardedMenuItems = adminRepository.getDiscardMenuItems();
        boolean continueAction;
        do {
            adminConsoleManager.displayDiscardedMenuItems(discardedMenuItems);
            int menuItemId = adminConsoleManager.takeUserChoice("Enter the Menu Item Id you want to perform action for:");
            MenuItem menuItem = AdminRepository.getMenuItemById(menuItemId);
            if (menuItem != null && isValidDiscardMenuItemId(menuItemId, discardedMenuItems)) {
                handleDiscardMenuItemAction(menuItem);
            } else {
                System.out.println("Invalid Discard Menu Item Id.");
            }
            continueAction = adminConsoleManager.takeUserBooleanInput("Do you wish to Continue for other Discard Menu Item? true/false");
        } while (continueAction);
    }

    private boolean isValidDiscardMenuItemId(int menuItemId, List<DiscardMenuItem> discardedMenuItems) {
        for (DiscardMenuItem discardMenuItem : discardedMenuItems) {
            if (discardMenuItem.getMenuItemId() == menuItemId) return true;
        }
        return false;
    }

    private void handleDiscardMenuItemAction(MenuItem menuItem) throws BadResponseException, IOException, InvalidResponseException {
        adminConsoleManager.displayDiscardMenuItemActions();
        int choice = adminConsoleManager.takeUserChoice("Enter Choice:");
        switch (choice) {
            case 1 -> {
                if (adminConsoleManager.takeUserBooleanInput("Are you sure you want to permanently remove food item from menu. true/false"))
                    adminRepository.deleteMenuItem(menuItem);
            }
            case 2 -> handleGetDetailedFeedback(menuItem.getName());
            default -> adminConsoleManager.displayMessage("Invalid choice selected");
        }
    }

    private void handleGetDetailedFeedback(String menuItemName) throws IOException, InvalidResponseException, BadResponseException {
        String notificationMessage = String.format(DETAILED_FEEDBACK_MESSAGE, menuItemName, menuItemName, menuItemName);
        Notification notification = new Notification(
                NotificationTypeEnum.GET_DETAILED_FEEDBACK.ordinal() + 1,
                notificationMessage,
                new Date());
        adminConsoleManager.displayMessage(sendNotificationToAllEmployees(notification));
    }
}