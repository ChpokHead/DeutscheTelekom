package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.enums.DriverStatus;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {
    public Driver mapDriverDtoToDriver(DriverDto driverDto) {
        return Driver.builder()
                .withId(driverDto.getPersonalNumber())
                .withFirstName(driverDto.getFirstName())
                .withLastName(driverDto.getLastName())
                .withLocation(driverDto.getLocation())
                .withStatus(DriverStatus.fromInteger(driverDto.getStatus()))
                .withMonthWorkedHours(driverDto.getMonthWorkedHours())
                .withCurrentTruck(driverDto.getCurrentTruck())
                .withCurrentOrder(driverDto.getCurrentOrder())
                .build();
    }

    public DriverDto mapDriverToDriverDto(Driver driver) {
        final DriverDto driverDto = new DriverDto();

        driverDto.setFirstName(driver.getFirstName());
        driverDto.setLastName(driver.getLastName());
        driverDto.setStatus(driver.getStatus().ordinal());
        driverDto.setPersonalNumber(driver.getId());
        driverDto.setMonthWorkedHours(driver.getMonthWorkedHours());
        driverDto.setLocation(driver.getLocation());
        driverDto.setCurrentTruck(driver.getCurrentTruck());
        driverDto.setCurrentOrder(driver.getCurrentOrder());

        return driverDto;
    }
}
