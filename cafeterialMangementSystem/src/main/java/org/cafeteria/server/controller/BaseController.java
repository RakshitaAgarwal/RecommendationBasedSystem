package org.cafeteria.server.controller;

import org.cafeteria.common.customException.CustomExceptions.BadRequestException;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.UserAction;
import org.cafeteria.server.helper.MethodRegistry;
import org.cafeteria.server.network.SessionManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

public abstract class BaseController {
    public String handle(ParsedRequest request, SessionManager sessionManager) throws SQLException, BadRequestException {
            try {
                return invokeAction(request.getUserAction(), request, sessionManager);
            } catch (Exception e) {
                throw new BadRequestException("Error invoking action: " + request.getUserAction());
            }
    }

    public String handle(ParsedRequest request) throws SQLException, BadRequestException {
        try {
            return invokeAction(request.getUserAction(), request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Error invoking action: "  + request.getUserAction());
        }
    }

    private String invokeAction(UserAction userAction, ParsedRequest request) throws InvocationTargetException, IllegalAccessException {
        Method method = MethodRegistry.getMethod(userAction);
        if (method != null) {
            return (String) method.invoke(this, request);
        } else {
            throw new UnsupportedOperationException("Action not supported: " + userAction);
        }
    }

    private String invokeAction(UserAction userAction, ParsedRequest request, SessionManager sessionManager) throws Exception {
        Method method = MethodRegistry.getMethod(userAction);
        if (method != null) {
            return (String) method.invoke(this, request, sessionManager);
        } else {
            throw new UnsupportedOperationException("Action not supported: " + userAction);
        }
    }
}