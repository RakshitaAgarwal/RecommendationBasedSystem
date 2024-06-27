package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.MenuItemUserVote;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.VotingService;
import org.cafeteria.server.services.interfaces.IVotingService;

import java.sql.SQLException;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class VotingController {
    private static IVotingService _votingService;

    public VotingController() {
        _votingService = new VotingService();
    }

    public String voteForNextDayMenu(@NotNull ParsedRequest request) throws SQLException {
        MenuItemUserVote userVote = deserializeData(request.getJsonData(), MenuItemUserVote.class);
        String response;
        try {
            if (_votingService.add(userVote)) {
                response = createResponse(ResponseCode.OK, null);
            } else {
                response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
            }
        } catch (DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData("User has already casted their vote for this meal."));
        }
        return response;
    }
}
