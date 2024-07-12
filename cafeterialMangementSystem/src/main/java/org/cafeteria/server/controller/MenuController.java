package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.deserializeData;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.serializeData;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.enums.NotificationTypeEnum;
import org.cafeteria.server.services.MenuService;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.IMenuService;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MenuController {
    private static IMenuService _menuService;
    private static INotificationService _notificationService;
    public MenuController() {
        _menuService = new MenuService();
        _notificationService = new NotificationService();
    }
    public String addMenuItem(@NotNull ParsedRequest request) throws SQLException, DuplicateEntryFoundException {
        MenuItem menuItem = deserializeData(request.getJsonData(), MenuItem.class);
        String response;
        if (_menuService.add(menuItem)) {
            Notification notification = new Notification(NotificationTypeEnum.ADD_MENU.ordinal()+1, menuItem.getName() + " added to the menu.", new Date());
            _notificationService.sendNotificationToAllEmployees(notification);
            response = createResponse(ResponseCode.OK, serializeData(menuItem.getName() + " Added successfully in the Menu."));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while adding Menu Item."));
        }
        return response;
    }

    public String deleteMenuItem(@NotNull ParsedRequest request) throws SQLException {
        MenuItem menuItem = deserializeData(request.getJsonData(), MenuItem.class);
        boolean isItemUpdated = _menuService.delete(menuItem);
        String response;
        if(isItemUpdated) {
            response = createResponse(ResponseCode.OK, serializeData(menuItem.getName() + " Deleted successfully from the Menu."));
            Notification notification = new Notification(NotificationTypeEnum.DELETE_MENU.ordinal()+1, menuItem.getName() + " deleted from the menu.", new Date());
            _notificationService.sendNotificationToAllEmployees(notification);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while deleting Menu Item."));
        }
        return response;
    }

    public String updateMenuItem(@NotNull ParsedRequest request) throws SQLException {
        MenuItem menuItem = deserializeData(request.getJsonData(), MenuItem.class);
        boolean isItemUpdated = _menuService.update(menuItem);
        String response;
        if(isItemUpdated) {
            response = createResponse(ResponseCode.OK, serializeData(menuItem.getName() + " Updated successfully in the Menu."));
            Notification notification = new Notification(NotificationTypeEnum.UPDATE_MENU.ordinal()+1, menuItem.getName() + " updated in the menu.", new Date());
            _notificationService.sendNotificationToAllEmployees(notification);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while updating Menu Item."));
        }
        return response;
    }

    public String ShowMenuItems() throws SQLException {
        List<MenuItem> menu = _menuService.getAll();
        String response;
        if (!menu.isEmpty()) {
            response = createResponse(ResponseCode.OK, serializeData(menu));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No menu Items exist in the Menu"));
        }
        return response;
    }

    public String getMenuItemByName(@NotNull ParsedRequest request) throws SQLException {
        String menuItemName = deserializeData(request.getJsonData(), String.class);
        MenuItem menuItem = _menuService.getByName(menuItemName);
        String response;
        if(menuItem != null) {
            response = createResponse(ResponseCode.OK, serializeData(menuItem));
        } else {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData("No Item Exist with name: " + menuItemName));
        }
        return response;
    }

    public String getMenuItemById(@NotNull ParsedRequest request) throws SQLException {
        Integer menuItemId = deserializeData(request.getJsonData(), Integer.class);
        MenuItem menuItem = _menuService.getById(menuItemId);
        String response;
        if(menuItem != null) {
            response = createResponse(ResponseCode.OK, serializeData(menuItem));
        } else {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData("No Such Item Exist with id: " + menuItemId));
        }
        return response;
    }
}