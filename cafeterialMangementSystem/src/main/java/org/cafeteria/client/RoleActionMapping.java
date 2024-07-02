package org.cafeteria.client;

import org.cafeteria.common.model.UserAction;
import org.cafeteria.common.model.enums.UserRoleEnum;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class RoleActionMapping {

    private static final EnumMap<UserRoleEnum, Set<UserAction>> roleActionMap = new EnumMap<>(UserRoleEnum.class);

    static {
        roleActionMap.put(UserRoleEnum.ADMIN, EnumSet.of(
                UserAction.LOGIN,
                UserAction.CREATE_USER,
                UserAction.ADD_MENU_ITEM,
                UserAction.DELETE_MENU_ITEM,
                UserAction.UPDATE_MENU_ITEM,
                UserAction.GET_ALL_MENU_ITEMS
        ));

        roleActionMap.put(UserRoleEnum.CHEF, EnumSet.of(
                UserAction.LOGIN,
                UserAction.CREATE_USER,
                UserAction.GET_ALL_MENU_ITEMS,
                UserAction.GET_DISCARD_MENU_ITEMS,
                UserAction.ROLL_OUT_NEXT_DAY_MENU_OPTIONS
        ));

        roleActionMap.put(UserRoleEnum.EMP, EnumSet.of(
                UserAction.LOGIN,
                UserAction.CREATE_USER,
                UserAction.GET_NOTIFICATIONS,
                UserAction.GET_ALL_MENU_ITEMS,
                UserAction.VOTE_NEXT_DAY_MENU,
                UserAction.PROVIDE_FEEDBACK
        ));
    }

    public static Set<UserAction> getActionsForRole(UserRoleEnum role) {
        return roleActionMap.getOrDefault(role, EnumSet.noneOf(UserAction.class));
    }
}