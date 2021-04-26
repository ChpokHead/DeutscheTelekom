package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.enums.DriverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {
    @Autowired
    LocationDao locationDao;

    public Driver mapDriverDtoToDriver(DriverDto driverDto) {
        return Driver.builder()
                .withId(driverDto.getPersonalNumber())
                .withFirstName(driverDto.getFirstName())
                .withLastName(driverDto.getLastName())
                .withLocation(locationDao.findByName(driverDto.getLocation()).get())
                .withStatus(DriverStatus.fromInteger(driverDto.getStatus()))
                .withMonthWorkedHours(driverDto.getMonthWorkedHours())
                .build();
    }

    public DriverDto mapDriverToDriverDto(Driver driver) {
        final DriverDto driverDto = new DriverDto();

        driverDto.setFirstName(driver.getFirstName());
        driverDto.setLastName(driver.getLastName());
        driverDto.setStatus(driver.getStatus().ordinal());
        driverDto.setPersonalNumber(driver.getId());
        driverDto.setMonthWorkedHours(driver.getMonthWorkedHours());

        if (driver.getCurrentTruck() != null) {
            driverDto.setCurrentTruck(driver.getCurrentTruck().getRegNumber());
        }

        if (driver.getLocation() != null) {
            driverDto.setLocation(driver.getLocation().getName());
        }

        return driverDto;
    }
}
