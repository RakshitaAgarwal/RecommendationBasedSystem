package org.cafeteria.server.services;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemUserVote;
import org.cafeteria.server.repositories.MenuRepository;
import org.cafeteria.server.repositories.VotingRepository;
import org.cafeteria.server.repositories.interfaces.IMenuRepository;
import org.cafeteria.server.repositories.interfaces.IVotingRepository;
import org.cafeteria.server.services.interfaces.IVotingService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.cafeteria.common.constants.Constants.DATE_FORMAT;

public class VotingService implements IVotingService {
    private static IVotingRepository _votingRepository;
    private static IMenuRepository _menuRepository;

    public VotingService() {
        _votingRepository = new VotingRepository();
        _menuRepository = new MenuRepository();
    }

    @Override
    public boolean validate(MenuItemUserVote item) {
        return false;
    }


    @Override
    public boolean add(MenuItemUserVote userVote) throws SQLException, DuplicateEntryFoundException{
        List<MenuItemUserVote> userVotes = getUserVotes(userVote.getUserId(), userVote.getDateTime());
        if (!userVotes.isEmpty()) {
            MenuItem menuItemToVote = _menuRepository.getById(userVote.getMenuItemId());
            for (MenuItemUserVote castedUserVote : userVotes) {
                MenuItem votedMenuItem = _menuRepository.getById(castedUserVote.getMenuItemId());
                if (votedMenuItem.getMealTypeId() == menuItemToVote.getMealTypeId()) {
                    throw new DuplicateEntryFoundException("User has already casted voted for this category. Duplicate Entry.");
                }
            }
        }
        return _votingRepository.add(userVote);
    }

    private List<MenuItemUserVote> getUserVotes(int userId, Date dateTime) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return _votingRepository.getByUserCurrentDate(userId, dateFormat.format(dateTime));
    }

    @Override
    public boolean update(MenuItemUserVote object) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(MenuItemUserVote object) throws SQLException {
        return false;
    }

    @Override
    public List<MenuItemUserVote> getAll() {
        return null;
    }

    @Override
    public MenuItemUserVote getById(int id) throws SQLException {
        return null;
    }
}
