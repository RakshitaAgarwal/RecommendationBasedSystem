package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.model.DiscardMenuItem;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.UserAction;
import org.cafeteria.server.helper.UserActionHandler;
import org.cafeteria.server.services.DiscardMenuItemService;
import org.cafeteria.server.services.interfaces.IDiscardMenuItemService;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.createResponse;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.serializeData;

import java.sql.SQLException;
import java.util.List;

public class DiscardMenuItemController extends BaseController {
    private static IDiscardMenuItemService _discardMenuItemService;

    public DiscardMenuItemController() {
        _discardMenuItemService = new DiscardMenuItemService();
    }

    @UserActionHandler(UserAction.GET_DISCARD_MENU_ITEMS)
    public String getDiscardMenuItems(@NotNull ParsedRequest request) throws SQLException {
        List<DiscardMenuItem> discardMenuItems = _discardMenuItemService.getAll();
        String response;
        if (!discardMenuItems.isEmpty()) {
            response = createResponse(ResponseCode.OK, serializeData(discardMenuItems));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No Menu Items are Discarded yet."));
        }
        return response;
    }
}