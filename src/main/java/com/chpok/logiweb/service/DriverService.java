package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.DriverDto;

import java.util.List;

public interface DriverService {
    List<DriverDto> getAllDrivers();
    void updateDriver(DriverDto driver);
    void deleteDriver(Long id);
    void saveDriver(DriverDto driver);
    DriverDto getDriverById(Long id);
}
