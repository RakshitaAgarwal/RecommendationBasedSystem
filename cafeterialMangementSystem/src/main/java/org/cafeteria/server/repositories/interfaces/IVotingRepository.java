package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.Vote;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IVotingRepository extends ICrudRepository<Vote> {
    List<Vote> getByUserCurrentDate(int userId, String date) throws SQLException;

    Map<Integer, Integer> getNextDayMenuOptionsVotes(String date) throws SQLException;

    List<Vote> getAllByDate(String date) throws SQLException;
}