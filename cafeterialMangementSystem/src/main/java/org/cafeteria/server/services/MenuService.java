package org.cafeteria.server.services;

import org.cafeteria.common.model.MenuItem;
import org.cafeteria.server.repositories.MenuRepository;
import org.cafeteria.server.repositories.interfaces.IMenuRepository;
import org.cafeteria.server.services.interfaces.IMenuService;

import java.sql.SQLException;
import java.util.List;

public class MenuService implements IMenuService {
    private static IMenuRepository _menuRepository;

    public MenuService() {
        _menuRepository = new MenuRepository();
    }
    @Override
    public boolean validate(MenuItem item) {
        return false;
    }

    @Override
    public boolean add(MenuItem object) throws SQLException {
        return _menuRepository.add(object);
    }

    @Override
    public void update(MenuItem object) {

    }

    @Override
    public void delete(MenuItem object) {

    }

    @Override
    public List<MenuItem> getAll() throws SQLException {
        return _menuRepository.GetAll();
    }

    @Override
    public void getById(MenuItem object) {

    }

    @Override
    public MenuItem getByName(String menuItemName) throws SQLException {
        return _menuRepository.getByName(menuItemName);
    }
}
