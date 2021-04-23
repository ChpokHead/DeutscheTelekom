package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.service.TruckService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {

    private final TruckDao truckDao;

    public TruckServiceImpl(TruckDao truckDao) {
        this.truckDao = truckDao;
    }

    @Override
    public List<Truck> getAllTrucks() {
        return truckDao.findAll();
    }

    @Override
    public void updateTruck(Truck truck) {
        truckDao.update(truck);
    }

    @Override
    public void deleteTruck(Long id) {
        truckDao.deleteById(id);
    }

    @Override
    public void saveTruck(Truck truck) {
        truckDao.save(truck);
    }
}
