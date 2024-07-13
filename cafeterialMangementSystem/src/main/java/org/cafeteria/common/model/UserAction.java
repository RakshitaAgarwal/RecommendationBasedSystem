package org.cafeteria.common.model;

public enum UserAction {
    LOGIN,
    LOGOUT,
    ADD_USER_PROFILE,
    UPDATE_USER_PROFILE,
    GET_USER_PROFILE,
    ADD_MENU_ITEM,
    DELETE_MENU_ITEM,
    UPDATE_MENU_ITEM,
    GET_MENU_ITEM_BY_NAME,
    GET_MENU_ITEM_BY_ID,
    GET_ALL_MENU_ITEMS,
    GET_DISCARD_MENU_ITEMS,
    CREATE_DETAILED_FEEDBACK_REQUEST,
    GET_DETAILED_FEEDBACK_REQUESTS,
    ADD_DETAILED_FEEDBACKS,
    GET_NEXT_DAY_MENU_RECOMMENDATIONS,
    GET_MENU_ITEM_RECOMMENDATION_SCORE,
    ROLL_OUT_NEXT_DAY_MENU_OPTIONS,
    GET_NEXT_DAY_MENU_OPTIONS,
    UPDATE_NEXT_DAY_FINAL_MENU,
    ADD_FEEDBACK,
    SEND_NOTIFICATION_TO_EMPLOYEES,
    GET_USER_NOTIFICATIONS,
    UPDATE_NOTIFICATIONS_READ_STATUS,
    VOTE_NEXT_DAY_MENU,
    GET_NEXT_DAY_MENU_VOTING
}