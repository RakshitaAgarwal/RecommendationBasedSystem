package org.cafeteria.server.controller;

import org.cafeteria.common.model.DiscardMenuItem;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.DiscardMenuItemService;
import org.cafeteria.server.services.interfaces.IDiscardMenuItemService;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.createResponse;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.serializeData;

import java.sql.SQLException;
import java.util.List;

public class DiscardMenuItemController {
    private static IDiscardMenuItemService _discardMenuItemService;
    public DiscardMenuItemController() {
        _discardMenuItemService = new DiscardMenuItemService();
    }
    public String discardMenuItems() throws SQLException {
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