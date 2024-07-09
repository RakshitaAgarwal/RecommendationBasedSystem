package org.cafeteria.client.consoleManager;

import org.cafeteria.common.model.DiscardMenuItem;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.common.model.enums.MealTypeEnum;

import java.util.List;
import java.util.Map;

public class ChefConsoleManager extends UserConsoleManager{

    public static void displayUserActionItems() {
        System.out.println("1. Show Menu");
        System.out.println("2. Roll Out Items for Next Day Menu");
        System.out.println("3. See Voting for Rolled out Menu Items");
        System.out.println("4. Update Next Day final Menu Items");
        System.out.println("5. See Discarded Menu Items");
        System.out.println("6. Logout");
        System.out.println("7. Exit");
    }

    public static void displayVoting(Map<MealTypeEnum, List<String>> categorizedVotes) {
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

    public static void displayDiscardedMenuItems(List<DiscardMenuItem> items) {
        System.out.println("---------------------------------------------------------");
        System.out.printf("%-5s | %-10s | %-10s%n", "ID", "MenuItemID", "Avg. Rating");
        System.out.println("---------------------------------------------------------");
        for (DiscardMenuItem item : items) {
            System.out.printf("%-5d | %-10d | %-10.2f%n",
                    item.getId(), item.getMenuItemId(), item.getAvgRating());
        }
        System.out.println("----------------------------------------------------------");
    }

    public static void displayDiscardMenuItemActions() {
        System.out.println("Choose action to perform:");
        System.out.println("1. Remove the Food Item from Menu");
        System.out.println("2. Get Detailed Feedback");
    }

    public static void displayMenuItemsRecommendationByMealType(Map<MealTypeEnum, List<MenuItemRecommendation>> menuItemsByMealType) {
        displayMessage("Menu Item Recommendations");
        for (Map.Entry<MealTypeEnum, List<MenuItemRecommendation>> entry : menuItemsByMealType.entrySet()) {
            MealTypeEnum mealType = entry.getKey();
            List<MenuItemRecommendation> menuItems = entry.getValue();

            System.out.println("Meal Type: " + mealType);
            System.out.println("-------------------------------------");
            for (MenuItemRecommendation menuItemRecommendation : menuItems) {
                String formattedScore = String.format("%.6f", menuItemRecommendation.getRecommendationScore());
                String sentiment = menuItemRecommendation.getSentimentOfItem();
                String menuItemInfo = String.format("ID: %d, Score: %s, Sentiment: %s",
                        menuItemRecommendation.getMenuItemId(), formattedScore, sentiment);
                System.out.println(menuItemInfo);
            }
            System.out.println("-----------------------------------------");
        }
    }
}