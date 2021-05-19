package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.service.DriverService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
import com.chpok.logiweb.service.TruckService;
import com.chpok.logiweb.service.validation.ValidationProvider;
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
public class DriverServiceImpl implements DriverService {
    private static final Logger LOGGER = LogManager.getLogger(DriverServiceImpl.class);

    private final ValidationProvider<DriverDto> validator;
    private final DriverDao driverDao;
    private final DriverMapper driverMapper;
    private final OrderMapper orderMapper;
    private final TruckMapper truckMapper;
    private final TruckService truckService;

    public DriverServiceImpl(ValidationProvider<DriverDto> validator, DriverDao driverDao, DriverMapper driverMapper, OrderMapper orderMapper, TruckMapper truckMapper, TruckService truckService) {
        this.validator = validator;
        this.driverDao = driverDao;
        this.driverMapper = driverMapper;
        this.orderMapper = orderMapper;
        this.truckMapper = truckMapper;
        this.truckService = truckService;
    }

    @Override
    public List<DriverDto> getAllDrivers() {
        try {
            return driverDao.findAll().stream().map(driverMapper::mapEntityToDto).collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all drivers exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateDriver(DriverDto driver) {
        try {
            validator.validate(driver);

            driverDao.update(driverMapper.mapDtoToEntity(driver));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating driver exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateDriverAndShiftWorkerStatus(Long driverId, DriverStatus newStatus) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);
            final DriverDto updatingDriverShiftworker = truckService.getDriverShiftworker(updatingDriver);

            if (newStatus == DriverStatus.DRIVING) {
                updateDriverStatus(updatingDriverShiftworker.getPersonalNumber(), DriverStatus.SHIFTING);
            } else if (newStatus == DriverStatus.SHIFTING) {
                updateDriverStatus(updatingDriverShiftworker.getPersonalNumber(), DriverStatus.DRIVING);
            }

            updateDriverStatus(updatingDriver.getPersonalNumber(), newStatus);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating driver status exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateDriverStatus(Long driverId, DriverStatus newStatus) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);

            updatingDriver.setStatus(newStatus.ordinal());

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating driver status exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void deleteDriver(Long id) {
        try {
            driverDao.deleteById(id);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting driver by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void saveDriver(DriverDto driver) {
        try {
            validator.validate(driver);

            driverDao.save(driverMapper.mapDtoToEntity(driver));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("saving driver exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public DriverDto getDriverById(Long id) {
        try {
            return driverMapper.mapEntityToDto(driverDao.findById(id).get());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting driver by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<DriverDto> getAllDriversWithoutCurrentOrder() {
        try {
            return driverDao.findAllDriversWithoutCurrentOrder().stream().map(driverMapper::mapEntityToDto).collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting drivers without current order exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateDriverCurrentOrder(Long driverId, OrderDto newOrder) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);

            updatingDriver.setCurrentOrder(orderMapper.mapDtoToEntity(newOrder));

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating driver current order by id exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateDriverCurrentTruck(Long driverId, TruckDto newTruck) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);

            updatingDriver.setCurrentTruck(truckMapper.mapDtoToEntity(newTruck));

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating driver current truck by id exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateDriverMonthWorkedHours(Long driverId, Short newMonthWorkedHours) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);

            updatingDriver.setMonthWorkedHours(newMonthWorkedHours);

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating driver monthWorkedHours by id exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateDriverLocation(Long driverId, Location location) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);

            updatingDriver.setLocation(location);

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating driver location by id exception");

            throw new InvalidEntityException();
        }
    }

}
