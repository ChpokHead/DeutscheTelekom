package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Truck;

import java.util.List;
import java.util.Optional;

public interface TruckDao extends CrudDao <Truck> {
    List<Truck> findByCurrentLocationId(Long currentLocationId);
    Optional<Truck> findByRegNumber(String regNumber);
}
