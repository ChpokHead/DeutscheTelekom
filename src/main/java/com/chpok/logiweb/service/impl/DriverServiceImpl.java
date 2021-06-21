package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.model.kafka.LogiwebMessage;
import com.chpok.logiweb.service.DriverService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.service.TruckService;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {
    private static final Logger LOGGER = LogManager.getLogger(DriverServiceImpl.class);

    @Autowired
    private DriverDao driverDao;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private TruckService truckService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private KafkaTemplate<String, LogiwebMessage> kafkaTemplate;
    private final ValidationProvider<DriverDto> saveUpdateValidator;
    private final ValidationProvider<DriverDto> deleteValidator;


    public DriverServiceImpl(@Qualifier("driverDtoValidator") ValidationProvider<DriverDto> saveUpdateValidator,
                             @Qualifier("driverDtoDeleteValidator") ValidationProvider<DriverDto> deleteValidator) {
        this.saveUpdateValidator = saveUpdateValidator;
        this.deleteValidator = deleteValidator;
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
            saveUpdateValidator.validate(driver);

            driverDao.update(driverMapper.mapDtoToEntity(driver));

            logOnSuccess(String.format("driver with id = %d was updated", driver.getPersonalNumber()));

            sendMessage(new LogiwebMessage("driver updated", driver.getPersonalNumber()));
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

            if (updatingDriverShiftworker != null) {
                if (newStatus == DriverStatus.DRIVING) {
                    updateDriverStatus(updatingDriverShiftworker.getPersonalNumber(), DriverStatus.SHIFTING);
                } else if (newStatus == DriverStatus.SHIFTING) {
                    updateDriverStatus(updatingDriverShiftworker.getPersonalNumber(), DriverStatus.DRIVING);
                }
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
            final Driver deletingDriver = driverDao.findById(id).orElseThrow(IllegalArgumentException::new);

            deleteValidator.validate(driverMapper.mapEntityToDto(deletingDriver));

            driverDao.deleteById(id);

            logOnSuccess(String.format("driver with id = %d was deleted", id));

            sendMessage(new LogiwebMessage("driver deleted", id));
        } catch (IllegalArgumentException e){
            LOGGER.error("deleting driver by id exception");

            throw new InvalidEntityException();
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting driver by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void saveDriver(DriverDto driver) {
        try {
            saveUpdateValidator.validate(driver);

            final Long savedDriverId = driverDao.save(driverMapper.mapDtoToEntity(driver));

            logOnSuccess(String.format("new driver with first name = %s and last name = %s was created", driver.getFirstName(), driver.getLastName()));

            sendMessage(new LogiwebMessage("driver saved", savedDriverId));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("saving driver exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public DriverDto getDriverById(Long id) {
        try {
            return driverMapper.mapEntityToDto(driverDao.findById(id)
                    .orElseThrow(NoSuchElementException::new));
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

            updatingDriver.setCurrentTruck(newTruck);

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating driver current truck by id exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateDriverWhenOrderIsCompleted(Long driverId, short newMonthWorkedHours, Location newLocation) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);

            updatingDriver.setMonthWorkedHours(newMonthWorkedHours);
            updatingDriver.setLocation(newLocation);
            updatingDriver.setStatus(DriverStatus.RESTING.ordinal());
            updatingDriver.setCurrentOrder(null);
            updatingDriver.setCurrentTruck(null);

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating driver when order is completed exception");
        }
    }

    @Override
    public void updateDriverWhenOrderIsDeleted(Long driverId) {
        try {
            final DriverDto updatingDriver = getDriverById(driverId);

            updatingDriver.setStatus(DriverStatus.RESTING.ordinal());
            updatingDriver.setCurrentOrder(null);
            updatingDriver.setCurrentTruck(null);

            updateDriver(updatingDriver);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("updating driver when order is deleted exception");
        }
    }

    private void logOnSuccess(String logInfo) {
        LOGGER.info(logInfo);
    }

    private void sendMessage(LogiwebMessage message) {
        kafkaTemplate.send("logiweb-driver", message);
    }

}
