package org.cafeteria.client.handlers;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.AdminRepository;
import org.cafeteria.client.repositories.ChefRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;

import static org.cafeteria.client.repositories.AdminRepository.getMenuItemById;
import static org.cafeteria.client.handlers.AdminHandler.handleDisplayMenu;
import static org.cafeteria.client.repositories.AdminRepository.sendNotificationToAllEmployees;
import static org.cafeteria.common.constants.Constants.DETAILED_FEEDBACK_MESSAGE;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.io.IOException;
import java.util.*;

public class ChefHandler extends UserManager {
    private static ChefRepository chefRepository;
    private static AdminRepository adminRepository;

    public ChefHandler(ServerConnection connection, User user, Scanner sc) {
        super(user, sc);
        chefRepository = new ChefRepository(connection);
        adminRepository = new AdminRepository(connection);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. Roll Out Items for Next Day Menu");
            System.out.println("3. See Voting for Rolled out Menu Items");
            System.out.println("4. Update Next Day final Menu Items");
            System.out.println("5. See Discarded Menu Items");
            System.out.println("6. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> handleDisplayMenu();
                    case 2 -> handleRollOutNextDayMenuOptions();
                    case 3 -> seeVotingForRolledOutItems();
                    case 4 -> handleUpdateNextDayFinalMenu();
                    case 5 -> handleDiscardMenuItems();
                    case 6 -> {
                        chefRepository.closeConnection();
                        return;
                    }
                }
            } catch (BadResponseException | InvalidResponseException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                sc.next();
                System.out.println("Invalid Input Entered. Please Enter Valid Input");
            }
        }
    }

    private void seeVotingForRolledOutItems() throws IOException, BadResponseException, InvalidResponseException {
        Map<Integer, Integer> nextDayVoting = chefRepository.getVotingForMenuItem();
        Map<MealTypeEnum, List<String>> categorizedVoting = categorizeVotesByMealType(nextDayVoting);
        displayVoting(categorizedVoting);
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

    private void displayVoting(Map<MealTypeEnum, List<String>> categorizedVotes) {
        System.out.println();
        System.out.println("--------Votes For Next Day menu--------");
        for (Map.Entry<MealTypeEnum, List<String>> entry : categorizedVotes.entrySet()) {
            MealTypeEnum mealType = entry.getKey();
            List<String> menuItemsWithVotes = entry.getValue();

            System.out.println("Meal Type: " + mealType);

            for (String menuItemWithVotes : menuItemsWithVotes) {
                System.out.println("  " + menuItemWithVotes);
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");
    }

    public void handleRollOutNextDayMenuOptions() throws IOException, InvalidResponseException, BadResponseException {
        Map<MealTypeEnum, List<MenuItemRecommendation>> recommendedItems = chefRepository.getRecommendationsForNextDayMenu();
        displayMenuItemsRecommendationByMealType(recommendedItems);
        List<Integer> rolledOutItems = getTopRolledOutMenuItemIds(recommendedItems, 5);
        if (!rolledOutItems.isEmpty()) {
            System.out.println(chefRepository.processRollOutMenuOptions(rolledOutItems));
        }
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

    private void handleUpdateNextDayFinalMenu() throws IOException, InvalidResponseException, BadResponseException {
        List<Integer> preparedMenuItemIds = new ArrayList<>();
        for (MealTypeEnum mealType : MealTypeEnum.values()) {
            int isContinue = 0;
            boolean isSuccess = false;
            do {
                System.out.println("Enter Menu Item Id prepared for " + mealType.name());
                int menuItemId = sc.nextInt();
                MenuItem menuItem = getMenuItemById(menuItemId);
                if (menuItem != null && getEnumFromOrdinal(MealTypeEnum.class, menuItem.getMealTypeId()) == mealType) {
                    preparedMenuItemIds.add(menuItemId);
                    isSuccess = true;
                } else {
                    System.out.println("Invalid Menu Item Id Entered. Do you want to try again (1. Yes / 0. No");
                    isContinue = sc.nextInt();
                }
            } while (isContinue == 1);
            if (!isSuccess) {
                System.out.println("No Item selected for " + mealType.name());
                break;
            }
        }
        if (preparedMenuItemIds.size() == MealTypeEnum.values().length) {
            System.out.println(chefRepository.processUpdatingFinalMenu(preparedMenuItemIds));
        } else {
            System.out.println("Invalid Input!!. Please Try again later.");
        }
    }

    public void handleDiscardMenuItems() throws IOException {
        try {
            List<DiscardMenuItem> discardedMenuItems = chefRepository.getDiscardMenuItems();
            displayDiscardedMenuItems(discardedMenuItems);
            boolean isContinue;
            do {
                System.out.println("Enter the Menu Item Id you want to perform action for:");
                int menuItemId = sc.nextInt();
                MenuItem menuItem = getMenuItemById(menuItemId);
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
                if (sc.nextBoolean()) System.out.println(adminRepository.deleteMenuItem(menuItem));
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