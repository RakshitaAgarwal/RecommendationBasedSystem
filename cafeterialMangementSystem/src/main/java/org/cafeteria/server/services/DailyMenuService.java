package org.cafeteria.server.services;

import org.cafeteria.common.model.DailyMenu;
import org.cafeteria.server.services.interfaces.IDailyMenuService;

import java.util.List;

public class DailyMenuService implements IDailyMenuService {
    @Override
    public boolean validate(DailyMenu item) {
        return false;
    }

    @Override
    public boolean add(DailyMenu object) {

        return false;
    }

    @Override
    public boolean update(DailyMenu object) {

        return false;
    }

    @Override
    public boolean delete(DailyMenu object) {

        return false;
    }

    @Override
    public List<DailyMenu> getAll() {

        return null;
    }

    @Override
    public DailyMenu getById(int id) {
        return null;
    }

    @Override
    public void updateDailyMenu() {

    }

    @Override
    public void weeklyMenuCleanUp() {

    }
}