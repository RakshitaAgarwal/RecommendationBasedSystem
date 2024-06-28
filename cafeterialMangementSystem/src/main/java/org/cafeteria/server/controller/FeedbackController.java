package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.FeedbackService;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class FeedbackController {
    private static IFeedbackService _feedbackService;
    public FeedbackController() {
        _feedbackService = new FeedbackService();
    }

    public String addFeedback(@NotNull ParsedRequest request) throws SQLException, DuplicateEntryFoundException {
        Feedback feedback = deserializeData(request.getJsonData(), Feedback.class);
        String response;
        if(_feedbackService.add(feedback)) {
            response = createResponse(ResponseCode.OK, null);
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred"));
        }
        return response;
    }

    public String getFeedbackReport(@NotNull ParsedRequest request) throws SQLException {
        return "";
    }
}
