package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.service.CargoService;
import com.chpok.logiweb.mapper.impl.CargoMapper;
import com.chpok.logiweb.service.WaypointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CargoServiceImpl implements CargoService {
    private final WaypointService waypointService;
    private final CargoDao cargoDao;
    private final CargoMapper cargoMapper;

    public CargoServiceImpl(WaypointService waypointService, CargoDao cargoDao, CargoMapper cargoMapper) {
        this.waypointService = waypointService;
        this.cargoDao = cargoDao;
        this.cargoMapper = cargoMapper;
    }

    @Override
    public List<CargoDto> getAllCargos() {
        return cargoDao.findAll().stream().map(cargoMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void saveCargo(CargoDto cargo) {
        cargoDao.save(cargoMapper.mapDtoToEntity(cargo));
    }

    @Override
    public CargoDto getCargoById(Long id) {
        return cargoMapper.mapEntityToDto(cargoDao.findById(id).get());
    }

    @Override
    public void deleteCargo(Long id) {
        final Cargo deletingCargo = cargoDao.findById(id).get();

        for (Order.Waypoint waypoint : deletingCargo.getWaypoints()) {
            waypointService.deleteWaypoint(waypoint.getId());
        }

        cargoDao.deleteById(id);
    }

    @Override
    public void updateCargo(CargoDto cargo) {
        cargoDao.update(cargoMapper.mapDtoToEntity(cargo));
    }

    @Override
    public List<CargoDto> getUnoccupiedCargos() {
        final List<Cargo> allCargos = cargoDao.findAll();
        final List<Cargo> unoccupiedCargos = allCargos.stream().filter(cargo -> cargo.getWaypoints().isEmpty()).collect(Collectors.toList());

        return unoccupiedCargos.stream().map(cargoMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void updateCargoStatus(Long cargoId, CargoStatus newCargoStatus) {
        final Cargo updatingCargo = cargoDao.findById(cargoId).get();

        updatingCargo.setStatus(newCargoStatus);

        cargoDao.update(updatingCargo);
    }
}
