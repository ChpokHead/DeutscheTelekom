package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.TruckStatus;
import org.springframework.stereotype.Component;

@Component
public class TruckMapper implements Mapper<Truck, TruckDto> {
    public TruckDto mapEntityToDto(Truck truck) {
        if (truck == null) {
            return null;
        }

        final TruckDto truckDto = new TruckDto();

        truckDto.setId(truck.getId());
        truckDto.setCapacity(truck.getCapacity());
        truckDto.setRegNumber(truck.getRegNumber());
        truckDto.setDriversShift(truck.getDriversShift());
        truckDto.setStatus(truck.getStatus().ordinal());
        truckDto.setLocation(truck.getLocation());
        truckDto.setCurrentDrivers(truck.getCurrentDrivers());
        truckDto.setCurrentOrder(truck.getCurrentOrder());

        return truckDto;
    }

    public Truck mapDtoToEntity(TruckDto truckDto) {
        if (truckDto == null) {
            return null;
        }

        return Truck.builder()
                .withId(truckDto.getId())
                .withRegNumber(truckDto.getRegNumber())
                .withDriversShift(truckDto.getDriversShift())
                .withCapacity(truckDto.getCapacity())
                .withLocation(truckDto.getLocation())
                .withStatus(TruckStatus.fromInteger(truckDto.getStatus()))
                .withCurrentOrder(truckDto.getCurrentOrder())
                .build();
    }
}
