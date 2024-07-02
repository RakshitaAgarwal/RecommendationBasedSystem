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
                    default -> throw new InvalidChoiceException("Invalid Choice");
                }
            } catch (InvalidResponseException | BadResponseException | InvalidChoiceException e) {
                adminConsoleManager.displayMessage(e.getMessage());
            }
        }
    }

    public static void handleDisplayMenu() throws IOException, InvalidResponseException, BadResponseException {
        List<MenuItem> menuItems = AdminRepository.getMenuItems();
        AdminConsoleManager.displayMenuItems(menuItems);
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
        try {
            MenuItem menuItem = getUpdatedMenuItem();
            if (menuItem != null) {
                adminConsoleManager.displayMessage(adminRepository.updateMenuItem(menuItem));
            }
        } catch (InvalidChoiceException e) {
            adminConsoleManager.displayMessage(e.getMessage());
        }
    }

    private MenuItem getUpdatedMenuItem() throws InvalidChoiceException, IOException, InvalidResponseException {
        String menuItemName = adminConsoleManager.takeUserStringInput("Enter the name of the food item you want to update: ");
        try {
            MenuItem menuItem = AdminRepository.getFoodItemByName(menuItemName);
            adminConsoleManager.displayMessage("Food item found: " + menuItem.getName());
            adminConsoleManager.displayMenuItemUpdateOptions();
            int option = adminConsoleManager.takeUserChoice("Choose an option to update (1/2/3/4/5/6/7/8): ");
            updateMenuItem(menuItem, option);
            return menuItem;
        } catch (BadResponseException e) {
            adminConsoleManager.displayMessage(e.getMessage());
            return null;
        }
    }

    private void updateMenuItem(MenuItem menuItem, int option) throws InvalidChoiceException {
        switch (option) {
            case 1 -> updatePrice(menuItem);
            case 2 -> updateAvailability(menuItem);
            case 3 -> updateLastTimePrepared(menuItem);
            case 4 -> updateMealType(menuItem);
            case 5 -> updateDietaryPreference(menuItem);
            case 6 -> updateSweetContentLevel(menuItem);
            case 7 -> updateSpiceContentLevel(menuItem);
            case 8 -> updateCuisineType(menuItem);
            default -> throw new InvalidChoiceException("Invalid Choice");
        }
    }

    private void updatePrice(MenuItem menuItem) {
        float newPrice = adminConsoleManager.takeUserFloatInput("Enter new price: ");
        menuItem.setPrice(newPrice);
    }

    private void updateAvailability(MenuItem menuItem) {
        boolean isAvailable = adminConsoleManager.takeUserBooleanInput("Enter availability (true/false): ");
        menuItem.setAvailable(isAvailable);
    }

    private void updateLastTimePrepared(MenuItem menuItem) {
        Date date = adminConsoleManager.takeUserDateInput("Enter new last time prepared (yyyy-MM-dd): ");
        menuItem.setLastTimePrepared(date);
    }

    private void updateMealType(MenuItem menuItem) {
        int mealTypeId = adminConsoleManager.takeMealTypeId();
        menuItem.setMealTypeId(mealTypeId);
    }

    private void updateDietaryPreference(MenuItem menuItem) {
        int menuItemTypeId = adminConsoleManager.takeDietaryPreferenceId();
        menuItem.setMenuItemTypeId(menuItemTypeId);
    }

    private void updateSweetContentLevel(MenuItem menuItem) {
        int sweetContentLevelId = adminConsoleManager.takeSweetLevelId();
        menuItem.setSweetContentLevelId(sweetContentLevelId);
    }

    private void updateSpiceContentLevel(MenuItem menuItem) {
        int spiceContentLevelId = adminConsoleManager.takeSpiceLevelId();
        menuItem.setSpiceContentLevelId(spiceContentLevelId);
    }

    private void updateCuisineType(MenuItem menuItem) {
        int cuisineTypeId = adminConsoleManager.takeCuisineId();
        menuItem.setCuisineTypeId(cuisineTypeId);
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