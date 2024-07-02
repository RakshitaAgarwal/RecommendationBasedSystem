package org.cafeteria.client.consoleManager;

import org.cafeteria.common.model.DiscardMenuItem;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.enums.ContentLevelEnum;
import org.cafeteria.common.model.enums.CuisineTypeEnum;
import org.cafeteria.common.model.enums.MealTypeEnum;
import org.cafeteria.common.model.enums.MenuItemTypeEnum;
import static org.cafeteria.common.util.Utils.extractDate;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AdminConsoleManager extends UserConsoleManager{
    public AdminConsoleManager(Scanner sc) {
        super(sc);
    }

    @Override
    public void displayUserActionItems() {
        System.out.println("1. Show Menu");
        System.out.println("2. Add Menu Item");
        System.out.println("3. Delete Menu Item");
        System.out.println("4. Update Menu Item");
        System.out.println("5. See Discarded Menu Items");
        System.out.println("6. Exit");
    }

    public void displayMenuItemUpdateOptions() {
        System.out.println("1. Update Price");
        System.out.println("2. Update Availability");
        System.out.println("3. Update Last Time Prepared");
        System.out.println("4. Update Meal Type");
        System.out.println("5. Update Menu Item Type");
        System.out.println("6. Update Sweet Content Level");
        System.out.println("7. Update Spice Content Level");
        System.out.println("8. Update Cuisine");
    }

    public void displayDiscardedMenuItems(List<DiscardMenuItem> items) {
        System.out.println("---------------------------------------------------------");
        System.out.printf("%-5s | %-10s | %-10s%n", "ID", "MenuItemID", "Avg. Rating");
        System.out.println("---------------------------------------------------------");
        for (DiscardMenuItem item : items) {
            System.out.printf("%-5d | %-10d | %-10.2f%n",
                    item.getId(), item.getMenuItemId(), item.getAvgRating());
        }
        System.out.println("----------------------------------------------------------");
    }

    public void displayDiscardMenuItemActions() {
        System.out.println("Choose action to perform:");
        System.out.println("1. Remove the Food Item from Menu");
        System.out.println("2. Get Detailed Feedback");
    }

    public static void displayMenuItems(List<MenuItem> menuItems) {
        System.out.println();
        System.out.println("------------------------Food Item Menu------------------------");

        Map<Integer, List<MenuItem>> menuItemsByMeal = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getMealTypeId));

        for (Map.Entry<Integer, List<MenuItem>> entry : menuItemsByMeal.entrySet()) {
            MealTypeEnum mealType = getEnumFromOrdinal(MealTypeEnum.class, entry.getKey());
            System.out.println("\nMeal Type: " + (mealType != null ? mealType : "Unknown"));

            Map<Integer, List<MenuItem>> menuItemsByType = entry.getValue().stream()
                    .collect(Collectors.groupingBy(MenuItem::getMenuItemTypeId));

            System.out.format("%-5s %-20s %-10s %-10s %-10s %-15s %-15s %-15s %-15s%n",
                    "ID", "Name", "Price (Rs.)", "Available", "Last Prepared", "Type", "Cuisine", "Sweet Level", "Spice Level");
            System.out.println("--------------------------------------------------------------------------------------------");
            for (List<MenuItem> items : menuItemsByType.values()) {
                for (MenuItem item : items) {
                    System.out.format("%-5d %-20s %-10.2f %-10s %-15s %-15s %-15s %-15s %-15s%n",
                            item.getId(),
                            item.getName(),
                            item.getPrice(),
                            (item.isAvailable() ? "Yes" : "No"),
                            (item.getLastTimePrepared() != null ? extractDate(item.getLastTimePrepared()) : "N/A"),
                            getEnumFromOrdinal(MenuItemTypeEnum.class, item.getMenuItemTypeId()),
                            getEnumFromOrdinal(CuisineTypeEnum.class, item.getCuisineTypeId()),
                            getEnumFromOrdinal(ContentLevelEnum.class, item.getSweetContentLevelId()),
                            getEnumFromOrdinal(ContentLevelEnum.class, item.getSpiceContentLevelId())
                    );
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------------------");
    }

    public MenuItem takeMenuItemFromUser() {
        String name = takeUserStringInput("Enter name of the food item to add in the menu:");
        float price = takeUserFloatInput("Enter price of the food item:");
        boolean isAvailable = takeUserBooleanInput("Enter availability of the food item (true/false):");
        int mealTypeId = takeMealTypeId();
        int menuItemTypeId = takeDietaryPreferenceId();
        int sweetContentLevelId = takeSweetLevelId();
        int spiceContentLevelId = takeSpiceLevelId();
        int cuisineTypeId = takeCuisineId();
        return new MenuItem(name, price, isAvailable, mealTypeId, menuItemTypeId, sweetContentLevelId, spiceContentLevelId, cuisineTypeId);
    }

    public int takeSpiceLevelId() {
        int spiceLevelId = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Please Select your Spice Level Preference:");
                for (ContentLevelEnum preference : ContentLevelEnum.values()) {
                    System.out.printf("%d. %s%n", preference.ordinal() + 1, preference.name().replace("_", " "));
                }

                int selection = takeUserChoice("");
                if (selection >= 1 && selection <= ContentLevelEnum.values().length) {
                    spiceLevelId = selection;
                    validInput = true;
                } else {
                    System.out.println("Invalid selection. Please choose a number between 1 and " + ContentLevelEnum.values().length + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }

        return spiceLevelId;
    }

    public int takeSweetLevelId() {
        int sweetLevelId = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Please Select Sweet Level Preference:");
                for (ContentLevelEnum preference : ContentLevelEnum.values()) {
                    System.out.printf("%d. %s%n", preference.ordinal() + 1, preference.name().replace("_", " "));
                }

                int selection = sc.nextInt();
                if (selection >= 1 && selection <= ContentLevelEnum.values().length) {
                    sweetLevelId = selection;
                    validInput = true;
                } else {
                    System.out.println("Invalid selection. Please choose a number between 1 and " + ContentLevelEnum.values().length + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }
        return sweetLevelId;
    }

    public int takeCuisineId() {
        int favCuisineId = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Please Select Cuisine:");
                for (CuisineTypeEnum preference : CuisineTypeEnum.values()) {
                    System.out.printf("%d. %s%n", preference.ordinal() + 1, preference.name().replace("_", " "));
                }

                int selection = sc.nextInt();
                if (selection >= 1 && selection <= CuisineTypeEnum.values().length) {
                    favCuisineId = selection;
                    validInput = true;
                } else {
                    System.out.println("Invalid selection. Please choose a number between 1 and " + CuisineTypeEnum.values().length + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }

        return favCuisineId;
    }

    public int takeDietaryPreferenceId() {
        int dietaryPreferenceId = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Please Select Dietary Preference:");
                for (MenuItemTypeEnum preference : MenuItemTypeEnum.values()) {
                    System.out.printf("%d. %s%n", preference.ordinal() + 1, preference.name().replace("_", " "));
                }

                int selection = sc.nextInt();
                if (selection >= 1 && selection <= MenuItemTypeEnum.values().length) {
                    dietaryPreferenceId = selection;
                    validInput = true;
                } else {
                    System.out.println("Invalid selection. Please choose a number between 1 and " + MenuItemTypeEnum.values().length + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }

        return dietaryPreferenceId;
    }

    public int takeMealTypeId() {
        int mealTypeId = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Please Select Meal Type:");
                for (MealTypeEnum preference : MealTypeEnum.values()) {
                    System.out.printf("%d. %s%n", preference.ordinal() + 1, preference.name().replace("_", " "));
                }

                int selection = sc.nextInt();
                if (selection >= 1 && selection <= MealTypeEnum.values().length) {
                    mealTypeId = selection;
                    validInput = true;
                } else {
                    System.out.println("Invalid selection. Please choose a number between 1 and " + MealTypeEnum.values().length + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }

        return mealTypeId;
    }
}