package org.cafeteria.client.services;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.ChefRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.MealTypeEnum;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.common.model.User;
import static org.cafeteria.client.repositories.AdminRepository.getMenuItemById;
import static org.cafeteria.client.services.AdminService.handleDisplayMenu;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.io.IOException;
import java.util.*;

public class ChefService extends UserManager {
    private static ChefRepository chefRepository;

    public ChefService(ServerConnection connection, User user, Scanner sc) {
        super(user, sc);
        chefRepository = new ChefRepository(connection);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. Roll Out Items for Next Day Menu");
            System.out.println("3. See Voting for Rolled out Menu Items");
            System.out.println("4. Update Next Day final Menu Items");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> handleDisplayMenu();
                    case 2 -> handleRollOutNextDayMenuOptions();
                    case 3 -> seeVotingForRolledOutItems();
                    case 4 -> handleUpdateNextDayFinalMenu();
                    case 5 -> {
                        chefRepository.closeConnection();
                        return;
                    }
                }
            } catch (BadResponseException | InvalidResponseException e) {
                System.out.println(e.getMessage());
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
        List<Integer> rolledOutItems = new ArrayList<>();
        if (recommendedItems == null || recommendedItems.isEmpty()) {
            rollOutRandomItems();
        } else {
            displayMenuItemsRecommendationByMealType(recommendedItems);
            rolledOutItems = getTopRolledOutMenuItemIds(recommendedItems, 5);
        }
        if (!rolledOutItems.isEmpty()) {
            System.out.println(chefRepository.processRollOutMenuOptions(rolledOutItems));
        }
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
}