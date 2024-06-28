package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.Vote;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IVotingRepository extends ICrudRepository<Vote> {
    public List<Vote> getByUserCurrentDate(int userId, String date) throws SQLException;

    public Map<Integer, Integer> getNextDayMenuOptionsVotes(String date) throws SQLException;

    public List<Vote> getAllByDate(String date) throws SQLException;
}
