package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.UserAction;
import org.cafeteria.common.model.Vote;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.helper.UserActionHandler;
import org.cafeteria.server.services.VotingService;
import org.cafeteria.server.services.interfaces.IVotingService;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;
import static org.cafeteria.common.communicationProtocol.JSONSerializer.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class VotingController extends BaseController {
    private static IVotingService _votingService;

    public VotingController() {
        _votingService = new VotingService();
    }

    @UserActionHandler(UserAction.VOTE_NEXT_DAY_MENU)
    public String voteForNextDayMenu(@NotNull ParsedRequest request) throws SQLException {
        Vote userVote = deserializeData(request.getJsonData(), Vote.class);
        String response;
        try {
            if (_votingService.add(userVote)) {
                response = createResponse(ResponseCode.OK, serializeData("User Id: " + userVote.getUserId() + " vote successfully recorded."));
            } else {
                response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred. Please Try again."));
            }
        } catch (DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData("Vote Already Casted for this Category. Please come back tomorrow."));
        }
        return response;
    }

    @UserActionHandler(UserAction.GET_NEXT_DAY_MENU_VOTING)
    public String getNextDayMenuVoting(@NotNull ParsedRequest request) throws SQLException {
        Date date = deserializeData(request.getJsonData(), Date.class);
        Map<Integer, Integer> nextDayMenuOptionsVotes = _votingService.getNextDayMenuOptionsVotes(date);
        String response;
        if (nextDayMenuOptionsVotes != null) {
            if (nextDayMenuOptionsVotes.isEmpty()) {
                response = createResponse(ResponseCode.EMPTY_RESPONSE, serializeData("No Votes casted yet. Please come back later"));
            } else {
                response = createResponse(ResponseCode.OK, serializeMap(nextDayMenuOptionsVotes));
            }
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, serializeData("Some error occurred. Please try again."));
        }
        return response;
    }
}