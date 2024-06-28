package org.cafeteria.server.controller;

import com.sun.istack.NotNull;
import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.Vote;
import org.cafeteria.common.model.ParsedRequest;
import org.cafeteria.common.model.ResponseCode;
import org.cafeteria.server.services.VotingService;
import org.cafeteria.server.services.interfaces.IVotingService;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import static org.cafeteria.common.communicationProtocol.CustomProtocol.*;

public class VotingController {
    private static IVotingService _votingService;

    public VotingController() {
        _votingService = new VotingService();
    }

    public String voteForNextDayMenu(@NotNull ParsedRequest request) throws SQLException {
        Vote userVote = deserializeData(request.getJsonData(), Vote.class);
        String response;
        try {
            if (_votingService.add(userVote)) {
                response = createResponse(ResponseCode.OK, null);
            } else {
                response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR, null);
            }
        } catch (DuplicateEntryFoundException e) {
            response = createResponse(ResponseCode.BAD_REQUEST, serializeData("Vote Already Casted for this Category. Please come back tomorrow."));
        }
        return response;
    }

    public String getVotingForNextDayMenu(@NotNull ParsedRequest request) throws SQLException {
        Date date = deserializeData(request.getJsonData(), Date.class);
        Map<Integer, Integer> nextDayMenuOptionsVotes = _votingService.getNextDayMenuOptionsVotes(date);
        String response;
        if(nextDayMenuOptionsVotes!=null) {
            if(nextDayMenuOptionsVotes.isEmpty()) {
                response = createResponse(ResponseCode.EMPTY_RESPONSE ,null);
            } else {
                response = createResponse(ResponseCode.OK, serializeMap(nextDayMenuOptionsVotes));
            }
        } else {
            response = createResponse(ResponseCode.INTERNAL_SERVER_ERROR ,null);
        }
        return response;
    }
}