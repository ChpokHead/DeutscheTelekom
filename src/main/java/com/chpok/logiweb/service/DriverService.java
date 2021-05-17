package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;

import java.util.List;

public interface DriverService {
    List<DriverDto> getAllDrivers();
    void updateDriver(DriverDto driver);
    void updateDriverStatus(DriverDto driverDto);
    void deleteDriver(Long id);
    void saveDriver(DriverDto driver);
    DriverDto getDriverById(Long id);
    List<DriverDto> getAllDriversWithoutCurrentOrder();
    void updateDriverCurrentOrder(Long driverId, OrderDto newOrder);
    void updateDriverCurrentTruck(Long driverId, TruckDto newTruck);
    void deleteDriverCurrentOrderWithCurrentTruck(Long driverId);
    void updateDriversMonthWorkedHours(Long driverId, Short newMonthWorkedHours);
}
