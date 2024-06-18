package org.cafeteria.server.services.interfaces;

public interface ICrudService<T> {
    public void add(T object);
    public void update(T object);
    public void delete(T object);
    public void getAll(T object);
    public void getById(T object);
}
