package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Driver;

import java.util.List;
import java.util.Optional;

public interface DriverDao extends CrudDao<Driver> {
    List<Driver> findByCurrentTruckId(Long id);
    List<Driver> findAllDriversWithoutCurrentOrder();
}
