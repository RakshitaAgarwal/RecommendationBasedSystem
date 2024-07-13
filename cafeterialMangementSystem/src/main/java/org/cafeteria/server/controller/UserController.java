package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.*;
import org.cafeteria.server.helper.UserActionHandler;
import org.cafeteria.server.network.SessionManager;
import org.cafeteria.server.services.UserProfileService;
import org.cafeteria.server.services.UserService;
import org.cafeteria.server.services.interfaces.IUserProfileService;
import org.cafeteria.server.services.interfaces.IUserService;

import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.createResponse;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.*;

public class UserController extends BaseController {
    private static IUserService _userService;
    private static IUserProfileService _userProfileService;

    public UserController() {
        _userService = new UserService();
        _userProfileService = new UserProfileService();
    }

    @UserActionHandler(UserAction.LOGIN)
    public String handleUserLogin(@NotNull ParsedRequest request, SessionManager sessionManager) throws SQLException {
        User user = deserializeData(request.getJsonData(), User.class);
        User loggedInUser = _userService.loginUser(user);
        String response;
        if (loggedInUser != null) {
            response = createResponse(ResponseCode.OK, serializeData(loggedInUser));
            createUserSession(sessionManager, loggedInUser);
        } else {
            response = createResponse(ResponseCode.UNAUTHORIZED, serializeData("No such user exist."));
        }
        return response;
    }

    @UserActionHandler(UserAction.LOGOUT)
    public String handleUserLogout(@NotNull ParsedRequest request, SessionManager sessionManager) {
        String response;
        if (sessionManager.endUserSession()) {
            response = createResponse(ResponseCode.OK, serializeData("Successfully Logged out"));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Error Logging out User"));
        }
        return response;
    }

    private void createUserSession(SessionManager sessionManager, User user) throws SQLException {
        boolean isRetry;
        do {
            isRetry = !sessionManager.createUserSession(user.getId());
        } while (isRetry);
    }

    @UserActionHandler(UserAction.ADD_USER_PROFILE)
    public String addUserProfile(@NotNull ParsedRequest request) throws SQLException {
        UserProfile userProfile = deserializeData(request.getJsonData(), UserProfile.class);
        String response;
        if (_userProfileService.add(userProfile)) {
            response = createResponse(ResponseCode.OK, serializeData("User Profile Successfully created for " + userProfile.getUserId() + " user."));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, "Some Error Occurred while creating User Profile");
        }
        return response;
    }

    @UserActionHandler(UserAction.UPDATE_USER_PROFILE)
    public String updateUserProfile(@NotNull ParsedRequest request) throws SQLException {
        UserProfile userProfile = deserializeData(request.getJsonData(), UserProfile.class);
        String response;
        if (_userProfileService.update(userProfile)) {
            response = createResponse(ResponseCode.OK, serializeData("User Profile Updated Successfully"));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No User Profile found for " + userProfile.getUserId() + " user."));
        }
        return response;
    }

    @UserActionHandler(UserAction.GET_USER_PROFILE)
    public String getUserProfile(@NotNull ParsedRequest request) throws SQLException {
        int userId = deserializeData(request.getJsonData(), Integer.class);
        String response;
        UserProfile userProfile = _userProfileService.getByUserId(userId);
        if (userProfile != null) {
            response = createResponse(ResponseCode.OK, serializeData(userProfile));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No User Profile found for " + userId + " user."));
        }
        return response;
    }
}