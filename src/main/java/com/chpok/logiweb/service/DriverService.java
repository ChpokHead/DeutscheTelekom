package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Location;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    List<DriverDto> getAllDrivers();
    void updateDriver(DriverDto driver);
    void deleteDriver(Long id);
    void saveDriver(DriverDto driver);
    DriverDto getDriverById(Long id);
    List<DriverDto> getAllDriversWithoutCurrentOrder();
    void updateDriverCurrentOrder(Long driverId, OrderDto newOrder);
    void updateDriverCurrentTruck(Long driverId, TruckDto newTruck);
    void updateDriverStatus(DriverDto driver);
    void updateDriverMonthWorkedHours(Long driverId, Short newMonthWorkedHours);
    void updateDriverLocation(Long driverId, Location location);
}
