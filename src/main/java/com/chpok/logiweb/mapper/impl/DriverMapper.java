package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.enums.DriverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper implements Mapper<Driver, DriverDto> {
    @Autowired
    private TruckMapper truckMapper;

    public Driver mapDtoToEntity(DriverDto driverDto) {
        if (driverDto == null) {
            return null;
        }

        return Driver.builder()
                .withId(driverDto.getPersonalNumber())
                .withFirstName(driverDto.getFirstName())
                .withLastName(driverDto.getLastName())
                .withLocation(driverDto.getLocation())
                .withStatus(DriverStatus.fromInteger(driverDto.getStatus()))
                .withMonthWorkedHours(driverDto.getMonthWorkedHours())
                .withCurrentTruck(truckMapper.mapDtoToEntity(driverDto.getCurrentTruck()))
                .withCurrentOrder(driverDto.getCurrentOrder())
                .build();
    }

    public DriverDto mapEntityToDto(Driver driver) {
        if (driver == null) {
            return null;
        }

        final DriverDto driverDto = new DriverDto();

        driverDto.setFirstName(driver.getFirstName());
        driverDto.setLastName(driver.getLastName());
        driverDto.setStatus(driver.getStatus().ordinal());
        driverDto.setPersonalNumber(driver.getId());
        driverDto.setMonthWorkedHours(driver.getMonthWorkedHours());
        driverDto.setLocation(driver.getLocation());
        driverDto.setCurrentTruck(truckMapper.mapEntityToDto(driver.getCurrentTruck()));
        driverDto.setCurrentOrder(driver.getCurrentOrder());

        return driverDto;
    }
}
