package org.cafeteria.server.services;

import org.cafeteria.common.model.DiscardMenuItem;
import org.cafeteria.server.repositories.DiscardMenuItemRepository;
import org.cafeteria.server.repositories.interfaces.IDiscardMenuItemsRepository;
import org.cafeteria.server.services.interfaces.IDiscardMenuItemService;

import java.sql.SQLException;
import java.util.List;

public class DiscardMenuItemService implements IDiscardMenuItemService {
    private static IDiscardMenuItemsRepository _discardMenuItemsRepository;
    public DiscardMenuItemService() {
        _discardMenuItemsRepository = new DiscardMenuItemRepository();
    }
    @Override
    public List<DiscardMenuItem> getAll() throws SQLException {
        return _discardMenuItemsRepository.getAll();
    }
}