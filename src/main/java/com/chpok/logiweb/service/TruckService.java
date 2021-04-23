package com.chpok.logiweb.service;

import com.chpok.logiweb.model.Truck;

import java.util.List;

public interface TruckService {
    List<Truck> getAllTrucks();
    void updateTruck(Truck truck);
    void deleteTruck(Long id);
    void saveTruck(Truck truck);
}
