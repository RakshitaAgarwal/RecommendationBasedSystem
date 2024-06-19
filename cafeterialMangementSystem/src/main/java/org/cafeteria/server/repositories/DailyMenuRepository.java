package org.cafeteria.server.repositories;

import org.cafeteria.common.model.DailyMenu;
import org.cafeteria.server.repositories.interfaces.IDailyMenuRepository;

import java.util.List;

public class DailyMenuRepository implements IDailyMenuRepository {
    @Override
    public boolean add(DailyMenu item) {

        return false;
    }

    @Override
    public void delete(DailyMenu item) {

    }

    @Override
    public boolean update(DailyMenu item) {

        return false;
    }

    @Override
    public List<DailyMenu> GetAll() {
        return null;
    }

    @Override
    public DailyMenu getById(int id) {
        return null;
    }
}