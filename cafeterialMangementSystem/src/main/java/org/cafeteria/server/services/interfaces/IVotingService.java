package org.cafeteria.server.services.interfaces;

import org.cafeteria.common.model.Vote;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public interface IVotingService extends IValidationService<Vote>, ICrudService<Vote> {
    public Map<Integer, Integer> getNextDayMenuOptionsVotes(Date dateTime) throws SQLException;
}