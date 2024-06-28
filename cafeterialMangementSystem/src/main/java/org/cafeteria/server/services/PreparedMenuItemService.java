package org.cafeteria.server.services;

import org.cafeteria.common.model.PreparedMenu;
import org.cafeteria.server.repositories.PreparedMenuItemItemRepository;
import org.cafeteria.server.repositories.interfaces.IPreparedMenuItemRepository;
import org.cafeteria.server.services.interfaces.IPreparedMenuItemService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreparedMenuItemService implements IPreparedMenuItemService {

    private static IPreparedMenuItemRepository _preparedMenuItemRepository;
    public PreparedMenuItemService() {
        _preparedMenuItemRepository = new PreparedMenuItemItemRepository();
    }
    @Override
    public boolean validate(PreparedMenu item) {
        return false;
    }

    @Override
    public boolean addAll(List<Integer> preparedMenuItemIds) throws SQLException {
        List<PreparedMenu> preparedMenuItems = new ArrayList<>();
        Date date = new Date();
        for(Integer menuItemId :preparedMenuItemIds) {
            preparedMenuItems.add(new PreparedMenu(menuItemId, date));
        }
        return _preparedMenuItemRepository.addBatch(preparedMenuItems);
    }

    @Override
    public boolean add(PreparedMenu object) {
        return false;
    }

    @Override
    public boolean update(PreparedMenu object) {
        return false;
    }

    @Override
    public boolean delete(PreparedMenu object) {
        return false;
    }

    @Override
    public List<PreparedMenu> getAll() {
        return null;
    }

    @Override
    public PreparedMenu getById(int id) {
        return null;
    }

    @Override
    public void updateDailyMenu() {

    }

    @Override
    public void weeklyMenuCleanUp() {

    }
}