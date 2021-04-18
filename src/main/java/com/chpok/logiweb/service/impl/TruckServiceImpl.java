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
    public List<Truck> findAll() {
        return truckDao.findAll();
    }

    @Override
    public void update(Truck truck) {
        truckDao.update(truck);
    }
}
