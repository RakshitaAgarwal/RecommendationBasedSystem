package org.cafeteria.server.helper;

import org.cafeteria.common.model.UserAction;
import org.cafeteria.server.controller.BaseController;

import java.util.HashMap;
import java.util.Map;

import static org.cafeteria.server.Server.*;

public class ControllerRegistry {
    private static final Map<UserAction, BaseController> controllerMap = new HashMap<>();

    static {
        controllerMap.put(UserAction.LOGIN, userController);
        controllerMap.put(UserAction.LOGOUT, userController);
        controllerMap.put(UserAction.ADD_USER_PROFILE, userController);
        controllerMap.put(UserAction.UPDATE_USER_PROFILE, userController);
        controllerMap.put(UserAction.GET_USER_PROFILE, userController);

        controllerMap.put(UserAction.ADD_MENU_ITEM, menuController);
        controllerMap.put(UserAction.DELETE_MENU_ITEM, menuController);
        controllerMap.put(UserAction.UPDATE_MENU_ITEM, menuController);
        controllerMap.put(UserAction.GET_ALL_MENU_ITEMS, menuController);
        controllerMap.put(UserAction.GET_MENU_ITEM_BY_NAME, menuController);
        controllerMap.put(UserAction.GET_MENU_ITEM_BY_ID, menuController);

        controllerMap.put(UserAction.GET_DISCARD_MENU_ITEMS, discardMenuItemController);

        controllerMap.put(UserAction.CREATE_DETAILED_FEEDBACK_REQUEST, detailedFeedbackController);
        controllerMap.put(UserAction.GET_DETAILED_FEEDBACK_REQUESTS, detailedFeedbackController);
        controllerMap.put(UserAction.ADD_DETAILED_FEEDBACKS, detailedFeedbackController);

        controllerMap.put(UserAction.ADD_FEEDBACK, feedbackController);

        controllerMap.put(UserAction.SEND_NOTIFICATION_TO_EMPLOYEES, notificationController);
        controllerMap.put(UserAction.GET_USER_NOTIFICATIONS, notificationController);
        controllerMap.put(UserAction.UPDATE_NOTIFICATIONS_READ_STATUS, notificationController);

        controllerMap.put(UserAction.VOTE_NEXT_DAY_MENU, votingController);
        controllerMap.put(UserAction.GET_NEXT_DAY_MENU_VOTING, votingController);

        controllerMap.put(UserAction.GET_MENU_ITEM_RECOMMENDATION_SCORE, recommendationController);
        controllerMap.put(UserAction.GET_NEXT_DAY_MENU_RECOMMENDATIONS, recommendationController);

        controllerMap.put(UserAction.ROLL_OUT_NEXT_DAY_MENU_OPTIONS, rolledOutMenuItemController);
        controllerMap.put(UserAction.GET_NEXT_DAY_MENU_OPTIONS, rolledOutMenuItemController);

        controllerMap.put(UserAction.UPDATE_NEXT_DAY_FINAL_MENU, preparedMenuItemController);
    }

    public static BaseController getController(UserAction action) {
        return controllerMap.get(action);
    }
}