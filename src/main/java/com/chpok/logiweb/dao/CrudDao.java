package com.chpok.logiweb.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CrudDao<E> {
    void save(E entity);

    Optional<E> findById(Long id);

    List<E> findAll();

    void update(E entity);

    void deleteById(Long id);

    void deleteAllByIds(Set<Long> ids);
}
