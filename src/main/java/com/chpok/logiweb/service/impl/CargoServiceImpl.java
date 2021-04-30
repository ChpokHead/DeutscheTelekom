package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.service.CargoService;
import com.chpok.logiweb.service.mapper.CargoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CargoServiceImpl implements CargoService {
    private final CargoDao cargoDao;
    private final CargoMapper cargoMapper;

    public CargoServiceImpl(CargoDao cargoDao, CargoMapper cargoMapper) {
        this.cargoDao = cargoDao;
        this.cargoMapper = cargoMapper;
    }

    @Override
    public List<CargoDto> getAllCargos() {
        return cargoDao.findAll().stream().map(cargoMapper::mapCargoToCargoDto).collect(Collectors.toList());
    }

    @Override
    public void saveCargo(CargoDto cargo) {
        cargoDao.save(cargoMapper.mapCargoDtoToCargo(cargo));
    }

    @Override
    public CargoDto getCargoById(Long id) {
        return cargoMapper.mapCargoToCargoDto(cargoDao.findById(id).get());
    }

    @Override
    public void deleteCargo(Long id) {
        cargoDao.deleteById(id);
    }

    @Override
    public void updateCargo(CargoDto cargo) {
        cargoDao.update(cargoMapper.mapCargoDtoToCargo(cargo));
    }
}
