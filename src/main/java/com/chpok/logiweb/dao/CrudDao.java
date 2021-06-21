package com.chpok.logiweb.dao;

import com.chpok.logiweb.exception.MethodNotImplementedException;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E> {
    default Long save(E entity) {
        throw new MethodNotImplementedException();
    }

    default Optional<E> findById(Long id) {
        throw new MethodNotImplementedException();
    }

    default List<E> findAll() {
        throw new MethodNotImplementedException();
    }

    default void update(E entity) {
        throw new MethodNotImplementedException();
    }

    default void deleteById(Long id) {
        throw new MethodNotImplementedException();
    }

}
