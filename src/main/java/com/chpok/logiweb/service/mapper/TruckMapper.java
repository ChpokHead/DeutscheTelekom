package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.TruckStatus;
import org.springframework.stereotype.Component;

@Component
public class TruckMapper {
    LocationDao locationDao;

    public TruckMapper(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public TruckDto mapTruckToTruckDto(Truck truck) {
        final TruckDto truckDto = new TruckDto();

        truckDto.setId(truck.getId());
        truckDto.setCapacity(truck.getCapacity());
        truckDto.setRegNumber(truck.getRegNumber());
        truckDto.setDriversShift(truck.getDriversShift());
        truckDto.setStatus(truck.getStatus().ordinal());

        if (truck.getLocation() != null) {
            truckDto.setLocation(truck.getLocation().getName());
        }
//        final List<Long> driverIdList = new ArrayList<>();
//
//        truck.getCurrentDrivers().forEach(driver -> driverIdList.add(driver.getId()));
//        truckDto.setCurrentDrivers(driverIdList);

        return truckDto;
    }

    public Truck mapTruckDtoToTruck(TruckDto truckDto) {
        return Truck.builder()
                .withId(truckDto.getId())
                .withRegNumber(truckDto.getRegNumber())
                .withDriversShift(truckDto.getDriversShift())
                .withCapacity(truckDto.getCapacity())
                .withLocation(locationDao.findByName(truckDto.getLocation()).get())
                .withStatus(TruckStatus.fromInteger(truckDto.getStatus()))
                .build();
    }
}
