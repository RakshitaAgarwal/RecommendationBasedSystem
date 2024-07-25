package org.cafeteria.server.helper;

import org.cafeteria.common.model.UserAction;
import org.cafeteria.server.controller.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodRegistry {
    private static final Map<UserAction, Method> methodMap = new HashMap<>();

    static {
        registerMethods(UserController.class);
        registerMethods(MenuController.class);
        registerMethods(DetailedFeedbackController.class);
        registerMethods(DiscardMenuItemController.class);
        registerMethods(FeedbackController.class);
        registerMethods(NotificationController.class);
        registerMethods(PreparedMenuItemController.class);
        registerMethods(RecommendationController.class);
        registerMethods(RolledOutMenuItemController.class);
        registerMethods(VotingController.class);
    }

    private static <T> void registerMethods(Class<T> controllerClass) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(UserActionHandler.class)) {
                UserActionHandler annotation = method.getAnnotation(UserActionHandler.class);
                methodMap.put(annotation.value(), method);
            }
        }
    }

    public static Method getMethod(UserAction action) {
        return methodMap.get(action);
    }
}