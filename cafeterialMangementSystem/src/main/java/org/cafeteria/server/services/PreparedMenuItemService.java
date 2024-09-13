package org.cafeteria.server.services;

import org.cafeteria.common.customException.CustomExceptions;
import org.cafeteria.common.model.PreparedMenuItem;
import org.cafeteria.server.repositories.PreparedMenuItemItemRepository;
import org.cafeteria.server.repositories.interfaces.IPreparedMenuItemRepository;
import org.cafeteria.server.services.interfaces.IPreparedMenuItemService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.cafeteria.common.util.Utils.extractDate;

public class PreparedMenuItemService implements IPreparedMenuItemService {
    private static IPreparedMenuItemRepository _preparedMenuItemRepository;
    public PreparedMenuItemService() {
        _preparedMenuItemRepository = new PreparedMenuItemItemRepository();
    }

    @Override
    public boolean addPreparedMenuItems(List<Integer> preparedMenuItemIds) throws SQLException, CustomExceptions.DuplicateEntryFoundException {
        List<PreparedMenuItem> preparedMenuItems = new ArrayList<>();
        Date date = new Date();
        if(!_preparedMenuItemRepository.getByDate(extractDate(date)).isEmpty())
            throw new CustomExceptions.DuplicateEntryFoundException("Prepared Menu Items have already been Updated for " + extractDate(date));

        for(Integer menuItemId :preparedMenuItemIds) {
            preparedMenuItems.add(new PreparedMenuItem(menuItemId, date));
        }
        return _preparedMenuItemRepository.addBatch(preparedMenuItems);
    }

    @Override
    public boolean add(PreparedMenuItem object) {
        return false;
    }

    @Override
    public boolean update(PreparedMenuItem object) {
        return false;
    }

    @Override
    public boolean delete(PreparedMenuItem object) {
        return false;
    }

    @Override
    public List<PreparedMenuItem> getAll() {
        return null;
    }

    @Override
    public PreparedMenuItem getById(int id) {
        return null;
    }
}