package org.cafeteria.server.services;

import org.cafeteria.common.model.MenuItem;
import org.cafeteria.server.repositories.MenuRepository;
import org.cafeteria.server.repositories.interfaces.IMenuRepository;
import org.cafeteria.server.services.interfaces.IMenuService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MenuService implements IMenuService {
    private static IMenuRepository _menuRepository;

    public MenuService() {
        _menuRepository = new MenuRepository();
    }

    public static List<MenuItem> filterMenuItemsByLastPrepared(List<MenuItem> menuItems) {
        Date dateOfOneWeekAgo = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L);

        return menuItems.stream()
                .filter(item -> item.getLastTimePrepared() == null || item.getLastTimePrepared().before(dateOfOneWeekAgo))
                .collect(Collectors.toList());
    }

    public static List<MenuItem> filterMenuItemBasedOnAvailability(List<MenuItem> menuItems) {
        return menuItems.stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }

    public static List<Integer> filterMealTypeMenuItemIds(List<MenuItem> mealTypeItems) {
        List<MenuItem> filteredMenuItems = filterMenuItemsByLastPrepared(mealTypeItems);
        filteredMenuItems = filterMenuItemBasedOnAvailability(filteredMenuItems);
        List<Integer> filteredMealTypeItems = new ArrayList<>();
        for(MenuItem menuItem :filteredMenuItems) {
            filteredMealTypeItems.add(menuItem.getId());
        }
        return filteredMealTypeItems;
    }

    @Override
    public boolean add(MenuItem object) throws SQLException {
        return _menuRepository.add(object);
    }

    @Override
    public boolean update(MenuItem object) throws SQLException {
        return _menuRepository.update(object);
    }

    @Override
    public boolean delete(MenuItem object) throws SQLException {
        return _menuRepository.delete(object);
    }

    @Override
    public List<MenuItem> getAll() throws SQLException {
        return _menuRepository.GetAll();
    }

    @Override
    public MenuItem getById(int id) throws SQLException {
        return _menuRepository.getById(id);
    }

    @Override
    public MenuItem getByName(String menuItemName) throws SQLException {
        return _menuRepository.getByName(menuItemName);
    }

    @Override
    public List<MenuItem> getByMealTypeId(int mealTypeId) throws SQLException {
        return _menuRepository.getByMealTypeId(mealTypeId);
    }
}