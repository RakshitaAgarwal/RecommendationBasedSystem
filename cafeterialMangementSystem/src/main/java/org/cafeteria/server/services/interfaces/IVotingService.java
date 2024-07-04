package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Vote;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public interface IVotingService extends ICrudService<Vote> {
    Map<Integer, Integer> getNextDayMenuOptionsVotes(Date dateTime) throws SQLException;
}