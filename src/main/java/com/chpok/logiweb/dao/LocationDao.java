package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Location;

import java.util.Optional;

public interface LocationDao extends CrudDao<Location> {
    Optional<Location> findByName(String name);
}
