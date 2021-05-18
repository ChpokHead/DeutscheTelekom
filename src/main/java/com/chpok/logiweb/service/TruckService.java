package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Location;

import java.util.List;

public interface TruckService {
    List<TruckDto> getAllTrucks();
    void updateTruck(TruckDto truck);
    void deleteTruck(Long id);
    TruckDto getTruckById(Long id);
    void saveTruck(TruckDto truck);
    List<DriverDto> getDriverShiftworkers(DriverDto driver);
    List<TruckDto> getTrucksWithOKStatusAndWithoutCurrentOrder();
    void updateTruckCurrentOrder(Long truckId, OrderDto newOrder);
    void updateTruckLocation(Long truckId, Location newLocation);
}
