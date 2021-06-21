package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.TruckStatus;
import com.chpok.logiweb.model.kafka.LogiwebMessage;
import com.chpok.logiweb.service.TruckService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
import com.chpok.logiweb.validation.ValidationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class TruckServiceImpl implements TruckService {
    private static final Logger LOGGER = LogManager.getLogger(TruckServiceImpl.class);

    @Autowired
    private TruckDao truckDao;
    @Autowired
    private TruckMapper truckMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private KafkaTemplate<String, LogiwebMessage> kafkaTemplate;
    private final ValidationProvider<TruckDto> saveUpdateValidator;
    private final ValidationProvider<TruckDto> deleteValidator;

    public TruckServiceImpl(@Qualifier("truckDtoValidator") ValidationProvider<TruckDto> saveUpdateValidator,
                            @Qualifier("truckDtoDeleteValidator") ValidationProvider<TruckDto> deleteValidator) {
        this.saveUpdateValidator = saveUpdateValidator;
        this.deleteValidator = deleteValidator;
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
            saveUpdateValidator.validate(truckDto);

            truckDao.update(truckMapper.mapDtoToEntity(truckDto));

            sendMessage(new LogiwebMessage("truck updated", truckDto.getId()));

            logOnSuccess(String.format("truck with id = %d was updated", truckDto.getId()));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating truck exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void deleteTruck(Long id) {
        try {
            final Truck deletingTruck = truckDao.findById(id).orElseThrow(NoSuchElementException::new);

            deleteValidator.validate(truckMapper.mapEntityToDto(deletingTruck));

            truckDao.deleteById(id);

            sendMessage(new LogiwebMessage("truck deleted", deletingTruck.getId()));

            logOnSuccess(String.format("truck with id = %d was deleted", id));
        } catch (IllegalArgumentException e) {
            LOGGER.error("deleting truck by id exception");

            throw new InvalidEntityException();
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting truck by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void saveTruck(TruckDto truck) {
        try {
            saveUpdateValidator.validate(truck);

            final Long savedTruckId = truckDao.save(truckMapper.mapDtoToEntity(truck));

            sendMessage(new LogiwebMessage("truck saved", savedTruckId));

            logOnSuccess(String.format("truck with reg number = %s was created", truck.getRegNumber()));
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

            logOnSuccess(String.format("current order of truck with id = %d was updated", truckId));
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

            logOnSuccess(String.format("truck with id = %d changed location to %s", truckId, newLocation.getName()));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating truck location exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateTruckWhenCurrentOrderIsDeleted(Long truckId) {
        try {
            final TruckDto updatingTruck = getTruckById(truckId);

            updatingTruck.setCurrentOrder(null);
            updatingTruck.setCurrentDrivers(Collections.emptyList());

            updateTruck(updatingTruck);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating truck when order is deleted exception");

            throw new EntityNotFoundException();
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

    @Override
    public List<TruckDto> getTrucksAtLocationByName(String locationName) {
        try {
            final Location foundLocation = locationDao.findByName(locationName).orElseThrow(NoSuchElementException::new);
            final List<Truck> trucksAtLocation = truckDao.findByCurrentLocationId(foundLocation.getId());

            return trucksAtLocation.stream().map(truckMapper::mapEntityToDto).collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting trucks by location name exception");

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

    private void logOnSuccess(String logInfo) {
        LOGGER.info(logInfo);
    }

    private void sendMessage(LogiwebMessage message) {
        kafkaTemplate.send("logiweb-truck", message);
    }
}
