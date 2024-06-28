package org.cafeteria.server.services;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.Vote;
import org.cafeteria.server.repositories.MenuRepository;
import org.cafeteria.server.repositories.VotingRepository;
import org.cafeteria.server.repositories.interfaces.IMenuRepository;
import org.cafeteria.server.repositories.interfaces.IVotingRepository;
import org.cafeteria.server.services.interfaces.IVotingService;
import static org.cafeteria.common.util.Utils.extractDate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VotingService implements IVotingService {
    private static IVotingRepository _votingRepository;
    private static IMenuRepository _menuRepository;

    public VotingService() {
        _votingRepository = new VotingRepository();
        _menuRepository = new MenuRepository();
    }

    @Override
    public boolean validate(Vote item) {
        return false;
    }


    @Override
    public boolean add(Vote userVote) throws SQLException, DuplicateEntryFoundException{
        List<Vote> userVotes = getUserCurrentDayVotes(userVote.getUserId(), userVote.getDateTime());
        if (!userVotes.isEmpty()) {
            MenuItem menuItemToVote = _menuRepository.getById(userVote.getMenuItemId());
            for (Vote castedUserVote : userVotes) {
                MenuItem votedMenuItem = _menuRepository.getById(castedUserVote.getMenuItemId());
                if (votedMenuItem.getMealTypeId() == menuItemToVote.getMealTypeId()) {
                    throw new DuplicateEntryFoundException("User has already casted voted for this category. Duplicate Entry.");
                }
            }
        }
        return _votingRepository.add(userVote);
    }

    private List<Vote> getUserCurrentDayVotes(int userId, Date dateTime) throws SQLException {
        return _votingRepository.getByUserCurrentDate(userId, extractDate(dateTime));
    }

    @Override
    public Map<Integer, Integer> getNextDayMenuOptionsVotes(Date dateTime) throws SQLException {
        return _votingRepository.getNextDayMenuOptionsVotes(extractDate(dateTime));
    }

    @Override
    public boolean update(Vote object) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Vote object) throws SQLException {
        return false;
    }

    @Override
    public List<Vote> getAll() {
        return null;
    }

    @Override
    public Vote getById(int id) throws SQLException {
        return null;
    }
}