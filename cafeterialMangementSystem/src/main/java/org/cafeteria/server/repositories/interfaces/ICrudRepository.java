package org.cafeteria.server.repositories.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

// generic crud repository
public interface ICrudRepository<T> {
    void add(T item);
    void delete(T item);
    void update(T item);
    T GetAll();
    T getById(int id) throws SQLException;
}
