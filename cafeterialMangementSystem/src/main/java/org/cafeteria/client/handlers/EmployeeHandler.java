package org.cafeteria.client.handlers;

import org.cafeteria.client.consoleManager.EmployeeConsoleManager;
import org.cafeteria.client.global.GlobalData;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.EmployeeRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.enums.MealTypeEnum;

import static org.cafeteria.client.handlers.AdminHandler.handleDisplayMenu;
import static org.cafeteria.client.repositories.AdminRepository.getFoodItemByName;
import static org.cafeteria.client.repositories.AdminRepository.getMenuItemById;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
                    default -> throw new InvalidChoiceException("Invalid Choice");
                }
            } catch (InvalidResponseException | BadResponseException | InvalidChoiceException e) {
                employeeConsoleManager.displayMessage(e.getMessage());
            }
        }
    }

    private void handleDisplayUserProfile() throws IOException, InvalidResponseException {
        UserProfile userProfile;
        try {
            userProfile = employeeRepository.getUserProfile(user.getId());
            EmployeeConsoleManager.displayEmployeeProfile(userProfile);
        } catch (EmptyResponseException e) {
            employeeConsoleManager.displayMessage(e.getMessage());
        }
    }

    private void handleCreateUpdateUserProfile() throws IOException, InvalidResponseException, BadResponseException {
        UserProfile userProfile;
        try {
            userProfile = employeeRepository.getUserProfile(user.getId());
            if (employeeConsoleManager.takeUserChoice("Your User Profile already exists. Do you want to update it. Press 1 - Yes.") == 1) {
                processUpdateUserProfile(userProfile);
            }
        } catch (EmptyResponseException e) {
            employeeConsoleManager.displayMessage("Your User Profile does not exist. Please create your User Profile.");
            createUserProfile();
        }
    }

    private void processUpdateUserProfile(UserProfile userProfile) throws BadResponseException, IOException, InvalidResponseException {
        try {
            updateUserProfile(userProfile);
            String response = employeeRepository.updateUserProfile(userProfile);
            employeeConsoleManager.displayMessage(response);
        } catch (InvalidChoiceException e) {
            employeeConsoleManager.displayMessage(e.getMessage());
        }
    }

    private void updateUserProfile(UserProfile userProfile) throws InvalidChoiceException {
        employeeConsoleManager.displayUserProfileUpdateOptions();
        int option = employeeConsoleManager.takeUserChoice("Choose an option to update: ");
        switch (option) {
            case 1 -> {
                int dietaryPreferenceId = employeeConsoleManager.takeDietaryPreferenceId();
                userProfile.setDietaryPreferenceId(dietaryPreferenceId);
            }
            case 2 -> {
                int spiceLevelId = employeeConsoleManager.takeSpiceLevelId();
                userProfile.setSpiceLevelId(spiceLevelId);
            }
            case 3 -> {
                int favCuisineId = employeeConsoleManager.takeFavCuisineId();
                userProfile.setFavCuisineId(favCuisineId);
            }
            case 4 -> {
                boolean isSweetTooth = employeeConsoleManager.takeIsSweetTooth();
                userProfile.setSweetTooth(isSweetTooth);
            }
            default -> throw new InvalidChoiceException("Invalid Choice");
        }
    }

    private void createUserProfile() throws BadResponseException, IOException, InvalidResponseException {
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
        String response = employeeRepository.addUserProfile(userProfile);
        employeeConsoleManager.displayMessage(response);
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
            Map<Integer, String> rolledOutItemsForMealType = getRolledOutItemsForMealType(rolledOutItems, mealTypeId);
            UserProfile userProfile;
            try {
                userProfile = employeeRepository.getUserProfile(user.getId());
                Map<Integer, MenuItem> menuItemMap = getMenuItemMap(rolledOutItemsForMealType);
                if (userProfile != null)
                    rolledOutItemsForMealType = sortRolledOutItemsForMealType(rolledOutItemsForMealType, userProfile, menuItemMap);
            } catch (EmptyResponseException e) {
                System.out.println(e.getMessage() + "Getting default order of menu options. ");
            }
            employeeConsoleManager.displayMessage(getEnumFromOrdinal(MealTypeEnum.class, mealTypeId).name() + " Menu Options");
            processVotingForMealType(rolledOutItemsForMealType);
            continueVoting = employeeConsoleManager.takeUserChoice("Do you wish to cast vote for another Meal Type? Enter 1-Yes / 0-No: ");
        } while (continueVoting == 1);
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

    private Map<Integer, MenuItem> getMenuItemMap(Map<Integer, String> rolledOutItemsForMealType) throws BadResponseException, IOException, InvalidResponseException {
        Map<Integer, MenuItem> menuItemMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : rolledOutItemsForMealType.entrySet()) {
            Integer menuItemId = entry.getKey();
            menuItemMap.put(menuItemId, getMenuItemById(menuItemId));
        }
        return menuItemMap;
    }

    private Map<Integer, String> sortRolledOutItemsForMealType(
            Map<Integer, String> rolledOutItemsForMealType,
            UserProfile userProfile,
            Map<Integer, MenuItem> menuItemMap) {

        Map<Integer, String> sortedEntries = rolledOutItemsForMealType.entrySet()
                .stream()
                .sorted(new MenuItemComparator(userProfile, menuItemMap))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedEntries;
    }

    private void processVotingForMealType(Map<Integer, String> rolledOutItemsMap) throws BadResponseException, IOException, InvalidResponseException {
        if (rolledOutItemsMap.isEmpty())
            employeeConsoleManager.displayMessage("No Items Rolled out yet. Please come back later");
        else {
            employeeConsoleManager.displayRolledOutMenuItems(rolledOutItemsMap);
            int menuItemId = employeeConsoleManager.takeUserChoice("Enter the Menu Item Id you want to vote for: ");

            if (rolledOutItemsMap.containsKey(menuItemId)) {
                Vote userVote = new Vote(menuItemId, user.getId(), new Date());
                employeeConsoleManager.displayMessage(employeeRepository.voteForMenuItem(userVote));
            } else {
                employeeConsoleManager.displayMessage("Invalid selection");
            }
        }
    }

    private String formatRecommendation(MenuItem menuItem, MenuItemRecommendation recommendation) {
        String formattedScore = String.format("%.6f", recommendation.getRecommendationScore());
        return "Menu Item Name: " + menuItem.getName() + " | Score: " + formattedScore + " | Key Phrases: " + recommendation.getSentimentOfItem();
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