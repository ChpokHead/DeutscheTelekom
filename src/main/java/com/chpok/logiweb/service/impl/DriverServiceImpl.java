package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.service.DriverService;
import com.chpok.logiweb.service.mapper.DriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {
    private final DriverDao driverDao;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverDao driverDao, DriverMapper driverMapper) {
        this.driverDao = driverDao;
        this.driverMapper = driverMapper;
    }

    @Override
    public List<DriverDto> getAllDrivers() {
        return driverDao.findAll().stream().map(driverMapper::mapDriverToDriverDto).collect(Collectors.toList());
    }

    @Override
    public void updateDriver(DriverDto driver) {
        driverDao.update(driverMapper.mapDriverDtoToDriver(driver));
    }

    @Override
    public void deleteDriver(Long id) {
        driverDao.deleteById(id);
    }

    @Override
    public void saveDriver(DriverDto driver) {
        driverDao.save(driverMapper.mapDriverDtoToDriver(driver));
    }

    @Override
    public DriverDto getDriverById(Long id) {
        return driverMapper.mapDriverToDriverDto(driverDao.findById(id).get());
    }
}
