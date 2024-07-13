package org.cafeteria.server.helper;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.UserAction;
import org.cafeteria.server.controller.BaseController;
import org.cafeteria.server.network.SessionManager;

import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.createResponse;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.serializeData;

public class RequestHandler {
    public SessionManager sessionManager;

    public RequestHandler() {
        sessionManager = new SessionManager();
    }

    public String handleRequest(ParsedRequest request) {
        String response;
        try {
            BaseController controller = ControllerRegistry.getController(request.getUserAction());
            if (controller != null) {
                if (requiresSessionManager(request.getUserAction())) {
                    response = controller.handle(request, sessionManager);
                } else {
                    response = controller.handle(request);
                }
            } else {
                response = createResponse(ResponseCode.BAD_REQUEST, serializeData("Invalid action: " + request.getUserAction()));
            }
            handleUserSession(request);
        } catch (SQLException e) {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Error Occurred while processing your request."));
        } catch (CustomExceptions.BadRequestException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData(e.getMessage()));
        }
        return response;
    }

    private boolean requiresSessionManager(UserAction action) {
        return action == UserAction.LOGIN || action == UserAction.LOGOUT;
    }

    void handleUserSession(ParsedRequest request) {
        if (request.getUserAction() != UserAction.LOGOUT && request.getUserAction() != UserAction.LOGIN) {
            sessionManager.recordUserActivity(request.getUserAction().name());
        }
    }

    public void endUserSession() {
        sessionManager.endUserSession();
    }
}