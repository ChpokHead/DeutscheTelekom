package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Driver;

import java.util.Optional;

public interface DriverDao extends CrudDao<Driver> {
    Optional<Driver> findByPersonalNumber(String personalNumber);
}
