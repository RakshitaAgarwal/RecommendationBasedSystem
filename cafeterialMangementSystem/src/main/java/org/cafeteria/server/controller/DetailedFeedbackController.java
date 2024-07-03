package org.cafeteria.server.controller;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.Notification;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.enums.NotificationTypeEnum;
import org.cafeteria.server.services.DetailedFeedbackService;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.IDetailedFeedbackService;
import org.cafeteria.server.services.interfaces.INotificationService;

import java.sql.SQLException;
import java.util.Date;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class DetailedFeedbackController {
    private final IDetailedFeedbackService _detailedFeedbackService;
    private static INotificationService _notificationService;
    public DetailedFeedbackController() {
        _detailedFeedbackService = new DetailedFeedbackService();
        _notificationService = new NotificationService();
    }

    public String createDetailedFeedbackRequest(ParsedRequest request) throws SQLException {
        int menuItemId = deserializeData(request.getJsonData(), Integer.class);
        String response;
        try {
            if(_detailedFeedbackService.addDetailedFeedbackRequest(menuItemId)) {
                response = createResponse(ResponseCode.OK, serializeData("Detailed Feedback Request successfully generated for " + menuItemId + " menu Item ID."));
                Notification notification = new Notification(
                        NotificationTypeEnum.GET_DETAILED_FEEDBACK.ordinal() + 1,
                        "We are trying to improve your experience. Please Provide Detailed Feedback for " + menuItemId + " Menu Item Id.",
                        new Date());
                _notificationService.sendNotificationToAllEmployees(notification);
            } else {
                response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while generating detailed Feedback request"));
            }
        } catch (CustomExceptions.DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData("Detailed Feedback Request is already generated for " + menuItemId + " Menu Item ID."));
        }
        return response;
    }
}
