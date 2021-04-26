package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.service.TruckService;
import com.chpok.logiweb.service.mapper.TruckMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TruckServiceImpl implements TruckService {

    private final TruckDao truckDao;
    private final TruckMapper truckMapper;

    public TruckServiceImpl(TruckDao truckDao, TruckMapper truckMapper) {
        this.truckDao = truckDao;
        this.truckMapper = truckMapper;
    }

    @Override
    public List<TruckDto> getAllTrucks() {
        return truckDao.findAll().stream().map(truckMapper::mapTruckToTruckDto).collect(Collectors.toList());
    }

    @Override
    public void updateTruck(TruckDto truckDto) {
        truckDao.update(truckMapper.mapTruckDtoToTruck(truckDto));
    }

    @Override
    public void deleteTruck(Long id) {
        truckDao.deleteById(id);
    }

    @Override
    public void saveTruck(TruckDto truckDto) {
        truckDao.save(truckMapper.mapTruckDtoToTruck(truckDto));
    }

    @Override
    public TruckDto getTruckById(Long id) {
        return truckMapper.mapTruckToTruckDto(truckDao.findById(id).get());
    }
}
