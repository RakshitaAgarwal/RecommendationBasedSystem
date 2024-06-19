package org.cafeteria.server.services.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ICrudService<T> {
    public boolean add(T object) throws SQLException;
    public boolean update(T object) throws SQLException;
    public void delete(T object);
    public List<T> getAll() throws SQLException;
    public void getById(T object);
}
