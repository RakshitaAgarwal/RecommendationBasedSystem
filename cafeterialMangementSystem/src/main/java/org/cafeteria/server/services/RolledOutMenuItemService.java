package org.cafeteria.server.services;

import org.cafeteria.common.customException.CustomExceptions.DuplicateEntryFoundException;
import org.cafeteria.common.model.RolledOutMenuItem;
import org.cafeteria.server.repositories.RolledOutMenuItemRepository;
import org.cafeteria.server.repositories.interfaces.IRolledOutMenuItemRepository;
import org.cafeteria.server.services.interfaces.IRolledOutMenuItemService;
import static org.cafeteria.common.constants.Constants.DATE_FORMAT;
import static org.cafeteria.common.util.Utils.extractDate;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RolledOutMenuItemService implements IRolledOutMenuItemService {
    private static IRolledOutMenuItemRepository _rollOutMenuItemRepository;

    public RolledOutMenuItemService() {
        _rollOutMenuItemRepository = new RolledOutMenuItemRepository();
    }

    @Override
    public boolean rollOutNextDayMenuOptions(List<Integer> rolledOutMenuItemIds) throws SQLException, DuplicateEntryFoundException {
        List<RolledOutMenuItem> rolledOutMenuItems = new ArrayList<>();
        Date rolledOutDate = new Date();
        if(!_rollOutMenuItemRepository.getByDate(extractDate(rolledOutDate)).isEmpty())
            throw new DuplicateEntryFoundException("Items have already been rolled out for " + extractDate(rolledOutDate));

        for (int menuItemId : rolledOutMenuItemIds) {
            RolledOutMenuItem rolledOutMenuItem = new RolledOutMenuItem();
            rolledOutMenuItem.setMenuItemId(menuItemId);
            rolledOutMenuItem.setRolledOutDate(rolledOutDate);
            rolledOutMenuItems.add(rolledOutMenuItem);
        }

        return _rollOutMenuItemRepository.addBatch(rolledOutMenuItems);
    }

    @Override
    public List<RolledOutMenuItem> getNextDayMenuOptions() throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String currentDate = dateFormat.format(new Date());
        System.out.println(currentDate);
        return _rollOutMenuItemRepository.getByDate(currentDate);
    }

    @Override
    public boolean add(RolledOutMenuItem object) {
        return false;
    }

    @Override
    public boolean update(RolledOutMenuItem object) {
        return false;
    }

    @Override
    public boolean delete(RolledOutMenuItem object) {
        return false;
    }

    @Override
    public List<RolledOutMenuItem> getAll() {
        return null;
    }

    @Override
    public RolledOutMenuItem getById(int id) {
        return null;
    }
}