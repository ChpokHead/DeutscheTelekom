package com.chpok.logiweb.service.validation;

public interface ValidationProvider<T> {
    void validate(T entity);
}
