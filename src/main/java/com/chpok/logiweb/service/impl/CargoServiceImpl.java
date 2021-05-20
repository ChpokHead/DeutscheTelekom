package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.service.CargoService;
import com.chpok.logiweb.mapper.impl.CargoMapper;
import com.chpok.logiweb.service.WaypointService;
import com.chpok.logiweb.validation.ValidationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class CargoServiceImpl implements CargoService {
    private static final Logger LOGGER = LogManager.getLogger(CargoServiceImpl.class);

    private final WaypointService waypointService;
    private final CargoDao cargoDao;
    private final ValidationProvider<CargoDto> validator;
    private final CargoMapper cargoMapper;

    public CargoServiceImpl(WaypointService waypointService, CargoDao cargoDao, ValidationProvider<CargoDto> validator, CargoMapper cargoMapper) {
        this.waypointService = waypointService;
        this.cargoDao = cargoDao;
        this.validator = validator;
        this.cargoMapper = cargoMapper;
    }

    @Override
    public List<CargoDto> getAllCargos() {
        try {
            return cargoDao.findAll().stream().map(cargoMapper::mapEntityToDto).collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all cargos exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void saveCargo(CargoDto cargo) {
        try {
            validator.validate(cargo);

            cargoDao.save(cargoMapper.mapDtoToEntity(cargo));

            logOnSuccess(String.format("new cargo with name = %s and weight = %d was created", cargo.getName(), cargo.getWeight()));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("saving cargo exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public CargoDto getCargoById(Long id) {
        try {
            return cargoMapper.mapEntityToDto(cargoDao.findById(id).orElseThrow(NoSuchElementException::new));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting cargo by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteCargo(Long id) {
        try {
            final Cargo deletingCargo = cargoDao.findById(id).orElseThrow(NoSuchElementException::new);

            for (Order.Waypoint waypoint : deletingCargo.getWaypoints()) {
                waypointService.deleteWaypoint(waypoint.getId());
            }

            cargoDao.deleteById(id);

            logOnSuccess(String.format("cargo with id = %d and name = %s was deleted", deletingCargo.getId(), deletingCargo.getName()));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting cargo by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateCargo(CargoDto cargo) {
        try {
            validator.validate(cargo);

            cargoDao.update(cargoMapper.mapDtoToEntity(cargo));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating cargo exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public List<CargoDto> getUnoccupiedCargos() {
        final List<Cargo> allCargos = cargoDao.findAll();
        final List<Cargo> unoccupiedCargos = allCargos.stream().filter(cargo -> cargo.getWaypoints().isEmpty()).collect(Collectors.toList());

        return unoccupiedCargos.stream().map(cargoMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void updateCargoStatus(Long cargoId, CargoStatus newCargoStatus) {
        try {
            final Cargo updatingCargo = cargoDao.findById(cargoId).orElseThrow(NoSuchElementException::new);

            updatingCargo.setStatus(newCargoStatus);

            cargoDao.update(updatingCargo);

            logOnSuccess(String.format("cargo with id = %d was updated to %s", updatingCargo.getId(), updatingCargo.getStatus().toString()));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating cargo status by id exception");

            throw new InvalidEntityException();
        }
    }

    private void logOnSuccess(String logInfo) {
        LOGGER.info(logInfo);
    }

}
