package com.chpok.logiweb.service;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.dao.DriverDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.model.*;
import com.chpok.logiweb.model.enums.DriverStatus;
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
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class DriverServiceTest {
    @Mock
    private DriverMapper driverMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private DriverDao driverDao;
    @Mock
    private TruckService truckService;
    /*@Mock
    private KafkaTemplate<String, LogiwebMessage> kafkaTemplate;*/
    @Autowired
    @InjectMocks
    private DriverService driverService;

    @Test
    void getAllDriversShouldReturnCorrectDriverDtoList() {
        final Driver firstDriver = Driver.builder()
                .withId(1L)
                .withFirstName("Андрей")
                .withLastName("Сухачев")
                .withStatus(DriverStatus.DRIVING).build();

        final DriverDto firstDriverDto = new DriverDto();

        firstDriverDto.setPersonalNumber(1L);
        firstDriverDto.setFirstName("Андрей");
        firstDriverDto.setLastName("Сухачев");
        firstDriverDto.setStatus(DriverStatus.DRIVING.ordinal());

        final Driver secondDriver = Driver.builder()
                .withId(2L)
                .withFirstName("Никита")
                .withLastName("Петров")
                .withStatus(DriverStatus.SHIFTING).build();

        final DriverDto secondDriverDto = new DriverDto();

        firstDriverDto.setPersonalNumber(2L);
        firstDriverDto.setFirstName("Никита");
        firstDriverDto.setLastName("Петров");
        firstDriverDto.setStatus(DriverStatus.SHIFTING.ordinal());

        final List<DriverDto> expected =
                Arrays.asList(firstDriverDto, secondDriverDto);

        when(driverDao.findAll()).thenReturn(Arrays.asList(firstDriver, secondDriver));
        when(driverMapper.mapEntityToDto(firstDriver)).thenReturn(firstDriverDto);
        when(driverMapper.mapEntityToDto(secondDriver)).thenReturn(secondDriverDto);

        final List<DriverDto> actual = driverService.getAllDrivers();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateShouldCorrectlyUpdateDriver() {
        final Driver updatedDriver = Driver.builder()
                .withId(2L)
                .withFirstName("Никита")
                .withLastName("Петров")
                .withMonthWorkedHours((short)10)
                .withStatus(DriverStatus.SHIFTING).build();

        final DriverDto updatedDriverDto = new DriverDto();

        updatedDriverDto.setPersonalNumber(2L);
        updatedDriverDto.setFirstName("Никита");
        updatedDriverDto.setLastName("Петров");
        updatedDriverDto.setMonthWorkedHours((short)10);
        updatedDriverDto.setStatus(DriverStatus.SHIFTING.ordinal());

        when(driverMapper.mapDtoToEntity(updatedDriverDto)).thenReturn(updatedDriver);

        doNothing().when(driverDao).update(updatedDriver);

        assertThatNoException().isThrownBy(() -> driverService.updateDriver(updatedDriverDto));
    }

    @Test
    void updateShouldThrowInvalidEntityExceptionIfDriverIsNotCorrect() {
        final DriverDto updatedDriverDto = new DriverDto();

        updatedDriverDto.setPersonalNumber(2L);
        updatedDriverDto.setFirstName("");
        updatedDriverDto.setLastName("   ");
        updatedDriverDto.setMonthWorkedHours((short)10);
        updatedDriverDto.setStatus(DriverStatus.SHIFTING.ordinal());

        assertThatExceptionOfType(InvalidEntityException.class).isThrownBy(() -> driverService.updateDriver(updatedDriverDto));
    }

    @Test
    void updateDriverStatusShouldCorrectlyUpdateDriverStatus() {
        final Long updatingDriverId = 2L;
        final Integer expected = DriverStatus.DRIVING.ordinal();
        final Driver oldDriver = Driver.builder()
                .withId(updatingDriverId)
                .withFirstName("Андрей")
                .withLastName("Сухачев")
                .withMonthWorkedHours((short)20)
                .withStatus(DriverStatus.RESTING).build();
        final DriverDto oldDriverDto = new DriverDto();

        oldDriverDto.setPersonalNumber(updatingDriverId);
        oldDriverDto.setFirstName("Андрей");
        oldDriverDto.setLastName("Сухачев");
        oldDriverDto.setMonthWorkedHours((short)20);
        oldDriverDto.setStatus(0);

        when(driverDao.findById(updatingDriverId)).thenReturn(Optional.ofNullable(oldDriver));
        when(driverMapper.mapEntityToDto(oldDriver)).thenReturn(oldDriverDto);

        driverService.updateDriverStatus(updatingDriverId, DriverStatus.DRIVING);

        final Integer actual = oldDriverDto.getStatus();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriverAndShiftWorkerStatusShouldCorrectlyUpdateDriverAndDriversShiftworkerStatus() {
        final Long updatingDriverId = 2L;
        final Long updatingShiftworkerId = 3L;
        final Integer driverExpectedStatus = DriverStatus.DRIVING.ordinal();
        final Integer shiftworkerExpectedStatus = DriverStatus.SHIFTING.ordinal();
        final Driver oldDriver = Driver.builder()
                .withId(updatingDriverId)
                .withFirstName("Андрей")
                .withLastName("Сухачев")
                .withMonthWorkedHours((short)20)
                .withStatus(DriverStatus.RESTING).build();

        final DriverDto oldDriverDto = new DriverDto();

        oldDriverDto.setPersonalNumber(updatingDriverId);
        oldDriverDto.setFirstName("Андрей");
        oldDriverDto.setLastName("Сухачев");
        oldDriverDto.setMonthWorkedHours((short)20);
        oldDriverDto.setStatus(0);

        when(driverDao.findById(updatingDriverId)).thenReturn(Optional.ofNullable(oldDriver));
        when(driverMapper.mapEntityToDto(oldDriver)).thenReturn(oldDriverDto);

        final Driver oldShiftworker = Driver.builder()
                .withId(updatingShiftworkerId)
                .withFirstName("Илья")
                .withLastName("Павлов")
                .withMonthWorkedHours((short)67)
                .withStatus(DriverStatus.RESTING).build();

        final DriverDto oldShiftworkerDto = new DriverDto();

        oldShiftworkerDto.setPersonalNumber(updatingShiftworkerId);
        oldShiftworkerDto.setFirstName("Илья");
        oldShiftworkerDto.setLastName("Павлов");
        oldShiftworkerDto.setMonthWorkedHours((short)67);
        oldShiftworkerDto.setStatus(0);

        when(driverDao.findById(updatingShiftworkerId)).thenReturn(Optional.ofNullable(oldShiftworker));
        when(driverMapper.mapEntityToDto(oldShiftworker)).thenReturn(oldShiftworkerDto);

        when(truckService.getDriverShiftworker(oldDriverDto)).thenReturn(oldShiftworkerDto);

        driverService.updateDriverAndShiftWorkerStatus(updatingDriverId, DriverStatus.DRIVING);

        final Integer driverActualStatus = oldDriverDto.getStatus();
        final Integer shiftworkerActualStatus = oldShiftworkerDto.getStatus();

        assertThat(driverActualStatus).isEqualTo(driverExpectedStatus);
        assertThat(shiftworkerActualStatus).isEqualTo(shiftworkerExpectedStatus);
    }

    @Test
    void deleteDriverShouldProperlyDeleteDriverById() {
        final Long deletingDriverId = 1L;
        final Driver deletingDriver = Driver.builder()
                .withId(deletingDriverId)
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short)28)
                .withCurrentOrder(Order.builder().withId(1L).build())
                .withStatus(DriverStatus.RESTING).build();

        final DriverDto deletingDriverDto = new DriverDto();

        deletingDriverDto.setPersonalNumber(deletingDriverId);
        deletingDriverDto.setFirstName("Иван");
        deletingDriverDto.setLastName("Иванов");
        deletingDriverDto.setMonthWorkedHours((short)28);
        deletingDriverDto.setCurrentOrder(Order.builder().withId(1L).build());
        deletingDriverDto.setStatus(DriverStatus.RESTING.ordinal());

        when(driverDao.findById(deletingDriverId)).thenReturn(Optional.ofNullable(deletingDriver));

        doNothing().when(driverDao).deleteById(deletingDriverId);

        when(driverMapper.mapEntityToDto(deletingDriver)).thenReturn(deletingDriverDto);

        driverService.deleteDriver(deletingDriverId);

        when(driverDao.findById(deletingDriverId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> driverService.getDriverById(deletingDriverId));
    }

    @Test
    void deleteDriverShouldThrowInvalidEntityExceptionIfDriverIsNotValid() {
        final Long deletingDriverId = 1L;
        final Driver deletingDriver = Driver.builder()
                .withId(deletingDriverId)
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short)28)
                .withStatus(DriverStatus.RESTING).build();

        final DriverDto deletingDriverDto = new DriverDto();

        deletingDriverDto.setPersonalNumber(deletingDriverId);
        deletingDriverDto.setFirstName("Иван");
        deletingDriverDto.setLastName("Иванов");
        deletingDriverDto.setMonthWorkedHours((short)28);
        deletingDriverDto.setStatus(DriverStatus.RESTING.ordinal());

        when(driverDao.findById(deletingDriverId)).thenReturn(Optional.ofNullable(deletingDriver));

        when(driverMapper.mapEntityToDto(deletingDriver)).thenReturn(deletingDriverDto);

        assertThatExceptionOfType(InvalidEntityException.class).isThrownBy(() -> driverService.deleteDriver(deletingDriverId));
    }

    @Test
    void saveDriverShouldThrowInvalidEntityExceptionIfDriverIsInvalid() {
        final DriverDto savingDriver = new DriverDto();
        savingDriver.setPersonalNumber(1L);
        savingDriver.setFirstName(" ");
        savingDriver.setLastName("Иванов");
        savingDriver.setStatus(DriverStatus.RESTING.ordinal());

        assertThatExceptionOfType(InvalidEntityException.class).isThrownBy(() -> driverService.saveDriver(savingDriver));
    }

    @Test
    void getDriverByIdShouldReturnCorrectDriver() {
        final Long foundDriverId = 1L;
        final Driver foundDriver = Driver.builder()
                .withId(foundDriverId)
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short)28)
                .withStatus(DriverStatus.RESTING).build();

        final DriverDto expected = new DriverDto();

        expected.setPersonalNumber(foundDriverId);
        expected.setFirstName("Иван");
        expected.setLastName("Иванов");
        expected.setMonthWorkedHours((short)28);
        expected.setStatus(DriverStatus.RESTING.ordinal());

        when(driverDao.findById(foundDriverId)).thenReturn(Optional.ofNullable(foundDriver));

        when(driverMapper.mapEntityToDto(foundDriver)).thenReturn(expected);

        final DriverDto actual = driverService.getDriverById(foundDriverId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllDriversWithoutCurrentOrderShouldReturnCorrectDriverList() {
        final Driver foundDriver = Driver.builder()
                .withId(1L)
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short)28)
                .withStatus(DriverStatus.RESTING).build();

        final DriverDto foundDriverDto = new DriverDto();

        foundDriverDto.setPersonalNumber(foundDriver.getId());
        foundDriverDto.setFirstName("Иван");
        foundDriverDto.setLastName("Иванов");
        foundDriverDto.setMonthWorkedHours((short)28);
        foundDriverDto.setStatus(DriverStatus.RESTING.ordinal());

        when(driverDao.findAllDriversWithoutCurrentOrder()).thenReturn(Collections.singletonList(foundDriver));
        when(driverMapper.mapEntityToDto(foundDriver)).thenReturn(foundDriverDto);

        final List<DriverDto> expected = Collections.singletonList(foundDriverDto);
        final List<DriverDto> actual = driverService.getAllDriversWithoutCurrentOrder();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriverCurrentOrderShouldCorrectlyUpdateDriverCurrentOrder() {
        final Long updatingDriverId = 2L;
        final Order expected = Order.builder().withId(2L).build();
        final Driver oldDriver = Driver.builder()
                .withId(updatingDriverId)
                .withFirstName("Андрей")
                .withLastName("Сухачев")
                .withMonthWorkedHours((short)20)
                .withCurrentOrder(Order.builder().withId(1L).build())
                .withStatus(DriverStatus.RESTING).build();
        final DriverDto oldDriverDto = new DriverDto();

        oldDriverDto.setPersonalNumber(updatingDriverId);
        oldDriverDto.setFirstName("Андрей");
        oldDriverDto.setLastName("Сухачев");
        oldDriverDto.setMonthWorkedHours((short)20);
        oldDriverDto.setStatus(0);

        final OrderDto expectedDto = new OrderDto();

        expectedDto.setId(2L);

        when(driverDao.findById(updatingDriverId)).thenReturn(Optional.ofNullable(oldDriver));
        when(driverMapper.mapEntityToDto(oldDriver)).thenReturn(oldDriverDto);
        when(orderMapper.mapDtoToEntity(expectedDto)).thenReturn(expected);

        driverService.updateDriverCurrentOrder(updatingDriverId, expectedDto);

        final Order actual = oldDriverDto.getCurrentOrder();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriverCurrentTruckShouldCorrectlyUpdateDriverCurrentTruck() {
        final Long updatingDriverId = 2L;
        final Driver oldDriver = Driver.builder()
                .withId(updatingDriverId)
                .withFirstName("Николай")
                .withLastName("Горбунов")
                .withMonthWorkedHours((short)100)
                .withCurrentTruck(Truck.builder().withId(1L).withRegNumber("FG20343").build())
                .withStatus(DriverStatus.RESTING).build();

        final TruckDto truckDto = new TruckDto();

        truckDto.setId(1L);
        truckDto.setRegNumber("FG20343");

        final DriverDto oldDriverDto = new DriverDto();

        oldDriverDto.setPersonalNumber(updatingDriverId);
        oldDriverDto.setFirstName("Николай");
        oldDriverDto.setLastName("Горбунов");
        oldDriverDto.setMonthWorkedHours((short)100);
        oldDriverDto.setCurrentTruck(truckDto);
        oldDriverDto.setStatus(0);

        final TruckDto expected = new TruckDto();

        expected.setId(2L);
        expected.setRegNumber("PO43623");

        when(driverDao.findById(updatingDriverId)).thenReturn(Optional.ofNullable(oldDriver));
        when(driverMapper.mapEntityToDto(oldDriver)).thenReturn(oldDriverDto);

        driverService.updateDriverCurrentTruck(updatingDriverId, expected);

        final TruckDto actual = oldDriverDto.getCurrentTruck();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriverWhenOrderIsCompletedShouldSetDriverTruckAndOrderToNullAndUpdateLocationWithMonthWorkedHoursAndStatus() {
        final Long updatingDriverId = 2L;
        final Integer expectedStatus = DriverStatus.RESTING.ordinal();
        final short expectedMonthWorkedHours = 50;
        final Location expectedLocation = new Location(2L,"Москва");

        final Driver oldDriver = Driver.builder()
                .withId(updatingDriverId)
                .withFirstName("Андрей")
                .withLastName("Сухачев")
                .withMonthWorkedHours((short)20)
                .withStatus(DriverStatus.RESTING).build();
        final DriverDto oldDriverDto = new DriverDto();

        oldDriverDto.setPersonalNumber(updatingDriverId);
        oldDriverDto.setFirstName("Андрей");
        oldDriverDto.setLastName("Сухачев");
        oldDriverDto.setMonthWorkedHours((short)20);
        oldDriverDto.setStatus(0);

        when(driverDao.findById(updatingDriverId)).thenReturn(Optional.ofNullable(oldDriver));
        when(driverMapper.mapEntityToDto(oldDriver)).thenReturn(oldDriverDto);

        driverService.updateDriverWhenOrderIsCompleted(updatingDriverId, expectedMonthWorkedHours, expectedLocation);

        assertThat(oldDriverDto.getMonthWorkedHours()).isEqualTo(expectedMonthWorkedHours);
        assertThat(oldDriverDto.getLocation()).isEqualTo(expectedLocation);
        assertThat(oldDriverDto.getStatus()).isEqualTo(expectedStatus);
        assertThat(oldDriverDto.getCurrentOrder()).isNull();
        assertThat(oldDriverDto.getCurrentTruck()).isNull();
    }

    @Test
    void updateDriverWhenOrderIsDeletedShouldSetDriverTruckAndOrderToNullAndUpdateStatus() {
        final Long updatingDriverId = 2L;
        final Integer expectedStatus = DriverStatus.RESTING.ordinal();

        final Driver oldDriver = Driver.builder()
                .withId(updatingDriverId)
                .withFirstName("Андрей")
                .withLastName("Сухачев")
                .withMonthWorkedHours((short)20)
                .withStatus(DriverStatus.RESTING).build();
        final DriverDto oldDriverDto = new DriverDto();

        oldDriverDto.setPersonalNumber(updatingDriverId);
        oldDriverDto.setFirstName("Андрей");
        oldDriverDto.setLastName("Сухачев");
        oldDriverDto.setMonthWorkedHours((short)20);
        oldDriverDto.setStatus(0);

        when(driverDao.findById(updatingDriverId)).thenReturn(Optional.ofNullable(oldDriver));
        when(driverMapper.mapEntityToDto(oldDriver)).thenReturn(oldDriverDto);

        driverService.updateDriverWhenOrderIsDeleted(updatingDriverId);

        assertThat(oldDriverDto.getStatus()).isEqualTo(expectedStatus);
        assertThat(oldDriverDto.getCurrentOrder()).isNull();
        assertThat(oldDriverDto.getCurrentTruck()).isNull();
    }
}
