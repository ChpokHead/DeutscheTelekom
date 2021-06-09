package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Truck;

import java.util.List;

public interface TruckDao extends CrudDao <Truck> {
    List<Truck> findByCurrentLocationId(Long currentLocationId);
}
