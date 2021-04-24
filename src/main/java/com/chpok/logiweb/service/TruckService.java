package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Truck;

import java.util.List;

public interface TruckService {
    List<Truck> getAllTrucks();
    void updateTruck(TruckDto truck);
    void deleteTruck(Long id);
    TruckDto getTruckById(Long id);
    void saveTruck(TruckDto truck);
}
