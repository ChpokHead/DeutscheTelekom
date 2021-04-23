package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
    DriverDao driverDao;

    @Override
    public List<Driver> getAllDrivers() {
        return driverDao.findAll();
    }

    @Override
    public void updateDriver(Driver driver) {

        driverDao.update(driver);
    }

    @Override
    public void deleteDriver(Long id) {
        driverDao.deleteById(id);
    }

    @Override
    public void saveDriver(Driver driver) {
        driverDao.save(driver);
    }
}
