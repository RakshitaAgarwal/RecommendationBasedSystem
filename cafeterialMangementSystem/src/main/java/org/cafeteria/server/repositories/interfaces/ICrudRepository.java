package org.cafeteria.server.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ICrudRepository<T> {
    boolean add(T item) throws SQLException;

    boolean addBatch(List<T> items) throws SQLException;

    boolean delete(T item) throws SQLException;

    boolean update(T item) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws SQLException;
}