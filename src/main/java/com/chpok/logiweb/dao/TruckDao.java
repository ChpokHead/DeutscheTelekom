package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Truck;

import java.util.Optional;

public interface TruckDao extends CrudDao <Truck> {
    Optional<Truck> findByRegNumber(String regNumber);
}
