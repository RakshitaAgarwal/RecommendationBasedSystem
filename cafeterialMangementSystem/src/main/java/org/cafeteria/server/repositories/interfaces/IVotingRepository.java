package org.cafeteria.server.repositories.interfaces;

import org.cafeteria.common.model.MenuItemUserVote;

import java.sql.SQLException;
import java.util.List;

public interface IVotingRepository extends ICrudRepository<MenuItemUserVote> {
    public List<MenuItemUserVote> getByUserCurrentDate(int userId, String date) throws SQLException;
}
