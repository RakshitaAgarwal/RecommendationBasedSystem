package org.cafeteria.client;

import org.cafeteria.common.model.UserAction;
import org.cafeteria.common.model.UserRole;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class RoleActionMapping {

    private static final EnumMap<UserRole, Set<UserAction>> roleActionMap = new EnumMap<>(UserRole.class);

    static {
        roleActionMap.put(UserRole.ADMIN, EnumSet.of(
                UserAction.LOGIN,
                UserAction.CREATE_USER,
                UserAction.ADD_MENU_ITEM,
                UserAction.DELETE_MENU_ITEM,
                UserAction.UPDATE_MENU_ITEM,
                UserAction.SHOW_MENU
        ));

        roleActionMap.put(UserRole.CHEF, EnumSet.of(
                UserAction.LOGIN,
                UserAction.CREATE_USER,
                UserAction.SHOW_MENU,
                UserAction.SEE_MONTHLY_REPORT,
                UserAction.PROVIDE_NEXT_DAY_MENU_OPTIONS
        ));

        roleActionMap.put(UserRole.EMP, EnumSet.of(
                UserAction.LOGIN,
                UserAction.CREATE_USER,
                UserAction.SEE_NOTIFICATIONS,
                UserAction.SHOW_MENU,
                UserAction.VOTE_NEXT_DAY_MENU,
                UserAction.PROVIDE_FEEDBACK
        ));
    }

    public static Set<UserAction> getActionsForRole(UserRole role) {
        return roleActionMap.getOrDefault(role, EnumSet.noneOf(UserAction.class));
    }
}