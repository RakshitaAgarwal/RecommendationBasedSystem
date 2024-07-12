package org.cafeteria.client.repositories;

import org.cafeteria.client.network.ServerConnection;
import org.cafeteria.common.customException.CustomExceptions.*;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.DetailedFeedback;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.*;

import java.io.IOException;
import java.util.List;

public class EmployeeRepository extends UserRepository {
    public EmployeeRepository(ServerConnection connection) {
        super(connection);
    }

    public MenuItemRecommendation getRecommendationScoreForMenuItem(int menuItemId) throws InvalidResponseException, IOException, BadResponseException {
        String request = createRequest(UserAction.GET_MENU_ITEM_RECOMMENDATION_SCORE, serializeData(menuItemId));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeData(parsedResponse.getJsonData(), MenuItemRecommendation.class);
        } else throw new BadResponseException("Some Error Occurred while getting Rolled Out Items!!");
    }

    public List<RolledOutMenuItem> getRolledOutMenuItems() throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.GET_NEXT_DAY_MENU_OPTIONS, null);
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeList(parsedResponse.getJsonData(), RolledOutMenuItem.class);
        } else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String voteForMenuItem(Vote userVote) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.VOTE_NEXT_DAY_MENU, serializeData(userVote));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else if (responseCode == ResponseCode.BAD_REQUEST)
            return (deserializeData(parsedResponse.getJsonData(), String.class));
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String provideFeedback(Feedback feedback) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.PROVIDE_FEEDBACK, serializeData(feedback));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public List<Notification> getNotifications(User user) throws IOException, InvalidResponseException, EmptyResponseException {
        String request = createRequest(UserAction.GET_NOTIFICATIONS, serializeData(user));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeList(parsedResponse.getJsonData(), Notification.class);
        } else throw new EmptyResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String updateNotificationReadStatus(List<Notification> notifications) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.UPDATE_NOTIFICATIONS_READ_STATUS, serializeData(notifications));
        String response = connection.sendData(request);
        if(response == null)
            throw new IOException("Server got Disconnected. Please Try again.");
        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String addUserProfile(UserProfile userProfile) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.ADD_USER_PROFILE, serializeData(userProfile));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public UserProfile getUserProfile(int userId) throws IOException, InvalidResponseException, EmptyResponseException {
        String request = createRequest(UserAction.GET_USER_PROFILE, serializeData(userId));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), UserProfile.class);
        else
            throw new EmptyResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String updateUserProfile(UserProfile userProfile) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.UPDATE_USER_PROFILE, serializeData(userProfile));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public List<DetailedFeedbackRequest> getDetailedFeedbackRequest() throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.GET_DETAILED_FEEDBACK_REQUESTS, null);
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK) {
            return deserializeList(parsedResponse.getJsonData(), DetailedFeedbackRequest.class);
        } else throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }

    public String addDetailedFeedbacks(List<DetailedFeedback> detailedFeedbacks) throws IOException, InvalidResponseException, BadResponseException {
        String request = createRequest(UserAction.ADD_DETAILED_FEEDBACKS, serializeData(detailedFeedbacks));
        String response = connection.sendData(request);
        if (response == null)
            throw new IOException("Server Got Disconnected. Please Try again.");

        ParsedResponse parsedResponse = parseResponse(response);
        ResponseCode responseCode = parsedResponse.getResponseCode();
        if (responseCode == ResponseCode.OK)
            return deserializeData(parsedResponse.getJsonData(), String.class);
        else
            throw new BadResponseException(deserializeData(parsedResponse.getJsonData(), String.class));
    }
}