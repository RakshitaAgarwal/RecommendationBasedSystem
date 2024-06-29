package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.PreparedMenuItemService;
import org.cafeteria.server.services.interfaces.IPreparedMenuItemService;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.util.Utils.extractDate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PreparedMenuItemController {
    private static IPreparedMenuItemService _preparedMenuItemService;
    public PreparedMenuItemController() {
        _preparedMenuItemService = new PreparedMenuItemService();
    }
    public String updateDailyFoodMenu(@NotNull ParsedRequest request) throws SQLException {
        List<Integer> menuItemIds = deserializeList(request.getJsonData(), Integer.class);
        String response;
        try {
            if(_preparedMenuItemService.addPreparedMenuItems(menuItemIds)) {
                response = createResponse(ResponseCode.OK, serializeData("Final Menu Updated for " + extractDate(new Date())));
            } else {
                response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while updating Next Day Final Menu. Please Try Again."));
            }
        } catch (DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData(e.getMessage()));
        }
        return response;
    }
}