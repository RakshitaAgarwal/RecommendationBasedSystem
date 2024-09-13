package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.common.model.UserAction;
import org.cafeteria.server.helper.UserActionHandler;
import org.cafeteria.server.services.FeedbackService;
import org.cafeteria.server.services.interfaces.IFeedbackService;

import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.deserializeData;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.serializeData;

public class FeedbackController extends BaseController {
    private static IFeedbackService _feedbackService;

    public FeedbackController() {
        _feedbackService = new FeedbackService();
    }

    @UserActionHandler(UserAction.ADD_FEEDBACK)
    public String addFeedback(@NotNull ParsedRequest request) throws SQLException, DuplicateEntryFoundException {
        Feedback feedback = deserializeData(request.getJsonData(), Feedback.class);
        String response;
        if (_feedbackService.add(feedback)) {
            response = createResponse(ResponseCode.OK, serializeData("Feedback Recorded successfully."));
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred while adding feedback. Please Try again."));
        }
        return response;
    }
}