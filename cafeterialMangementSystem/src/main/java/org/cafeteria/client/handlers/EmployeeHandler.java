package org.cafeteria.client.handlers;

import org.cafeteria.client.consoleManager.EmployeeConsoleManager;
import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.EmployeeRepository;
import org.cafeteria.common.customException.CustomExceptions.BadResponseException;
import org.cafeteria.common.customException.CustomExceptions.InvalidResponseException;
import org.cafeteria.common.model.*;
import static org.cafeteria.client.handlers.AdminHandler.handleDisplayMenu;
import static org.cafeteria.client.repositories.AdminRepository.getFoodItemByName;
import static org.cafeteria.client.repositories.AdminRepository.getMenuItemById;

import java.io.IOException;
import java.util.*;

public class EmployeeHandler extends UserHandler {
    private static EmployeeRepository employeeRepository;
    private static EmployeeConsoleManager employeeConsoleManager;

    public EmployeeHandler(ServerConnection connection, User user, Scanner sc) {
        super(user);
        employeeRepository = new EmployeeRepository(connection);
        employeeConsoleManager = new EmployeeConsoleManager(sc);
    }

    @Override
    public void showUserActionItems() throws IOException {
        while (true) {
            employeeConsoleManager.displayUserActionItems();
            int choice = employeeConsoleManager.takeUserChoice("Enter your Choice: ");
            try {
                switch (choice) {
                    case 1 -> handleDisplayMenu();
                    case 2 -> employeeConsoleManager.displayEmployeeNotifications(getNotifications());
                    case 3 -> handleNextDayMealVoting();
                    case 4 -> handleProvideFeedback();
                    case 5 -> handleCreateUpdateUserProfile();
                    case 6 -> handleDisplayUserProfile();
                    case 7 -> {
                        employeeRepository.closeConnection();
                        return;
                    }
                    default -> employeeConsoleManager.displayMessage("Invalid choice");
                }
            } catch (InvalidResponseException |BadResponseException e) {
                employeeConsoleManager.displayMessage(e.getMessage());
            }
        }
    }

    private void handleDisplayUserProfile() {
        UserProfile userProfile = null;
        employeeConsoleManager.displayEmployeeProfile(userProfile);
    }

    private void handleCreateUpdateUserProfile() {
        if(hasUserProfile(GlobalData.loggedInUser.getId())) {
            if(employeeConsoleManager.takeUserChoice("Your User Profile already exists. Do you want to update it. Press 1 - Yes.") == 1) {
                updateUserProfile();
            }
        } else {
            createUserProfile();
        }
    }

    private void updateUserProfile() {
    }

    private void createUserProfile() {
        int dietaryPreferenceId = employeeConsoleManager.takeDietaryPreferenceId();
        int spiceLevelId = employeeConsoleManager.takeSpiceLevelId();
        int favCuisineId = employeeConsoleManager.takeFavCuisineId();
        boolean isSweetTooth = employeeConsoleManager.takeIsSweetTooth();
        UserProfile userProfile = new UserProfile(
                GlobalData.loggedInUser.getId(),
                dietaryPreferenceId,
                spiceLevelId,
                favCuisineId,
                isSweetTooth
        );

    }

    private boolean hasUserProfile(int userId) {
        return false;
    }

    public void handleProvideFeedback() throws IOException, InvalidResponseException, BadResponseException {
        employeeConsoleManager.displayMessage(employeeRepository.provideFeedback(getUserFeedback()));
    }

    public List<Notification> getNotifications() throws BadResponseException, IOException, InvalidResponseException {
        return employeeRepository.getNotifications();
    }

    public void handleNextDayMealVoting() throws IOException, InvalidResponseException, BadResponseException {
        List<RolledOutMenuItem> rolledOutItems = employeeRepository.getRolledOutMenuItems();
        int continueVoting;
        do {
            int mealTypeId = employeeConsoleManager.takeMealTypeId();
            Map<Integer, String> rolledOutItemsMap = getRolledOutItemsForMealType(rolledOutItems, mealTypeId);
            processVotingForMealType(rolledOutItemsMap);
            continueVoting = employeeConsoleManager.takeUserChoice("Do you wish to cast vote for another Meal Type? Enter 1-Yes / 0-No.");
        } while (continueVoting == 1);
    }

    private void processVotingForMealType(Map<Integer, String> rolledOutItemsMap) throws BadResponseException, IOException, InvalidResponseException {
        if (rolledOutItemsMap.isEmpty())
            employeeConsoleManager.displayMessage("No Items Rolled out yet. Please come back later");
        else {
            employeeConsoleManager.displayRolledOutMenuItems(rolledOutItemsMap);
            int menuItemId = employeeConsoleManager.takeUserChoice("Enter the Menu Item Id you want to vote for:");

            if (rolledOutItemsMap.containsKey(menuItemId)) {
                Vote userVote = new Vote(menuItemId, user.getId(), new Date());
                employeeConsoleManager.displayMessage(employeeRepository.voteForMenuItem(userVote));
            } else {
                employeeConsoleManager.displayMessage("Invalid selection");
            }
        }
    }

    private Map<Integer, String> getRolledOutItemsForMealType(List<RolledOutMenuItem> rolledOutItems, int mealTypeId) throws IOException, InvalidResponseException, BadResponseException {
        Map<Integer, String> rolledOutItemsMap = new HashMap<>();
        for (RolledOutMenuItem rolledOutItem : rolledOutItems) {
            MenuItem menuItem = getMenuItemById(rolledOutItem.getMenuItemId());
            if (menuItem.getMealTypeId() == mealTypeId) {
                MenuItemRecommendation recommendation = employeeRepository.getRecommendationScoreForMenuItem(menuItem.getId());
                String recommendationScore = formatRecommendation(menuItem, recommendation);
                rolledOutItemsMap.put(menuItem.getId(), recommendationScore);
            }
        }
        return rolledOutItemsMap;
    }

    private String formatRecommendation(MenuItem menuItem, MenuItemRecommendation recommendation) {
        return menuItem.getName() + " " + recommendation.getRecommendationScore() + " " + recommendation.getSentimentOfItem();
    }

    public Feedback getUserFeedback() throws BadResponseException, IOException, InvalidResponseException {
        String foodItemName = employeeConsoleManager.takeUserStringInput("Enter the food item you want to provide feedback for:");
        MenuItem menuItem = getFoodItemByName(foodItemName);
        if (menuItem != null) {
            Feedback feedback = employeeConsoleManager.takeEmployeeFeedback(foodItemName);
            feedback.setMenuItemId(menuItem.getId());
            feedback.setUserId(GlobalData.loggedInUser.getId());
            feedback.setDateTime(new Date());
        } else {
            employeeConsoleManager.displayMessage("No Such food item exists in the menu");
        }
        return null;
    }
}