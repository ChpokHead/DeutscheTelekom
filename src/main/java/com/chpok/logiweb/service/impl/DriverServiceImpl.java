package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.service.DriverService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {
    private final DriverDao driverDao;
    private final DriverMapper driverMapper;
    private final OrderMapper orderMapper;
    private final TruckMapper truckMapper;

    public DriverServiceImpl(DriverDao driverDao, DriverMapper driverMapper, OrderMapper orderMapper, TruckMapper truckMapper) {
        this.driverDao = driverDao;
        this.driverMapper = driverMapper;
        this.orderMapper = orderMapper;
        this.truckMapper = truckMapper;
    }

    @Override
    public List<DriverDto> getAllDrivers() {
        return driverDao.findAll().stream().map(driverMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void updateDriver(DriverDto driver) {
        driverDao.update(driverMapper.mapDtoToEntity(driver));
    }

    @Override
    public void updateDriverStatus(DriverDto driverDto) {
        final DriverDto updatingDriver = getDriverById(driverDto.getPersonalNumber());

        updatingDriver.setStatus(driverDto.getStatus());

        updateDriver(updatingDriver);
    }

    @Override
    public void deleteDriver(Long id) {
        driverDao.deleteById(id);
    }

    @Override
    public void saveDriver(DriverDto driver) {
        driverDao.save(driverMapper.mapDtoToEntity(driver));
    }

    @Override
    public DriverDto getDriverById(Long id) {
        return driverMapper.mapEntityToDto(driverDao.findById(id).get());
    }

    @Override
    public List<DriverDto> getAllDriversWithoutCurrentOrder() {
        return driverDao.findAllDriversWithoutCurrentOrder().stream().map(driverMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void updateDriverCurrentOrder(Long driverId, OrderDto newOrder) {
        final DriverDto updatingDriver = getDriverById(driverId);

        updatingDriver.setCurrentOrder(orderMapper.mapDtoToEntity(newOrder));

        updateDriver(updatingDriver);
    }

    @Override
    public void updateDriverCurrentTruck(Long driverId, TruckDto newTruck) {
        final DriverDto updatingDriver = getDriverById(driverId);

        updatingDriver.setCurrentTruck(truckMapper.mapDtoToEntity(newTruck));

        updateDriver(updatingDriver);
    }

    @Override
    public void deleteDriverCurrentOrderWithCurrentTruck(Long driverId) {
        final DriverDto updatingDriver = getDriverById(driverId);

        updatingDriver.setCurrentTruck(null);
        updatingDriver.setCurrentOrder(null);

        updateDriver(updatingDriver);
    }

    @Override
    public void updateDriversMonthWorkedHours(Long driverId, Short newMonthWorkedHours) {
        final DriverDto updatingDriver = getDriverById(driverId);

        updatingDriver.setMonthWorkedHours(newMonthWorkedHours);

        updateDriver(updatingDriver);
    }

}
