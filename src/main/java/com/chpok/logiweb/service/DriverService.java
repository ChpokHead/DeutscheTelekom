package com.chpok.logiweb.service;

import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Truck;
import org.springframework.stereotype.Component;

import java.util.List;

public interface DriverService {
    List<Driver> getAllDrivers();
    void updateDriver(Driver driver);
    void deleteDriver(Long id);
    void saveDriver(Driver driver);
}
