package org.cafeteria.server.controller;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.*;
import org.cafeteria.common.model.enums.NotificationTypeEnum;
import org.cafeteria.common.model.DetailedFeedback;
import org.cafeteria.server.services.DetailedFeedbackService;
import org.cafeteria.server.services.NotificationService;
import org.cafeteria.server.services.interfaces.IDetailedFeedbackService;
import org.cafeteria.server.services.interfaces.INotificationService;
import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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
        } catch (DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData(e.getMessage()));
        }
        return response;
    }

    public String addDetailedFeedbacks(ParsedRequest request) throws SQLException {
        List<DetailedFeedback> detailedFeedbacks = deserializeList(request.getJsonData(), DetailedFeedback.class);
        String response;
        if(_detailedFeedbackService.addDetailedFeedbacks(detailedFeedbacks)) {
            response = createResponse(ResponseCode.OK, serializeData("Detailed Feedback added successfully"));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred in adding detailed feedback."));
        }
        return response;
    }

    public String getDetailedFeedbackRequests() throws SQLException {
        List<DetailedFeedbackRequest> detailedFeedbackRequests = _detailedFeedbackService.getDetailedFeedbackRequests();
        String response;
        if(!detailedFeedbackRequests.isEmpty()) {
            response = createResponse(ResponseCode.OK, serializeData(detailedFeedbackRequests));
        } else {
            response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No Detailed Feedback Requests Found."));
        }
        return response;
    }
}