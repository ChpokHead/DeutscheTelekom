package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.enums.TruckStatus;
import com.chpok.logiweb.service.TruckService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TruckServiceImpl implements TruckService {
    private final TruckDao truckDao;
    private final DriverDao driverDao;
    private final TruckMapper truckMapper;
    private final DriverMapper driverMapper;

    public TruckServiceImpl(TruckDao truckDao, DriverDao driverDao, TruckMapper truckMapper, DriverMapper driverMapper) {
        this.truckDao = truckDao;
        this.driverDao = driverDao;
        this.truckMapper = truckMapper;
        this.driverMapper = driverMapper;
    }

    @Override
    public List<TruckDto> getAllTrucks() {
        List<TruckDto> trucks = truckDao.findAll().stream().map(truckMapper::mapEntityToDto).collect(Collectors.toList());

        for (TruckDto truck : trucks) {
            truck.setCurrentDrivers(driverDao.findByCurrentTruckId(truck.getId()));
        }

        return trucks;
    }

    @Override
    public void updateTruck(TruckDto truckDto) {
        truckDao.update(truckMapper.mapDtoToEntity(truckDto));
    }

    @Override
    public void deleteTruck(Long id) {
        truckDao.deleteById(id);
    }

    @Override
    public void saveTruck(TruckDto truckDto) {
        truckDao.save(truckMapper.mapDtoToEntity(truckDto));
    }

    @Override
    public List<DriverDto> getDriverShiftworkers(DriverDto driver) {
        List<DriverDto> shiftworkers = new ArrayList<>();

        if (driver.getCurrentTruck() != null) {
            shiftworkers = getTruckById(driver.getCurrentTruck().getId()).getCurrentDrivers()
                    .stream().map(driverMapper::mapEntityToDto).collect(Collectors.toList());

            removeDriverFromListById(driver.getPersonalNumber(), shiftworkers);
        }


        return shiftworkers;
    }

    @Override
    public List<TruckDto> getTrucksWithOKStatusAndWithoutCurrentOrder() {
        final List<TruckDto> allTrucks = getAllTrucks();

        return allTrucks.stream()
                .filter(truck -> TruckStatus.fromInteger(truck.getStatus()) == TruckStatus.OK && truck.getCurrentOrder() == null)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTruckCurrentOrder(Long truckId, OrderDto newOrder) {
        final TruckDto updatingTruck = getTruckById(truckId);

        updatingTruck.setCurrentOrder(null);

        updateTruck(updatingTruck);
    }

    @Override
    public TruckDto getTruckById(Long id) {
        return truckMapper.mapEntityToDto(truckDao.findById(id).get());
    }

    private void removeDriverFromListById(Long id, List<DriverDto> driverList) {
        for (DriverDto driver : driverList) {
            if (driver.getPersonalNumber().equals(id)) {
                driverList.remove(driver);
                return;
            }
        }
    }
}
