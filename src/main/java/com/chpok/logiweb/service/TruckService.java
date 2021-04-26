package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.TruckDto;

import java.util.List;

public interface TruckService {
    List<TruckDto> getAllTrucks();
    void updateTruck(TruckDto truck);
    void deleteTruck(Long id);
    TruckDto getTruckById(Long id);
    void saveTruck(TruckDto truck);
}
