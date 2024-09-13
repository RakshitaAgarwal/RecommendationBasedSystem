package org.cafeteria.client.handlers;

import org.cafeteria.client.consoleManager.EmployeeConsoleManager;
import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.client.repositories.EmployeeRepository;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.enums.DetailedFeedbackQuestionEnum;
import org.cafeteria.common.model.enums.MealTypeEnum;
import org.cafeteria.common.model.DetailedFeedback;

import static org.cafeteria.client.consoleManager.UserConsoleManager.displayMessage;
import static org.cafeteria.client.handlers.AdminHandler.handleDisplayMenu;
import static org.cafeteria.client.repositories.AdminRepository.getFoodItemByName;
import static org.cafeteria.client.repositories.AdminRepository.getMenuItemById;
import static org.cafeteria.common.util.Utils.getEnumFromOrdinal;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeHandler extends UserHandler {
    private static EmployeeRepository employeeRepository;

    public EmployeeHandler(ServerConnection connection, User user) {
        super(user, connection);
        employeeRepository = new EmployeeRepository(connection);
    }

    @Override
    public boolean showUserActionItems() throws IOException {
        while (true) {
            EmployeeConsoleManager.displayUserActionItems();
            int choice = EmployeeConsoleManager.takeUserIntInput("Enter your Choice: ");
            try {
                switch (choice) {
                    case 1 -> handleDisplayMenu();
                    case 2 -> handleDisplayNotifications();
                    case 3 -> handleNextDayMealVoting();
                    case 4 -> handleProvideFeedback();
                    case 5 -> handleCreateUpdateUserProfile();
                    case 6 -> handleDisplayUserProfile();
                    case 7 -> handleProvideDetailedFeedback();
                    case 8 -> {
                        return handleLogout();
                    }
                    case 9 -> {
                        employeeRepository.closeConnection();
                        return false;
                    }
                    default -> throw new InvalidChoiceException("Invalid Choice");
                }
            } catch (InvalidResponseException | BadResponseException | InvalidChoiceException e) {
                displayMessage(e.getMessage());
            }
        }
    }

    private void handleDisplayNotifications() throws IOException, InvalidResponseException {
        try {
            List<Notification> notifications = getNotifications();
            EmployeeConsoleManager.displayEmployeeNotifications(notifications);
            updateNotificationsReadStatus(notifications);
        } catch (EmptyResponseException e) {
            displayMessage(e.getMessage());
        }
    }

    public List<Notification> getNotifications() throws IOException, InvalidResponseException, EmptyResponseException {
        return employeeRepository.getNotifications(user);
    }

    public void updateNotificationsReadStatus(List<Notification> notifications) throws IOException, InvalidResponseException {
        for (Notification notification:
             notifications) {
            notification.setNotificationRead(true);
        }
        try {
            String response = employeeRepository.updateNotificationReadStatus(notifications);
            displayMessage(response);
        } catch (BadResponseException e) {
            displayMessage(e.getMessage());
        }
    }

    private void handleDisplayUserProfile() throws IOException, InvalidResponseException {
        UserProfile userProfile;
        try {
            userProfile = employeeRepository.getUserProfile(user.getId());
            EmployeeConsoleManager.displayEmployeeProfile(userProfile);
        } catch (EmptyResponseException e) {
            displayMessage(e.getMessage());
        }
    }

    private void handleCreateUpdateUserProfile() throws IOException, InvalidResponseException, BadResponseException {
        UserProfile userProfile;
        try {
            userProfile = employeeRepository.getUserProfile(user.getId());
            if (EmployeeConsoleManager.takeUserIntInput("Your User Profile already exists. Do you want to update it. Press 1 - Yes.") == 1) {
                processUpdateUserProfile(userProfile);
            }
        } catch (EmptyResponseException e) {
            displayMessage("Your User Profile does not exist. Please create your User Profile.");
            createUserProfile();
        }
    }

    private void processUpdateUserProfile(UserProfile userProfile) throws BadResponseException, IOException, InvalidResponseException {
        try {
            updateUserProfile(userProfile);
            String response = employeeRepository.updateUserProfile(userProfile);
            displayMessage(response);
        } catch (InvalidChoiceException e) {
            displayMessage(e.getMessage());
        }
    }

    private void updateUserProfile(UserProfile userProfile) throws InvalidChoiceException {
        EmployeeConsoleManager.displayUserProfileUpdateOptions();
        int option = EmployeeConsoleManager.takeUserIntInput("Choose an option to update: ");
        switch (option) {
            case 1 -> {
                int dietaryPreferenceId = EmployeeConsoleManager.takeDietaryPreferenceId();
                userProfile.setDietaryPreferenceId(dietaryPreferenceId);
            }
            case 2 -> {
                int spiceLevelId = EmployeeConsoleManager.takeSpiceLevelId();
                userProfile.setSpiceLevelId(spiceLevelId);
            }
            case 3 -> {
                int favCuisineId = EmployeeConsoleManager.takeFavCuisineId();
                userProfile.setFavCuisineId(favCuisineId);
            }
            case 4 -> {
                boolean isSweetTooth = EmployeeConsoleManager.takeUserBooleanInput("Are you a Sweet Tooth?");
                userProfile.setSweetTooth(isSweetTooth);
            }
            default -> throw new InvalidChoiceException("Invalid Choice");
        }
    }

    private void createUserProfile() throws BadResponseException, IOException, InvalidResponseException {
        int dietaryPreferenceId = EmployeeConsoleManager.takeDietaryPreferenceId();
        int spiceLevelId = EmployeeConsoleManager.takeSpiceLevelId();
        int favCuisineId = EmployeeConsoleManager.takeFavCuisineId();
        boolean isSweetTooth = EmployeeConsoleManager.takeUserBooleanInput("Are you a Sweet Tooth?");
        UserProfile userProfile = new UserProfile(
                user.getId(),
                dietaryPreferenceId,
                spiceLevelId,
                favCuisineId,
                isSweetTooth
        );
        String response = employeeRepository.addUserProfile(userProfile);
        displayMessage(response);
    }

    public void handleProvideFeedback() throws IOException, InvalidResponseException, BadResponseException {
        displayMessage(employeeRepository.provideFeedback(getUserFeedback()));
    }

    public void handleNextDayMealVoting() throws IOException, InvalidResponseException, BadResponseException {
        List<RolledOutMenuItem> rolledOutItems = employeeRepository.getRolledOutMenuItems();
        int continueVoting;
        do {
            int mealTypeId = EmployeeConsoleManager.takeMealTypeId();
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
            processVotingForMealType(rolledOutItemsForMealType, getEnumFromOrdinal(MealTypeEnum.class, mealTypeId).name());
            continueVoting = EmployeeConsoleManager.takeUserIntInput("Do you wish to cast vote for another Meal Type? Enter 1-Yes / 0-No: ");
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

        return rolledOutItemsForMealType.entrySet()
                .stream()
                .sorted(new MenuItemComparator(userProfile, menuItemMap))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private void processVotingForMealType(Map<Integer, String> rolledOutItemsMap, String mealType) throws BadResponseException, IOException, InvalidResponseException {
        if (rolledOutItemsMap.isEmpty())
            displayMessage("No Items Rolled out yet. Please come back later");
        else {
            displayMessage(mealType + " Menu Options");
            EmployeeConsoleManager.displayRolledOutMenuItems(rolledOutItemsMap);
            int menuItemId = EmployeeConsoleManager.takeUserIntInput("Enter the Menu Item Id you want to vote for: ");

            if (rolledOutItemsMap.containsKey(menuItemId)) {
                Vote userVote = new Vote(menuItemId, user.getId(), new Date());
                displayMessage(employeeRepository.voteForMenuItem(userVote));
            } else {
                displayMessage("Invalid selection");
            }
        }
    }

    private String formatRecommendation(MenuItem menuItem, MenuItemRecommendation recommendation) {
        String formattedScore = String.format("%.6f", recommendation.getRecommendationScore());
        return "Menu Item Name: " + menuItem.getName() + " | Score: " + formattedScore + " | Key Phrases: " + recommendation.getSentimentOfItem();
    }

    public Feedback getUserFeedback() throws BadResponseException, IOException, InvalidResponseException {
        String foodItemName = EmployeeConsoleManager.takeUserStringInput("Enter the food item you want to provide feedback for:");
        MenuItem menuItem = getFoodItemByName(foodItemName);
        if (menuItem != null) {
            Feedback feedback = EmployeeConsoleManager.takeEmployeeFeedback(foodItemName);
            feedback.setMenuItemId(menuItem.getId());
            feedback.setUserId(user.getId());
            feedback.setDateTime(new Date());
            return feedback;
        } else {
            displayMessage("No Such food item exists in the menu");
        }
        return null;
    }

    private void handleProvideDetailedFeedback() throws IOException, InvalidResponseException {
        try {
            List<DetailedFeedbackRequest> detailedFeedbackRequests = employeeRepository.getDetailedFeedbackRequest();
            EmployeeConsoleManager.displayDetailedFeedbackRequests(detailedFeedbackRequests);

            int inputDetailedFeedbackId = EmployeeConsoleManager.takeUserIntInput("Choose Id to provide feedback: ");
            DetailedFeedbackRequest feedbackRequest = getDetailedFeedbackRequest(inputDetailedFeedbackId, detailedFeedbackRequests);
            if (feedbackRequest != null) {
                List<DetailedFeedback> detailedFeedbacks = getDetailedFeedback(feedbackRequest.getMenuItemId());
                displayMessage(employeeRepository.addDetailedFeedbacks(detailedFeedbacks));
            } else {
                displayMessage("Invalid Id Selected");
            }

        } catch (BadResponseException e) {
            displayMessage(e.getMessage());
        }
    }

    private DetailedFeedbackRequest getDetailedFeedbackRequest(int inputDetailedFeedbackId, List<DetailedFeedbackRequest> detailedFeedbackRequests) {
        for (DetailedFeedbackRequest feedbackRequest : detailedFeedbackRequests) {
            if (inputDetailedFeedbackId == feedbackRequest.getId())
                return feedbackRequest;
        }
        return null;
    }

    private List<DetailedFeedback> getDetailedFeedback(int menuItemId) {
        List<DetailedFeedback> detailedFeedbacks = new ArrayList<>();
        for (int i = 1; i <= DetailedFeedbackQuestionEnum.values().length; i++) {
            String answer = EmployeeConsoleManager.takeUserStringInput(
                    getEnumFromOrdinal(DetailedFeedbackQuestionEnum.class, i).toString()
            );
            DetailedFeedback detailedFeedback = new DetailedFeedback();
            detailedFeedback.setUserId(user.getId());
            detailedFeedback.setMenuItemId(menuItemId);
            detailedFeedback.setDateTime(new Date());
            detailedFeedback.setFeedbackQuestionId(i);
            detailedFeedback.setAnswer(answer);
            detailedFeedbacks.add(detailedFeedback);
        }
        return detailedFeedbacks;
    }
}