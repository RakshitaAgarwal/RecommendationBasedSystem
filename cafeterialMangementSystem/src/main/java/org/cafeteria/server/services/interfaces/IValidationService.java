package org.cafeteria.server.services.interfaces;

public interface IValidationService<T> {
    public boolean validate(T item);
}
