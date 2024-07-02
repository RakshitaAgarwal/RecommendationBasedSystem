package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.User;
import org.cafeteria.common.model.UserProfile;
import org.cafeteria.server.services.UserProfileService;
import org.cafeteria.server.services.UserService;
import org.cafeteria.server.services.interfaces.IUserProfileService;
import org.cafeteria.server.services.interfaces.IUserService;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.createResponse;

import java.sql.SQLException;

public class UserController {
    private static IUserService _userService;
    private static IUserProfileService _userProfileService;
    public UserController() {
        _userService = new UserService();
        _userProfileService = new UserProfileService();
    }

    public String handleUserLogin(@NotNull ParsedRequest request) throws SQLException {
        User user = deserializeData(request.getJsonData(), User.class);
        User loggedInUser = _userService.loginUser(user);
        String response;
        if (loggedInUser != null) {
            response = createResponse(ResponseCode.OK, serializeData(loggedInUser));
        } else {
            response = createResponse(ResponseCode.UNAUTHORIZED, serializeData("No such user exist."));
        }
        return response;
    }

    public String addUserProfile(@NotNull ParsedRequest request) throws SQLException {
        UserProfile userProfile = deserializeData(request.getJsonData(), UserProfile.class);
        String response;
        if(_userProfileService.add(userProfile)) {
            response = createResponse(ResponseCode.OK, serializeData("User Profile Successfully created for " + userProfile.getUserId() + " user."));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, "Some Error Occurred while creating User Profile");
        }
        return response;
    }

    public String updateUserProfile(@NotNull ParsedRequest request) throws SQLException {
        UserProfile userProfile = deserializeData(request.getJsonData(), UserProfile.class);
        String response;
        if(_userProfileService.update(userProfile)) {
            response = createResponse(ResponseCode.OK, serializeData("User Profile Updated Successfully"));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No User Profile found for " + userProfile.getUserId() + " user."));
        }
        return response;
    }

    public String getUserProfile(@NotNull ParsedRequest request) throws SQLException {
        int userId = deserializeData(request.getJsonData(), Integer.class);
        String response;
        UserProfile userProfile = _userProfileService.getByUserId(userId);
        if(userProfile != null) {
            response = createResponse(ResponseCode.OK, serializeData(userProfile));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No User Profile found for " + userId + " user."));
        }
        return response;
    }
}