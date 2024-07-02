package org.cafeteria.client.handlers;

import org.cafeteria.client.consoleManager.ChefConsoleManager;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.AdminRepository;
import org.cafeteria.client.repositories.ChefRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.enums.MealTypeEnum;
import org.cafeteria.common.model.enums.NotificationTypeEnum;
import static org.cafeteria.client.repositories.AdminRepository.getMenuItemById;
import static org.cafeteria.client.handlers.AdminHandler.handleDisplayMenu;
import static org.cafeteria.client.repositories.AdminRepository.sendNotificationToAllEmployees;
import static org.cafeteria.common.constants.Constants.DETAILED_FEEDBACK_MESSAGE;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.io.IOException;
import java.util.*;

public class ChefHandler extends UserHandler {
    private static ChefRepository chefRepository;
    private static AdminRepository adminRepository;
    private static ChefConsoleManager chefConsoleManager;

    public ChefHandler(ServerConnection connection, User user, Scanner sc) {
        super(user);
        chefRepository = new ChefRepository(connection);
        adminRepository = new AdminRepository(connection);
        chefConsoleManager = new ChefConsoleManager(sc);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            chefConsoleManager.displayUserActionItems();
            int choice = chefConsoleManager.takeUserChoice("Enter your Choice: ");
            try {
                switch (choice) {
                    case 1 -> handleDisplayMenu();
                    case 2 -> handleRollOutNextDayMenuOptions();
                    case 3 -> handleDisplayVotingForRolledOutItems();
                    case 4 -> handleUpdateNextDayFinalMenu();
                    case 5 -> handleDiscardMenuItems();
                    case 6 -> {
                        chefRepository.closeConnection();
                        return;
                    }
                    default -> throw new InvalidChoiceException("Invalid choice");
                }
            } catch (BadResponseException | InvalidResponseException | InvalidChoiceException e) {
                chefConsoleManager.displayMessage(e.getMessage());
            }
        }
    }

    public void handleRollOutNextDayMenuOptions() throws IOException, InvalidResponseException, BadResponseException {
        Map<MealTypeEnum, List<MenuItemRecommendation>> recommendedItems = chefRepository.getRecommendationsForNextDayMenu();
        chefConsoleManager.displayMenuItemsRecommendationByMealType(recommendedItems);
        List<Integer> rolledOutItems = getTopRolledOutMenuItemIds(recommendedItems, 5);
        if (!rolledOutItems.isEmpty()) {
            String response = chefRepository.processRollOutMenuOptions(rolledOutItems);
            chefConsoleManager.displayMessage(response);
        }
    }

    private List<Integer> getTopRolledOutMenuItemIds(Map<MealTypeEnum, List<MenuItemRecommendation>> menuItemScoresMap, int topCount) {
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

    private void handleDisplayVotingForRolledOutItems() throws IOException, BadResponseException, InvalidResponseException {
        Map<Integer, Integer> nextDayVoting = chefRepository.getVotingForMenuItem();
        Map<MealTypeEnum, List<String>> categorizedVoting = categorizeVotesByMealType(nextDayVoting);
        chefConsoleManager.displayVoting(categorizedVoting);
    }

    private Map<MealTypeEnum, List<String>> categorizeVotesByMealType(Map<Integer, Integer> nextDayVoting) throws IOException, InvalidResponseException, BadResponseException {
        Map<MealTypeEnum, List<String>> categorizedVotes = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : nextDayVoting.entrySet()) {
            int menuItemId = entry.getKey();
            MenuItem menuItem = getMenuItemById(menuItemId);
            MealTypeEnum mealType = getEnumFromOrdinal(MealTypeEnum.class, menuItem.getMealTypeId());

            int votes = entry.getValue();

            String menuItemWithVotes = "MenuItemId: " + menuItemId + ", Votes: " + votes;
            categorizedVotes.computeIfAbsent(mealType, k -> new ArrayList<>());
            categorizedVotes.get(mealType).add(menuItemWithVotes);
        }

        return categorizedVotes;
    }

    private void handleUpdateNextDayFinalMenu() throws IOException, InvalidResponseException, BadResponseException {
        List<Integer> preparedMenuItemIds = new ArrayList<>();
        for (MealTypeEnum mealType : MealTypeEnum.values()) {
            if (!updateMealTypeMenu(mealType, preparedMenuItemIds)) {
                chefConsoleManager.displayMessage("No Item selected for " + mealType.name());
                break;
            }
        }
        finalizeMenu(preparedMenuItemIds);
    }

    private boolean updateMealTypeMenu(MealTypeEnum mealType, List<Integer> preparedMenuItemIds) throws IOException, InvalidResponseException, BadResponseException {
        boolean isSuccess = false;
        do {
            int menuItemId = chefConsoleManager.takeUserChoice("Enter Menu Item Id prepared for " + mealType.name());
            MenuItem menuItem = getMenuItemById(menuItemId);
            if (isValidMenuItem(menuItem, mealType)) {
                preparedMenuItemIds.add(menuItemId);
                isSuccess = true;
            } else {
                int continueUpdating = chefConsoleManager.takeUserChoice("Invalid Menu Item Id Entered. Do you want to try again (1. Yes / 0. No)");
                if (continueUpdating != 1) {
                    break;
                }
            }
        } while (!isSuccess);
        return isSuccess;
    }

    private boolean isValidMenuItem(MenuItem menuItem, MealTypeEnum mealType) {
        return menuItem != null && menuItem.getMealTypeId() == mealType.ordinal();
    }

    private void finalizeMenu(List<Integer> preparedMenuItemIds) throws IOException, InvalidResponseException, BadResponseException {
        if (preparedMenuItemIds.size() == MealTypeEnum.values().length) {
            String response = chefRepository.processUpdatingFinalMenu(preparedMenuItemIds);
            chefConsoleManager.displayMessage(response);
        } else {
            chefConsoleManager.displayMessage("Invalid Input!!. Please Try again later.");
        }
    }

    public void handleDiscardMenuItems() throws IOException, BadResponseException, InvalidResponseException {
        List<DiscardMenuItem> discardedMenuItems = adminRepository.getDiscardMenuItems();
        boolean continueAction;
        do {
            chefConsoleManager.displayDiscardedMenuItems(discardedMenuItems);
            int menuItemId = chefConsoleManager.takeUserChoice("Enter the Menu Item Id you want to perform action for:");
            MenuItem menuItem = AdminRepository.getMenuItemById(menuItemId);
            if (menuItem != null && isValidDiscardMenuItemId(menuItemId, discardedMenuItems)) {
                handleDiscardMenuItemAction(menuItem);
            } else {
                chefConsoleManager.displayMessage("Invalid Discard Menu Item Id.");
            }
            continueAction = chefConsoleManager.takeUserBooleanInput("Do you wish to Continue for other Discard Menu Item? true/false");
        } while (continueAction);
    }

    private boolean isValidDiscardMenuItemId(int menuItemId, List<DiscardMenuItem> discardedMenuItems) {
        for (DiscardMenuItem discardMenuItem : discardedMenuItems) {
            if (discardMenuItem.getMenuItemId() == menuItemId) return true;
        }
        return false;
    }

    private void handleDiscardMenuItemAction(MenuItem menuItem) throws BadResponseException, IOException, InvalidResponseException {
        chefConsoleManager.displayDiscardMenuItemActions();
        int choice = chefConsoleManager.takeUserChoice("Enter Choice:");
        switch (choice) {
            case 1 -> {
                if (chefConsoleManager.takeUserBooleanInput("Are you sure you want to permanently remove food item from menu. true/false"))
                    adminRepository.deleteMenuItem(menuItem);
            }
            case 2 -> handleGetDetailedFeedback(menuItem.getName());
            default -> chefConsoleManager.displayMessage("Invalid choice selected");
        }
    }

    private void handleGetDetailedFeedback(String menuItemName) throws IOException, InvalidResponseException, BadResponseException {
        String notificationMessage = String.format(DETAILED_FEEDBACK_MESSAGE, menuItemName, menuItemName, menuItemName);
        Notification notification = new Notification(
                NotificationTypeEnum.GET_DETAILED_FEEDBACK.ordinal() + 1,
                notificationMessage,
                new Date());
        chefConsoleManager.displayMessage(sendNotificationToAllEmployees(notification));
    }
}