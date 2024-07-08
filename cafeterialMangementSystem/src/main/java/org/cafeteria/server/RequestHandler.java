package org.cafeteria.server;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.network.SessionManager;

import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.createResponse;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.serializeData;
import static org.cafeteria.server.Server.*;

public class RequestHandler {
    public SessionManager sessionManager;
    public RequestHandler() {
        sessionManager = new SessionManager();
    }
    public String handleRequest(ParsedRequest request) throws CustomExceptions.DuplicateEntryFoundException {
        String response = null;
        try {
            switch (request.getUserAction()) {
                case LOGIN -> response = userController.handleUserLogin(request, sessionManager);

                case ADD_USER_PROFILE -> response = userController.addUserProfile(request);

                case UPDATE_USER_PROFILE -> response = userController.updateUserProfile(request);

                case GET_USER_PROFILE -> response = userController.getUserProfile(request);

                case ADD_MENU_ITEM -> response = menuController.addMenuItem(request);

                case DELETE_MENU_ITEM -> response = menuController.deleteMenuItem(request);

                case UPDATE_MENU_ITEM -> response = menuController.updateMenuItem(request);

                case GET_ALL_MENU_ITEMS -> response = menuController.ShowMenuItems();

                case GET_MENU_ITEM_BY_NAME -> response = menuController.getMenuItemByName(request);

                case GET_MENU_ITEM_BY_ID -> response = menuController.getMenuItemById(request);

                case GET_DISCARD_MENU_ITEMS -> response = discardMenuItemController.discardMenuItems();

                case CREATE_DETAILED_FEEDBACK_REQUEST ->
                        response = detailedFeedbackController.createDetailedFeedbackRequest(request);

                case GET_DETAILED_FEEDBACK_REQUESTS -> response = detailedFeedbackController.getDetailedFeedbackRequests();

                case ADD_DETAILED_FEEDBACKS -> response = detailedFeedbackController.addDetailedFeedbacks(request);

                case PROVIDE_FEEDBACK -> response = feedbackController.addFeedback(request);

                case SEND_NOTIFICATION_TO_EMPLOYEES ->
                        response = notificationController.sendNotificationToAllEmployees(request);

                case GET_NOTIFICATIONS -> response = notificationController.getUserNotification(request);

                case VOTE_NEXT_DAY_MENU -> response = votingController.voteForNextDayMenu(request);

                case GET_VOTING_FOR_NEXT_DAY_MENU -> response = votingController.getVotingForNextDayMenu(request);

                case GET_MENU_ITEM_RECOMMENDATION_SCORE ->
                        response = recommendationController.getRecommendationScoreForMenuItem(request);

                case GET_RECOMMENDATION_FOR_NEXT_DAY_MENU ->
                        response = recommendationController.getRecommendationsForNextDayMenu();

                case ROLL_OUT_NEXT_DAY_MENU_OPTIONS ->
                        response = rolledOutMenuItemController.rollOutNextDayMenuOptions(request);

                case GET_NEXT_DAY_MENU_OPTIONS -> response = rolledOutMenuItemController.getNextDayMenuOptions();

                case UPDATE_NEXT_DAY_FINAL_MENU -> response = preparedMenuItemController.updateDailyFoodMenu(request);

            }
        } catch (SQLException e) {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Error Occurred while processing your request."));
        }
        return response;
    }

    public void endUserSession() {
        sessionManager.endUserSession();
    }
}