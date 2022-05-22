package com.chpok.logiweb.service;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.dao.WaypointDao;
import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.mapper.impl.CargoMapper;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.model.enums.WaypointType;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class CargoServiceTest {
    @Mock
    private CargoDao cargoDao;

    @Mock
    private WaypointDao waypointDao;

    @Autowired
    @InjectMocks
    private WaypointService waypointService;

    @Autowired
    private CargoMapper cargoMapper;

    @Autowired
    @InjectMocks
    private CargoService cargoService;

    @Test
    void getAllCargosShouldReturnCorrectCargoDtoList() {
        final Cargo firstCargo = Cargo.builder()
                .withId(1L)
                .withName("Столы")
                .withWeight(126)
                .withStatus(CargoStatus.PREPARED).build();
        final Cargo secondCargo = Cargo.builder()
                .withId(2L)
                .withName("Стулья")
                .withWeight(243).withStatus(CargoStatus.PREPARED)
                .build();
        final List<CargoDto> expected =
                Arrays.asList(cargoMapper.mapEntityToDto(firstCargo), cargoMapper.mapEntityToDto(secondCargo));

        when(cargoDao.findAll()).thenReturn(Arrays.asList(firstCargo, secondCargo));

        final List<CargoDto> actual = cargoService.getAllCargos();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveCargoShouldNotThrowInvalidEntityExceptionOnSaving() {
        final Cargo savingCargo = Cargo.builder()
                .withName("Коробки")
                .withWeight(43).withStatus(CargoStatus.SHIPPED)
                .build();

        when(cargoDao.save(savingCargo)).thenReturn(2L);

        assertThatNoException().isThrownBy(() -> cargoService.saveCargo(cargoMapper.mapEntityToDto(savingCargo)));
    }

    @Test
    void getCargoByIdShouldReturnCorrectCargoDto() {
        final Cargo foundCargo = Cargo.builder()
                .withId(1L)
                .withName("Столы")
                .withWeight(126)
                .withStatus(CargoStatus.PREPARED).build();
        final CargoDto expected = cargoMapper
                .mapEntityToDto(foundCargo);

        when(cargoDao.findById(1L)).thenReturn(Optional.of(foundCargo));

        final CargoDto actual = cargoService.getCargoById(foundCargo.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteCargoShouldCorrectlyDeleteCargoWithoutWaypoints() {
        final Cargo deletingCargo = Cargo.builder()
                .withId(1L)
                .withName("Стулья")
                .withWeight(12)
                .withWaypoints(Collections.emptyList())
                .withStatus(CargoStatus.PREPARED).build();
        final Long deletingCargoId = deletingCargo.getId();

        when(cargoDao.findById(deletingCargoId)).thenReturn(Optional.of(deletingCargo));

        doNothing().when(cargoDao).deleteById(deletingCargoId);

        cargoService.deleteCargo(deletingCargoId);

        when(cargoDao.findById(deletingCargoId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class).
                isThrownBy(() -> cargoService.getCargoById(deletingCargoId));
    }

    @Test
    void deleteCargoShouldCorrectlyDeleteCargoAndItWaypoints() {
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.LOADING)
                .build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .build();
        final Cargo deletingCargo = Cargo.builder()
                .withId(1L)
                .withName("Стулья")
                .withWeight(12)
                .withWaypoints(Arrays.asList(firstWaypoint, secondWaypoint))
                .withStatus(CargoStatus.PREPARED).build();
        final Long deletingCargoId = deletingCargo.getId();
        final Long firstDeletingWaypointId = firstWaypoint.getId();
        final Long secondDeletingWaypointId = secondWaypoint.getId();

        when(cargoDao.findById(deletingCargoId)).thenReturn(Optional.of(deletingCargo));

        doNothing().when(waypointDao).deleteById(firstDeletingWaypointId);
        doNothing().when(waypointDao).deleteById(secondDeletingWaypointId);

        cargoService.deleteCargo(deletingCargoId);

        when(cargoDao.findById(deletingCargoId)).thenReturn(Optional.empty());
        when(waypointDao.findById(firstWaypoint.getId())).thenReturn(Optional.empty());
        when(waypointDao.findById(secondWaypoint.getId())).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class).
                isThrownBy(() -> cargoService.getCargoById(deletingCargoId));

        assertThatExceptionOfType(EntityNotFoundException.class).
                isThrownBy(() -> waypointService.getWaypointById(firstDeletingWaypointId));
        assertThatExceptionOfType(EntityNotFoundException.class).
                isThrownBy(() -> waypointService.getWaypointById(secondDeletingWaypointId));
    }

    @Test
    void updateShouldCorrectlyUpdateCargo() {
        Cargo updatedCargo = Cargo.builder()
                .withId(1L)
                .withName("Кровати")
                .withWeight(60)
                .withStatus(CargoStatus.SHIPPED).build();

        doNothing().when(cargoDao).update(updatedCargo);

        assertThatNoException().isThrownBy(() -> cargoService.updateCargo(cargoMapper.mapEntityToDto(updatedCargo)));
    }

    @Test
    void updateShouldThrowInvalidEntityExceptionIfCargoIsNotCorrect() {
        Cargo updatedCargo = Cargo.builder()
                .withId(1L)
                .withName("Кровати")
                .withWeight(1000000)
                .withStatus(CargoStatus.SHIPPED).build();

        assertThatExceptionOfType(InvalidEntityException.class).isThrownBy(() -> cargoService.updateCargo(cargoMapper.mapEntityToDto(updatedCargo)));
    }

    @Test
    void getUnoccupiedCargosShouldReturnCargosWithoutWaypoints() {
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.LOADING)
                .build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .build();
        final Cargo firstCargo = Cargo.builder()
                .withId(1L)
                .withName("Стулья")
                .withWeight(12)
                .withWaypoints(Arrays.asList(firstWaypoint, secondWaypoint))
                .withStatus(CargoStatus.PREPARED).build();
        Cargo secondCargo = Cargo.builder()
                .withId(1L)
                .withName("Кровати")
                .withWeight(100)
                .withWaypoints(Collections.emptyList())
                .withStatus(CargoStatus.SHIPPED).build();

        when(cargoDao.findAll()).thenReturn(Arrays.asList(firstCargo, secondCargo));

        final List<CargoDto> expected = Collections.singletonList(cargoMapper.mapEntityToDto(secondCargo));
        final List<CargoDto> actual = cargoService.getUnoccupiedCargos();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateCargoStatusShouldCorrectlyUpdateCargoStatus() {
        final Long updatingCargoId = 2L;
        final CargoStatus expected = CargoStatus.DELIVERED;
        final Cargo oldCargo = Cargo.builder()
                .withId(updatingCargoId)
                .withName("Банки")
                .withWeight(45)
                .withWaypoints(Collections.emptyList())
                .withStatus(CargoStatus.SHIPPED).build();

        when(cargoDao.findById(updatingCargoId)).thenReturn(Optional.of(oldCargo));

        cargoService.updateCargoStatus(updatingCargoId, expected);

        final CargoStatus actual = oldCargo.getStatus();

        assertThat(actual).isEqualTo(expected);
    }

}
