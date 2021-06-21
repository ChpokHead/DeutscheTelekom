package com.chpok.logiweb.service;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.TruckStatus;
import com.chpok.logiweb.model.kafka.LogiwebMessage;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class TruckServiceTest {
    @Mock
    private TruckMapper truckMapper;
    @Mock
    private TruckDao truckDao;
    @Mock
    private DriverMapper driverMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private LocationDao locationDao;
    @Mock
    private KafkaTemplate<String, LogiwebMessage> kafkaTemplate;
    @Autowired
    @InjectMocks
    private TruckService truckService;

    @Test
    void getAllTrucksShouldReturnCorrectTruckDtoList() {
        final Truck firstTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withStatus(TruckStatus.OK).build();

        final TruckDto firstTruckDto = new TruckDto();

        firstTruckDto.setId(firstTruck.getId());
        firstTruckDto.setRegNumber(firstTruck.getRegNumber());
        firstTruckDto.setStatus(firstTruck.getStatus().ordinal());

        final Truck secondTruck = Truck.builder()
                .withId(2L)
                .withRegNumber("HR32842")
                .withStatus(TruckStatus.OK).build();

        final TruckDto secondTruckDto = new TruckDto();

        firstTruckDto.setId(secondTruck.getId());
        firstTruckDto.setRegNumber(secondTruck.getRegNumber());
        firstTruckDto.setStatus(secondTruck.getStatus().ordinal());

        final List<TruckDto> expected =
                Arrays.asList(firstTruckDto, secondTruckDto);

        when(truckDao.findAll()).thenReturn(Arrays.asList(firstTruck, secondTruck));
        when(truckMapper.mapEntityToDto(firstTruck)).thenReturn(firstTruckDto);
        when(truckMapper.mapEntityToDto(secondTruck)).thenReturn(secondTruckDto);

        final List<TruckDto> actual = truckService.getAllTrucks();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateTruckShouldCorrectlyUpdateTruck() {
        final Truck updatedTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withCapacity((short)12)
                .withDriversShift((short)2)
                .withStatus(TruckStatus.OK).build();

        final TruckDto updatedTruckDto = new TruckDto();

        updatedTruckDto.setId(updatedTruck.getId());
        updatedTruckDto.setRegNumber(updatedTruck.getRegNumber());
        updatedTruckDto.setStatus(updatedTruck.getStatus().ordinal());
        updatedTruckDto.setCapacity(updatedTruck.getCapacity());
        updatedTruckDto.setDriversShift(updatedTruck.getDriversShift());

        when(truckMapper.mapDtoToEntity(updatedTruckDto)).thenReturn(updatedTruck);

        assertThatNoException().isThrownBy(() -> truckService.updateTruck(updatedTruckDto));
    }

    @Test
    void updateTruckShouldThrowInvalidEntityExceptionIfTruckIsIncorrect() {
        final Truck updatedTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("  ")
                .withCapacity((short)-4)
                .withDriversShift((short)2)
                .withStatus(TruckStatus.OK).build();

        final TruckDto updatedTruckDto = new TruckDto();

        updatedTruckDto.setId(updatedTruck.getId());
        updatedTruckDto.setRegNumber(updatedTruck.getRegNumber());
        updatedTruckDto.setStatus(updatedTruck.getStatus().ordinal());
        updatedTruckDto.setCapacity(updatedTruck.getCapacity());
        updatedTruckDto.setDriversShift(updatedTruck.getDriversShift());

        assertThatExceptionOfType(InvalidEntityException.class).isThrownBy(() -> truckService.updateTruck(updatedTruckDto));
    }

    @Test
    void saveTruckShouldThrowInvalidEntityExceptionIfTruckIsInvalid() {
        final TruckDto savingTruck = new TruckDto();
        savingTruck.setId(1L);
        savingTruck.setRegNumber(" ");
        savingTruck.setCapacity((short)-20);

        assertThatExceptionOfType(InvalidEntityException.class).isThrownBy(() -> truckService.saveTruck(savingTruck));
    }

    @Test
    void getDriverShiftworkerShouldReturnCorrectDriverShiftworker() {
        final Long foundTruckId = 1L;
        final Truck foundTruck = Truck.builder()
                .withId(foundTruckId)
                .withRegNumber("OR32842")
                .withCapacity((short)12)
                .withDriversShift((short)2)
                .withStatus(TruckStatus.OK).build();

        final TruckDto foundTruckDto = new TruckDto();

        foundTruckDto.setId(foundTruck.getId());
        foundTruckDto.setRegNumber(foundTruck.getRegNumber());
        foundTruckDto.setStatus(foundTruck.getStatus().ordinal());
        foundTruckDto.setCapacity(foundTruck.getCapacity());
        foundTruckDto.setDriversShift(foundTruck.getDriversShift());

        final Driver firstDriver = Driver.builder()
                .withId(1L)
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short)43)
                .withCurrentTruck(foundTruck)
                .build();

        final Driver secondDriver = Driver.builder()
                .withId(2L)
                .withFirstName("Андрей")
                .withLastName("Андреев")
                .withMonthWorkedHours((short)13)
                .withCurrentTruck(foundTruck)
                .build();

        final DriverDto firstDriverDto = new DriverDto();

        firstDriverDto.setPersonalNumber(1L);
        firstDriverDto.setCurrentTruck(foundTruckDto);
        firstDriverDto.setFirstName("Иван");
        firstDriverDto.setLastName("Иванов");
        firstDriverDto.setMonthWorkedHours((short)43);

        final DriverDto secondDriverDto = new DriverDto();

        secondDriverDto.setPersonalNumber(2L);
        secondDriverDto.setCurrentTruck(foundTruckDto);
        secondDriverDto.setFirstName("Андрей");
        secondDriverDto.setLastName("Андреев");
        secondDriverDto.setMonthWorkedHours((short)13);

        foundTruckDto.setCurrentDrivers(Arrays.asList(firstDriver, secondDriver));

        when(truckDao.findById(foundTruckId)).thenReturn(java.util.Optional.of(foundTruck));
        when(truckMapper.mapEntityToDto(foundTruck)).thenReturn(foundTruckDto);

        when(driverMapper.mapEntityToDto(firstDriver)).thenReturn(firstDriverDto);
        when(driverMapper.mapEntityToDto(secondDriver)).thenReturn(secondDriverDto);

        final DriverDto actual = truckService.getDriverShiftworker(firstDriverDto);

        assertThat(actual).isEqualTo(secondDriverDto);
    }

    @Test
    void getTrucksWithOKStatusAndWithoutCurrentOrderShouldReturnCorrectTruckList() {
        final Truck firstTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withStatus(TruckStatus.OK).build();

        final TruckDto firstTruckDto = new TruckDto();

        firstTruckDto.setId(firstTruck.getId());
        firstTruckDto.setRegNumber(firstTruck.getRegNumber());
        firstTruckDto.setStatus(firstTruck.getStatus().ordinal());

        final Truck secondTruck = Truck.builder()
                .withId(2L)
                .withRegNumber("HR32842")
                .withStatus(TruckStatus.BROKEN).build();

        final TruckDto secondTruckDto = new TruckDto();

        secondTruckDto.setId(secondTruck.getId());
        secondTruckDto.setRegNumber(secondTruck.getRegNumber());
        secondTruckDto.setStatus(secondTruck.getStatus().ordinal());

        final List<TruckDto> expected =
                Collections.singletonList(firstTruckDto);

        when(truckDao.findAll()).thenReturn(Arrays.asList(firstTruck, secondTruck));
        when(truckMapper.mapEntityToDto(firstTruck)).thenReturn(firstTruckDto);
        when(truckMapper.mapEntityToDto(secondTruck)).thenReturn(secondTruckDto);

        final List<TruckDto> actual = truckService.getTrucksWithOKStatusAndWithoutCurrentOrder();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateTruckCurrentOrderShouldCorrectlyUpdateTruckCurrentOrder() {
        final Long updatingTruckId = 1L;
        final Order expected = Order.builder().withId(2L).build();
        final Truck oldTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withCapacity((short)23)
                .withDriversShift((short)2)
                .withCurrentOrder(Order.builder().withId(1L).build())
                .withStatus(TruckStatus.OK).build();
        final TruckDto oldTruckDto = new TruckDto();

        oldTruckDto.setId(oldTruck.getId());
        oldTruckDto.setRegNumber(oldTruck.getRegNumber());
        oldTruckDto.setStatus(oldTruck.getStatus().ordinal());
        oldTruckDto.setCurrentOrder(Order.builder().withId(1L).build());
        oldTruckDto.setCapacity((short)23);
        oldTruckDto.setDriversShift((short)2);

        final OrderDto expectedDto = new OrderDto();

        expectedDto.setId(2L);

        when(truckDao.findById(updatingTruckId)).thenReturn(Optional.of(oldTruck));
        when(truckMapper.mapEntityToDto(oldTruck)).thenReturn(oldTruckDto);
        when(orderMapper.mapDtoToEntity(expectedDto)).thenReturn(expected);

        truckService.updateTruckCurrentOrder(updatingTruckId, expectedDto);

        final Order actual = oldTruckDto.getCurrentOrder();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateTruckLocationShouldCorrectlyUpdateTruckLocation() {
        final Long updatingTruckId = 1L;
        final Location expected = new Location(9L, "Санкт-Петербург");
        final Truck oldTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withCapacity((short)23)
                .withDriversShift((short)2)
                .withCurrentOrder(Order.builder().withId(1L).build())
                .withLocation(new Location(8L, "Москва"))
                .withStatus(TruckStatus.OK).build();
        final TruckDto oldTruckDto = new TruckDto();

        oldTruckDto.setId(oldTruck.getId());
        oldTruckDto.setRegNumber(oldTruck.getRegNumber());
        oldTruckDto.setStatus(oldTruck.getStatus().ordinal());
        oldTruckDto.setCurrentOrder(Order.builder().withId(1L).build());
        oldTruckDto.setCapacity((short)23);
        oldTruckDto.setDriversShift((short)2);
        oldTruckDto.setLocation(new Location(8L, "Москва"));

        when(truckDao.findById(updatingTruckId)).thenReturn(Optional.of(oldTruck));
        when(truckMapper.mapEntityToDto(oldTruck)).thenReturn(oldTruckDto);

        truckService.updateTruckLocation(updatingTruckId, expected);

        final Location actual = oldTruckDto.getLocation();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateTruckWhenCurrentOrderIsDeletedShouldUpdateCurrentTruckOrderAndDriversToNull() {
        final Long updatingTruckId = 1L;
        final Truck oldTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withCapacity((short)23)
                .withDriversShift((short)2)
                .withCurrentOrder(Order.builder().withId(1L).build())
                .withCurrentDrivers(Collections.singletonList(Driver.builder().withId(1L).build()))
                .withStatus(TruckStatus.OK).build();
        final TruckDto oldTruckDto = new TruckDto();

        oldTruckDto.setId(oldTruck.getId());
        oldTruckDto.setRegNumber(oldTruck.getRegNumber());
        oldTruckDto.setStatus(oldTruck.getStatus().ordinal());
        oldTruckDto.setCurrentOrder(Order.builder().withId(1L).build());
        oldTruckDto.setCapacity((short)23);
        oldTruckDto.setDriversShift((short)2);
        oldTruckDto.setCurrentDrivers(Collections.singletonList(Driver.builder().withId(1L).build()));

        when(truckDao.findById(updatingTruckId)).thenReturn(Optional.of(oldTruck));
        when(truckMapper.mapEntityToDto(oldTruck)).thenReturn(oldTruckDto);

        truckService.updateTruckWhenCurrentOrderIsDeleted(updatingTruckId);

        assertThat(oldTruckDto.getCurrentDrivers()).isEmpty();
        assertThat(oldTruckDto.getCurrentOrder()).isNull();
    }

    @Test
    void getTruckByIdShouldReturnCorrectTruckIfIdIsPresent() {
        final Long foundTruckId = 1L;
        final Truck foundTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withCapacity((short)23)
                .withDriversShift((short)2)
                .withCurrentOrder(Order.builder().withId(1L).build())
                .withCurrentDrivers(Collections.singletonList(Driver.builder().withId(1L).build()))
                .withStatus(TruckStatus.OK).build();
        final TruckDto expected = new TruckDto();

        expected.setId(foundTruck.getId());
        expected.setRegNumber(foundTruck.getRegNumber());
        expected.setStatus(foundTruck.getStatus().ordinal());
        expected.setCurrentOrder(Order.builder().withId(1L).build());
        expected.setCapacity((short)23);
        expected.setDriversShift((short)2);
        expected.setCurrentDrivers(Collections.singletonList(Driver.builder().withId(1L).build()));

        when(truckDao.findById(foundTruckId)).thenReturn(Optional.of(foundTruck));
        when(truckMapper.mapEntityToDto(foundTruck)).thenReturn(expected);

        final TruckDto actual = truckService.getTruckById(foundTruckId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getTruckByIdShouldThrowEntityNotFoundExceptionIfIdIsNotPresent() {
        final Long foundTruckId = 1L;

        when(truckDao.findById(foundTruckId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> truckService.getTruckById(foundTruckId));
    }

    @Test
    void getTrucksAtLocationByNameShouldReturnCorrectTruckListIfLocationIsPresent() {
        final String findingLocationName = "Москва";
        final Location foundLocation = new Location(8L, findingLocationName);
        final Truck firstTruck = Truck.builder()
                .withId(1L)
                .withRegNumber("OR32842")
                .withCapacity((short)23)
                .withDriversShift((short)2)
                .withCurrentOrder(Order.builder().withId(1L).build())
                .withLocation(foundLocation)
                .withStatus(TruckStatus.OK).build();
        final TruckDto firstTruckDto = new TruckDto();

        firstTruckDto.setId(firstTruck.getId());
        firstTruckDto.setRegNumber(firstTruck.getRegNumber());
        firstTruckDto.setStatus(firstTruck.getStatus().ordinal());
        firstTruckDto.setCurrentOrder(Order.builder().withId(1L).build());
        firstTruckDto.setCapacity((short)23);
        firstTruckDto.setDriversShift((short)2);
        firstTruckDto.setLocation(foundLocation);

        final Truck secondTruck = Truck.builder()
                .withId(2L)
                .withRegNumber("BB00842")
                .withCapacity((short)23)
                .withDriversShift((short)2)
                .withLocation(new Location(10L, "Казань"))
                .withStatus(TruckStatus.OK).build();
        final TruckDto secondTruckDto = new TruckDto();

        secondTruckDto.setId(secondTruck.getId());
        secondTruckDto.setRegNumber(secondTruck.getRegNumber());
        secondTruckDto.setStatus(secondTruck.getStatus().ordinal());
        secondTruckDto.setCapacity((short)23);
        secondTruckDto.setDriversShift((short)2);
        secondTruckDto.setLocation(secondTruck.getLocation());

        final List<TruckDto> expected = Collections.singletonList(firstTruckDto);

        when(locationDao.findByName(findingLocationName)).thenReturn(Optional.of(foundLocation));
        when(truckDao.findByCurrentLocationId(foundLocation.getId())).thenReturn(Collections.singletonList(firstTruck));
        when(truckMapper.mapEntityToDto(firstTruck)).thenReturn(firstTruckDto);

        final List<TruckDto> actual = truckService.getTrucksAtLocationByName(findingLocationName);

        assertThat(actual).isEqualTo(expected);
    }
}
