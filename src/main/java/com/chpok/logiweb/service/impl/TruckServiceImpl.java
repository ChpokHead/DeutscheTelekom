package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.enums.TruckStatus;
import com.chpok.logiweb.service.TruckService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
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
public class TruckServiceImpl implements TruckService {
    private static final Logger LOGGER = LogManager.getLogger(TruckServiceImpl.class);

    private final ValidationProvider<TruckDto> validator;
    private final TruckDao truckDao;
    private final TruckMapper truckMapper;
    private final DriverMapper driverMapper;
    private final OrderMapper orderMapper;

    public TruckServiceImpl(ValidationProvider<TruckDto> validator, TruckDao truckDao, TruckMapper truckMapper, DriverMapper driverMapper, OrderMapper orderMapper) {
        this.validator = validator;
        this.truckDao = truckDao;
        this.truckMapper = truckMapper;
        this.driverMapper = driverMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<TruckDto> getAllTrucks() {
        try {
            return truckDao.findAll().stream().map(truckMapper::mapEntityToDto).collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all trucks exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateTruck(TruckDto truckDto) {
        try {
            validator.validate(truckDto);

            truckDao.update(truckMapper.mapDtoToEntity(truckDto));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating truck exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void deleteTruck(Long id) {
        try {
            truckDao.deleteById(id);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting truck by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void saveTruck(TruckDto truck) {
        try {
            validator.validate(truck);

            truckDao.save(truckMapper.mapDtoToEntity(truck));

            final String info = String.format("truck with reg number = %s was created", truck.getRegNumber());

            LOGGER.info(info);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("saving truck exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public DriverDto getDriverShiftworker(DriverDto driver) {
        try {
            if (driver.getCurrentTruck() != null) {
                final TruckDto currentTruck = getTruckById(driver.getCurrentTruck().getId());

                if (currentTruck.getCurrentDrivers().size() != 1) {
                    List<DriverDto> drivers = currentTruck.getCurrentDrivers()
                            .stream().map(driverMapper::mapEntityToDto).collect(Collectors.toList());

                    removeDriverFromListById(driver.getPersonalNumber(), drivers);

                    return drivers.get(0);
                }
            }

            return null;
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting driver shiftworker exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<TruckDto> getTrucksWithOKStatusAndWithoutCurrentOrder() {
        try {
            return getAllTrucks().stream()
                    .filter(truck -> TruckStatus.fromInteger(truck.getStatus()) == TruckStatus.OK && truck.getCurrentOrder() == null)
                    .collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting trucks with OK status and without current order exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateTruckCurrentOrder(Long truckId, OrderDto newOrder) {
        try {
            final TruckDto updatingTruck = getTruckById(truckId);

            updatingTruck.setCurrentOrder(orderMapper.mapDtoToEntity(newOrder));

            updateTruck(updatingTruck);

            final String info = String.format("current order of truck with id = %d has id = %d", truckId, newOrder.getId());

            LOGGER.info(info);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating truck's current order exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateTruckLocation(Long truckId, Location newLocation) {
        try {
            final TruckDto updatingTruck = getTruckById(truckId);

            updatingTruck.setLocation(newLocation);

            updateTruck(updatingTruck);

            final String info = String.format("truck with id = %d changed location to %s", truckId, newLocation.getName());

            LOGGER.info(info);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating truck location exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public TruckDto getTruckById(Long id) {
        try {
            return truckMapper.mapEntityToDto(truckDao.findById(id).orElseThrow(NoSuchElementException::new));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting truck by id exception");

            throw new EntityNotFoundException();
        }
    }

    private void removeDriverFromListById(Long id, List<DriverDto> driverList) {
        for (DriverDto driver : driverList) {
            if (driver.getPersonalNumber().equals(id)) {
                driverList.remove(driver);
                return;
            }
        }
    }
}
