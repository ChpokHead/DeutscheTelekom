package com.chpok.logiweb.validation;

public interface ValidationProvider<T> {
    void validate(T entity);
}
