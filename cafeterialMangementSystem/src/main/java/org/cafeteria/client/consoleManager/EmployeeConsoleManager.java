package org.cafeteria.client.consoleManager;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.Notification;
import org.cafeteria.common.model.UserProfile;
import org.cafeteria.common.model.enums.ContentLevelEnum;
import org.cafeteria.common.model.enums.CuisineTypeEnum;
import org.cafeteria.common.model.enums.MealTypeEnum;
import org.cafeteria.common.model.enums.MenuItemTypeEnum;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

public class EmployeeConsoleManager extends UserConsoleManager{
    public EmployeeConsoleManager(Scanner sc) {
        super(sc);
    }

    @Override
    public void displayUserActionItems() {
        System.out.println("1. See Menu");
        System.out.println("2. See Notifications");
        System.out.println("3. Vote For Next Day Menu");
        System.out.println("4. Provide Feedback");
        System.out.println("5. Create/Update User Profile");
        System.out.println("6. See User Profile");
        System.out.println("7. Exit");
    }

    public void displayRolledOutMenuItems(Map<Integer, String> rolledOutItemsMap) {
        for (Map.Entry<Integer, String> entry : rolledOutItemsMap.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
    }

    public void displayEmployeeNotifications(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            System.out.println("No notifications to display.");
            return;
        }

        for (Notification notification : notifications) {
            System.out.println("-----------------------------------");
            System.out.println("Notification ID: " + notification.getId());
            System.out.println("User ID: " + notification.getUserId());
            System.out.println("Notification Type ID: " + notification.getNotificationTypeId());
            System.out.println("Notification Message: " + notification.getNotificationMessage());
            System.out.println("Date and Time: " + notification.getDateTime());
            System.out.println("Is Notification Read: " + (notification.getNotificationRead() ? "Yes" : "No"));
            System.out.println("-----------------------------------");
        }
    }

    public static void displayEmployeeProfile(UserProfile userProfile) {
        System.out.println("Employee Profile Details:");
        System.out.println("----------------------");
        System.out.println("User ID: " + userProfile.getUserId());
        System.out.println("Dietary Preference: " + getEnumFromOrdinal(MenuItemTypeEnum.class, userProfile.getDietaryPreferenceId()));
        System.out.println("Spice Level: " + getEnumFromOrdinal(ContentLevelEnum.class, userProfile.getSpiceLevelId()));
        System.out.println("Favorite Cuisine: " + getEnumFromOrdinal(CuisineTypeEnum.class, userProfile.getFavCuisineId()));
        System.out.println("Sweet Tooth: " + (userProfile.isSweetTooth() ? "Yes" : "No"));
        System.out.println("----------------------");
    }

    public void displayUserProfileUpdateOptions() {
        System.out.println("1. Update Dietary Preference");
        System.out.println("2. Update Spice level Preference");
        System.out.println("3. Update Cuisine Preference");
        System.out.println("4. Update Sweet Food Preference");
    }

    public Feedback takeEmployeeFeedback(String foodItemName) {
        String comment = takeUserStringInput("Enter Feedback/Comment for " + foodItemName);
        float rating = takeUserFloatInput("Enter Rating for the " + foodItemName + " out of 5");
        return new Feedback(comment, rating);
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

    public int takeFavCuisineId() {
        int favCuisineId = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Please Select your Favourite Cuisine:");
                for (CuisineTypeEnum preference : CuisineTypeEnum.values()) {
                    System.out.printf("%d. %s%n", preference.ordinal() + 1, preference.name().replace("_", " "));
                }

                int selection = takeUserChoice("");
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
                System.out.println("Please Select your Dietary Preference:");
                for (MenuItemTypeEnum preference : MenuItemTypeEnum.values()) {
                    System.out.printf("%d. %s%n", preference.ordinal() + 1, preference.name().replace("_", " "));
                }

                int selection = takeUserChoice("");
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

    public boolean takeIsSweetTooth() {
        boolean isSweetTooth = false;
        try{
            isSweetTooth = takeUserBooleanInput("Are you a Sweet Tooth (true/false)");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter either true/false.");
            sc.next();
        }
        return isSweetTooth;
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

                int selection = takeUserChoice("");
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