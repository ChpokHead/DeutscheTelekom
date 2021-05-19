package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.enums.DriverStatus;

import java.util.List;

public interface DriverService {
    List<DriverDto> getAllDrivers();
    void updateDriver(DriverDto driver);
    void deleteDriver(Long id);
    void saveDriver(DriverDto driver);
    DriverDto getDriverById(Long id);
    List<DriverDto> getAllDriversWithoutCurrentOrder();
    void updateDriverCurrentOrder(Long driverId, OrderDto newOrder);
    void updateDriverCurrentTruck(Long driverId, TruckDto newTruck);
    void updateDriverStatus(Long driverId, DriverStatus newStatus);
    void updateDriverAndShiftWorkerStatus(Long driverId, DriverStatus newStatus);
    void updateDriverWhenOrderIsComplete(Long driverId, short newMonthWorkedHours, Location newLocation, DriverStatus newStatus);
}
