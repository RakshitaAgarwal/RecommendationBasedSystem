package org.cafeteria.server.services;

import org.cafeteria.common.model.PreparedMenu;
import org.cafeteria.server.services.interfaces.IPreparedMenuService;

import java.util.List;

public class PreparedMenuService implements IPreparedMenuService {
    @Override
    public boolean validate(PreparedMenu item) {
        return false;
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