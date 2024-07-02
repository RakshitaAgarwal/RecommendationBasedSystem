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

    public void displayEmployeeProfile(UserProfile userProfile) {}

    public Feedback takeEmployeeFeedback(String foodItemName) {
        System.out.println("Enter Feedback/Comment for " + foodItemName);
        String comment = sc.nextLine();
        System.out.println("Enter Rating for the " + foodItemName + " out of 5");
        float rating = sc.nextFloat();
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

                int selection = sc.nextInt();
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
                System.out.println("Please Select your Dietary Preference:");
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

    public boolean takeIsSweetTooth() {
        boolean isSweetTooth = false;
        try{
            isSweetTooth = sc.nextBoolean();
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
                System.out.println("Please Select your Favourite Cuisine:");
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