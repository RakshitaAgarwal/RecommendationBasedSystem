package org.cafeteria.server.repositories;

import org.cafeteria.common.model.PreparedMenu;
import org.cafeteria.server.repositories.interfaces.IPreparedMenuRepository;

import java.util.List;

public class PreparedMenuRepository implements IPreparedMenuRepository {
    @Override
    public boolean add(PreparedMenu item) {

        return false;
    }

    @Override
    public boolean delete(PreparedMenu item) {

        return false;
    }

    @Override
    public boolean update(PreparedMenu item) {

        return false;
    }

    @Override
    public List<PreparedMenu> GetAll() {
        return null;
    }

    @Override
    public PreparedMenu getById(int id) {
        return null;
    }
}