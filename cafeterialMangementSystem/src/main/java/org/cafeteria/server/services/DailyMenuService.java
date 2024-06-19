package org.cafeteria.server.services;

import org.cafeteria.common.model.DailyMenu;
import org.cafeteria.common.model.MenuItem;
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
    public void update(DailyMenu object) {

    }

    @Override
    public void delete(DailyMenu object) {

    }

    @Override
    public List<DailyMenu> getAll() {

        return null;
    }

    @Override
    public void getById(DailyMenu object) {

    }

    @Override
    public void updateDailyMenu() {

    }

    @Override
    public void weeklyMenuCleanUp() {

    }
}