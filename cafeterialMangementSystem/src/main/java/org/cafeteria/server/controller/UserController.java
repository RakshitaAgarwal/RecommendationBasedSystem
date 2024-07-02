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

    public String createUserProfile(@NotNull ParsedRequest request) throws SQLException {
        UserProfile userProfile = deserializeData(request.getJsonData(), UserProfile.class);
        String response ="";
        if(_userProfileService.add(userProfile)) {
            response = createResponse(ResponseCode.OK, serializeData(""));
        } else {
//            response = createResponse(ResponseCode.)
        }
        return response;
    }

    public String updateUserProfile(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }

    public String getUserProfile(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}