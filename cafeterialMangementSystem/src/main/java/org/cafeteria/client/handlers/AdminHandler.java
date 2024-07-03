package org.cafeteria.client.handlers;

import org.cafeteria.client.consoleManager.AdminConsoleManager;
import org.cafeteria.client.consoleManager.ChefConsoleManager;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.AdminRepository;
import org.cafeteria.common.customException.CustomExceptions.BadResponseException;
import org.cafeteria.common.customException.CustomExceptions.InvalidChoiceException;
import org.cafeteria.common.customException.CustomExceptions.InvalidResponseException;
import org.cafeteria.common.model.DiscardMenuItem;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.User;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AdminHandler extends UserHandler {
    private static AdminRepository adminRepository;

    public AdminHandler(ServerConnection connection, User user) {
        super(user);
        adminRepository = new AdminRepository(connection);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            AdminConsoleManager.displayUserActionItems();
            int choice = AdminConsoleManager.takeUserIntInput("Enter your Choice: ");
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
                AdminConsoleManager.displayMessage(e.getMessage());
            }
        }
    }

    public static void handleDisplayMenu() throws IOException, InvalidResponseException, BadResponseException {
        List<MenuItem> menuItems = AdminRepository.getMenuItems();
        AdminConsoleManager.displayMenuItems(menuItems);
    }

    public void handleAddMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        MenuItem menuItem = AdminConsoleManager.takeMenuItemFromUser();
        String response = adminRepository.addMenuItem(menuItem);
        AdminConsoleManager.displayMessage(response);
    }

    private void handleDeleteMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        String menuItemName = AdminConsoleManager.takeUserStringInput("Enter name of the food Item you want to delete from menu:");
        MenuItem menuItem = AdminRepository.getFoodItemByName(menuItemName);
        if (menuItem != null) {
            AdminConsoleManager.displayMessage(adminRepository.deleteMenuItem(menuItem));
        } else {
            AdminConsoleManager.displayMessage("No such food item exists in the menu");
        }
    }

    private void handleUpdateMenuItem() throws IOException, InvalidResponseException, BadResponseException {
        try {
            MenuItem menuItem = getUpdatedMenuItem();
            if (menuItem != null) {
                AdminConsoleManager.displayMessage(adminRepository.updateMenuItem(menuItem));
            }
        } catch (InvalidChoiceException e) {
            AdminConsoleManager.displayMessage(e.getMessage());
        }
    }

    private MenuItem getUpdatedMenuItem() throws InvalidChoiceException, IOException, InvalidResponseException {
        String menuItemName = AdminConsoleManager.takeUserStringInput("Enter the name of the food item you want to update: ");
        try {
            MenuItem menuItem = AdminRepository.getFoodItemByName(menuItemName);
            AdminConsoleManager.displayMessage("Food item found: " + menuItem.getName());
            AdminConsoleManager.displayMenuItemUpdateOptions();
            int option = AdminConsoleManager.takeUserIntInput("Choose an option to update (1/2/3/4/5/6/7/8): ");
            updateMenuItem(menuItem, option);
            return menuItem;
        } catch (BadResponseException e) {
            AdminConsoleManager.displayMessage(e.getMessage());
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
        float newPrice = AdminConsoleManager.takeUserFloatInput("Enter new price: ");
        menuItem.setPrice(newPrice);
    }

    private void updateAvailability(MenuItem menuItem) {
        boolean isAvailable = AdminConsoleManager.takeUserBooleanInput("Enter availability (true/false): ");
        menuItem.setAvailable(isAvailable);
    }

    private void updateLastTimePrepared(MenuItem menuItem) {
        Date date = AdminConsoleManager.takeUserDateInput("Enter new last time prepared (yyyy-MM-dd): ");
        menuItem.setLastTimePrepared(date);
    }

    private void updateMealType(MenuItem menuItem) {
        int mealTypeId = AdminConsoleManager.takeMealTypeId();
        menuItem.setMealTypeId(mealTypeId);
    }

    private void updateDietaryPreference(MenuItem menuItem) {
        int menuItemTypeId = AdminConsoleManager.takeDietaryPreferenceId();
        menuItem.setMenuItemTypeId(menuItemTypeId);
    }

    private void updateSweetContentLevel(MenuItem menuItem) {
        int sweetContentLevelId = AdminConsoleManager.takeSweetLevelId();
        menuItem.setSweetContentLevelId(sweetContentLevelId);
    }

    private void updateSpiceContentLevel(MenuItem menuItem) {
        int spiceContentLevelId = AdminConsoleManager.takeSpiceLevelId();
        menuItem.setSpiceContentLevelId(spiceContentLevelId);
    }

    private void updateCuisineType(MenuItem menuItem) {
        int cuisineTypeId = AdminConsoleManager.takeCuisineId();
        menuItem.setCuisineTypeId(cuisineTypeId);
    }

    public void handleDiscardMenuItems() throws IOException, BadResponseException, InvalidResponseException {
        List<DiscardMenuItem> discardedMenuItems = adminRepository.getDiscardMenuItems();
        boolean continueAction;
        do {
            AdminConsoleManager.displayDiscardedMenuItems(discardedMenuItems);
            int menuItemId = AdminConsoleManager.takeUserIntInput("Enter the Menu Item Id you want to perform action for:");
            MenuItem menuItem = AdminRepository.getMenuItemById(menuItemId);
            if (menuItem != null && isValidDiscardMenuItemId(menuItemId, discardedMenuItems)) {
                handleDiscardMenuItemAction(menuItem);
            } else {
                System.out.println("Invalid Discard Menu Item Id.");
            }
            continueAction = AdminConsoleManager.takeUserBooleanInput("Do you wish to Continue for other Discard Menu Item? true/false");
        } while (continueAction);
    }

    private boolean isValidDiscardMenuItemId(int menuItemId, List<DiscardMenuItem> discardedMenuItems) {
        for (DiscardMenuItem discardMenuItem : discardedMenuItems) {
            if (discardMenuItem.getMenuItemId() == menuItemId) return true;
        }
        return false;
    }

    private void handleDiscardMenuItemAction(MenuItem menuItem) throws BadResponseException, IOException, InvalidResponseException {
        AdminConsoleManager.displayDiscardMenuItemActions();
        int choice = AdminConsoleManager.takeUserIntInput("Enter Choice:");
        switch (choice) {
            case 1 -> {
                if (AdminConsoleManager.takeUserBooleanInput("Are you sure you want to permanently remove food item from menu. true/false"))
                    adminRepository.deleteMenuItem(menuItem);
            }
            case 2 -> handleGetDetailedFeedback(menuItem.getId());
            default -> AdminConsoleManager.displayMessage("Invalid choice selected");
        }
    }

    private void handleGetDetailedFeedback(int menuItemId) throws IOException, InvalidResponseException {
        String response;
        try {
            response = adminRepository.createDetailedFeedbackRequest(menuItemId);
        } catch (BadResponseException e) {
            response = e.getMessage();
        }
        ChefConsoleManager.displayMessage(response);
    }
}