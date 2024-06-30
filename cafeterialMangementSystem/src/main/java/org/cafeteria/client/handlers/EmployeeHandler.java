package org.cafeteria.client.handlers;

import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.EmployeeRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;

import static org.cafeteria.client.repositories.AdminRepository.getFoodItemByName;
import static org.cafeteria.client.repositories.AdminRepository.getMenuItemById;
import static org.cafeteria.client.handlers.AdminHandler.handleDisplayMenu;

import java.io.IOException;
import java.util.*;

public class EmployeeHandler extends UserManager {
    private static EmployeeRepository employeeRepository;

    public EmployeeHandler(ServerConnection connection, User user, Scanner sc) {
        super(user, sc);
        employeeRepository = new EmployeeRepository(connection);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            System.out.println("1. Show Menu");
            System.out.println("2. See Notifications");
            System.out.println("3. Vote For Next Day Menu");
            System.out.println("4. Provide Feedback");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> handleDisplayMenu();
                    case 2 -> displayUserNotifications(employeeRepository.seeNotifications());
                    case 3 -> handleNextDayMealVoting();
                    case 4 -> handleProvideFeedback();
                    case 5 -> {
                        employeeRepository.closeConnection();
                        return;
                    }
                }
            } catch (InvalidResponseException | BadResponseException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                sc.next();
                System.out.println("Invalid Input Entered. Please Enter Valid Input");
            }
        }
    }

    public void handleProvideFeedback() throws IOException, InvalidResponseException, BadResponseException {
        System.out.println(employeeRepository.provideFeedback(takeUserFeedback()));
    }

    public void displayUserNotifications(List<Notification> notifications) {
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

    public void handleNextDayMealVoting() throws IOException, InvalidResponseException, BadResponseException {
        List<RolledOutMenuItem> rolledOutItems = employeeRepository.getRolledOutMenuItems();
        int isContinue;
        do {
            System.out.println("\nSelect the Meal Type index you want to cast vote for (1. Lunch 2. Breakfast 3. Dinner):");
            int mealTypeId = sc.nextInt();

            Map<Integer, String> rolledOutItemsMap = new HashMap<>();

            for (RolledOutMenuItem rolledOutItem : rolledOutItems) {
                int menuItemId = rolledOutItem.getMenuItemId();
                MenuItem menuItem = getMenuItemById(menuItemId);
                if (menuItem.getMealTypeId() == mealTypeId) {
                    MenuItemRecommendation menuItemRecommendation = employeeRepository.getRecommendationScoreForMenuItem(menuItemId);

                    String menuItemRecommendationScore = menuItem.getName() + " " + menuItemRecommendation.getRecommendationScore() + " " + menuItemRecommendation.getSentimentOfItem();
                    rolledOutItemsMap.put(menuItemId, menuItemRecommendationScore);
                }
            }

            for (Map.Entry<Integer, String> entry : rolledOutItemsMap.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue());
            }
            if (rolledOutItemsMap.isEmpty())
                System.out.println("No Items Rolled out yet. Please come back later");
            else {
                System.out.println("Enter the index of the recommendation you want to vote for:");
                int selectedIndex = sc.nextInt();

                if (rolledOutItemsMap.containsKey(selectedIndex)) {
                    Vote userVote = new Vote(selectedIndex, GlobalData.loggedInUser.getId(), new Date());
                    System.out.println(employeeRepository.voteForMenuItem(userVote));
                } else {
                    System.out.println("Invalid selection");
                }
            }
            System.out.println("Do you wish to cast vote for another Meal Type? Enter 1-Yes / 0-No.");
            isContinue = sc.nextInt();
        } while (isContinue == 1);
    }


    private Feedback takeUserFeedback() throws IOException, InvalidResponseException, BadResponseException {
        Feedback feedback = new Feedback();
        System.out.println("Enter the food item you want to provide feedback for:");
        String foodItemName = sc.nextLine();
        MenuItem menuItem = getFoodItemByName(foodItemName);
        if (menuItem != null) {
            System.out.println("Enter Feedback/Comment for " + foodItemName);
            String comment = sc.nextLine();
            System.out.println("Enter Rating for the " + foodItemName + " out of 5");
            float rating = sc.nextFloat();
            feedback.setMenuItemId(menuItem.getId());
            feedback.setComment(comment);
            feedback.setRating(rating);
            feedback.setUserId(GlobalData.loggedInUser.getId());
            feedback.setDateTime(new Date());
            return feedback;
        } else {
            System.out.println("No Such food item exists in the menu");
        }
        return null;
    }
}