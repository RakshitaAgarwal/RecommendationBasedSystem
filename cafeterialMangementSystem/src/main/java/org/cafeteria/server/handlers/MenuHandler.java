package org.cafeteria.server.handlers;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.MenuService;
import org.cafeteria.server.services.interfaces.IMenuService;

import java.sql.SQLException;
import java.util.List;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class MenuHandler {
    private static IMenuService _menuService;
    public MenuHandler() {
        _menuService = new MenuService();
    }
    public String addMenuItem(@NotNull ParsedRequest request) throws SQLException {
        MenuItem menuItem = deserializeData(request.getJsonData(), MenuItem.class);
        String response;
        if (_menuService.add(menuItem)) {
            response = createResponse(ResponseCode.OK, null);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
        }
        return response;
    }

    public String deleteMenuItem(@NotNull ParsedRequest request) throws SQLException {
        MenuItem menuItem = deserializeData(request.getJsonData(), MenuItem.class);
        boolean isItemUpdated = _menuService.delete(menuItem);
        String response;
        if(isItemUpdated) {
            response = createResponse(ResponseCode.OK, null);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
        }
        return response;
    }

    public String updateMenuItem(@NotNull ParsedRequest request) throws SQLException {
        MenuItem menuItem = deserializeData(request.getJsonData(), MenuItem.class);
        boolean isItemUpdated = _menuService.update(menuItem);
        String response;
        if(isItemUpdated) {
            response = createResponse(ResponseCode.OK, null);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
        }
        return response;
    }

    public String ShowMenuItems(@NotNull ParsedRequest request) throws SQLException {
        List<MenuItem> menu = _menuService.getAll();
        String response;
        if (!menu.isEmpty()) {
            response = createResponse(ResponseCode.OK, serializeData(menu));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, null);
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
            response = createResponse(ResponseCode.BAD_REQUEST, null);
        }
        return response;
    }
}