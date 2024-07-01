package org.cafeteria.common.model;

public enum UserAction {
    LOGIN,
    CREATE_USER,
    CREATE_USER_PROFILE,
    UPDATE_USER_PROFILE,
    GET_USER_PROFILE,
    ADD_MENU_ITEM,
    DELETE_MENU_ITEM,
    UPDATE_MENU_ITEM,
    GET_MENU_ITEM_BY_NAME,
    GET_MENU_ITEM_BY_ID,
    GET_ALL_MENU_ITEMS,
    GET_DISCARD_MENU_ITEMS,
    GET_RECOMMENDATION_FOR_NEXT_DAY_MENU,
    GET_MENU_ITEM_RECOMMENDATION_SCORE,
    ROLL_OUT_NEXT_DAY_MENU_OPTIONS,
    GET_NEXT_DAY_MENU_OPTIONS,
    UPDATE_NEXT_DAY_FINAL_MENU,
    PROVIDE_FEEDBACK,
    SEND_NOTIFICATION_TO_EMPLOYEES,
    SEE_NOTIFICATIONS,
    VOTE_NEXT_DAY_MENU,
    GET_VOTING_FOR_NEXT_DAY_MENU
}