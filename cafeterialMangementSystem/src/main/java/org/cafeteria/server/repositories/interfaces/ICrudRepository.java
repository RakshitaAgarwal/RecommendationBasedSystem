package org.cafeteria.server.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ICrudRepository<T> {
    boolean add(T item) throws SQLException;
    void delete(T item);
    boolean update(T item) throws SQLException;
    List<T> GetAll() throws SQLException;
    T getById(int id) throws SQLException;
}
