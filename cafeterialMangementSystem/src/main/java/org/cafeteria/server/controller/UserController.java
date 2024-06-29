package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.User;
import org.cafeteria.server.services.UserService;
import org.cafeteria.server.services.interfaces.IUserService;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.createResponse;

import java.sql.SQLException;

public class UserController {
    private static IUserService _userService;
    public UserController() {
        _userService = new UserService();
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
}