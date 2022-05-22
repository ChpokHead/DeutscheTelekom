package com.chpok.logiweb.dao;
/*
import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.model.enums.WaypointType;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class WaypointDaoTest {
    @Autowired
    private WaypointDao waypointDao;

    @Autowired
    private CargoDao cargoDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void saveShouldCorrectlySaveWaypointEntity() {
        final Order savingOrder = Order.builder()
                .withStatus(false)
                .withCreationDate(LocalDate.now())
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(savingOrder);

        final Cargo savingCargo = Cargo.builder()
                .withName("Стол")
                .withWeight(400)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        final Order.Waypoint expected = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(savingCargo)
                .withIsDone(false)
                .withType(WaypointType.LOADING)
                .build();

        waypointDao.save(expected);

        final Order.Waypoint actual = waypointDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByIdShouldReturnCorrectWaypointEntity() {
        final Order savingOrder = Order.builder()
                .withStatus(false)
                .withCreationDate(LocalDate.of(2012, 3, 12))
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(savingOrder);

        final Cargo savingCargo = Cargo.builder()
                .withName("Шкафы")
                .withWeight(234)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        final Order.Waypoint expected = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(savingCargo)
                .withIsDone(false)
                .withType(WaypointType.LOADING)
                .build();

        waypointDao.save(expected);

        final Order.Waypoint actual = waypointDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void updateShouldCorrectlyUpdateWaypointEntity() {
        final Order savingOrder = Order.builder()
                .withStatus(false)
                .withCreationDate(LocalDate.of(2012, 3, 12))
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(savingOrder);

        final Cargo savingCargo = Cargo.builder()
                .withName("Шкафы")
                .withWeight(234)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        final Order.Waypoint savingWaypoint = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(savingCargo)
                .withIsDone(false)
                .withType(WaypointType.LOADING)
                .build();

        waypointDao.save(savingWaypoint);

        final Order.Waypoint expected = Order.Waypoint.builder()
                .withId(savingWaypoint.getId())
                .withOrder(savingOrder)
                .withCargo(savingCargo)
                .withIsDone(false)
                .withType(WaypointType.LOADING)
                .build();


        waypointDao.update(expected);

        final Order.Waypoint actual = waypointDao.findById(savingWaypoint.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllShouldReturnCorrectWaypointList() {
        final Order savingOrder = Order.builder()
                .withStatus(false)
                .withCreationDate(LocalDate.of(1990, 7, 4))
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(savingOrder);

        final Cargo savingCargo = Cargo.builder()
                .withName("Телевизоры")
                .withWeight(310)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        final Order.Waypoint firstSavingWaypoint = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(savingCargo)
                .withIsDone(true)
                .withType(WaypointType.LOADING)
                .build();

        final Order.Waypoint secondSavingWaypoint = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(savingCargo)
                .withIsDone(false)
                .withType(WaypointType.SHIPPING)
                .build();

        waypointDao.save(firstSavingWaypoint);

        waypointDao.save(secondSavingWaypoint);

        final List<Order.Waypoint> expected = Arrays.asList(firstSavingWaypoint, secondSavingWaypoint);

        final List<Order.Waypoint> actual = waypointDao.findAll();

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void updateWaypointsShouldCorrectlyUpdateWaypointEntityList() {
        final Order firstSavingOrder = Order.builder()
                .withStatus(false)
                .withCreationDate(LocalDate.of(1990, 7, 4))
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(firstSavingOrder);

        final Cargo firstSavingCargo = Cargo.builder()
                .withName("Телевизоры")
                .withWeight(310)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(firstSavingCargo);

        final Order secondSavingOrder = Order.builder()
                .withStatus(true)
                .withCreationDate(LocalDate.of(2015, 3, 22))
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(secondSavingOrder);

        final Cargo secondSavingCargo = Cargo.builder()
                .withName("Компьютеры")
                .withWeight(230)
                .withStatus(CargoStatus.SHIPPED)
                .build();

        cargoDao.save(secondSavingCargo);

        final Order.Waypoint firstSavingWaypoint = Order.Waypoint.builder()
                .withOrder(firstSavingOrder)
                .withCargo(firstSavingCargo)
                .withIsDone(true)
                .withType(WaypointType.LOADING)
                .build();

        final Order.Waypoint secondSavingWaypoint = Order.Waypoint.builder()
                .withOrder(firstSavingOrder)
                .withCargo(firstSavingCargo)
                .withIsDone(false)
                .withType(WaypointType.SHIPPING)
                .build();

        waypointDao.save(firstSavingWaypoint);

        waypointDao.save(secondSavingWaypoint);

        final Order.Waypoint firstUpdatingWaypoint = Order.Waypoint.builder()
                .withId(firstSavingWaypoint.getId())
                .withOrder(secondSavingOrder)
                .withCargo(secondSavingCargo)
                .withIsDone(false)
                .withType(WaypointType.LOADING)
                .build();

        final Order.Waypoint secondUpdatingWaypoint = Order.Waypoint.builder()
                .withId(secondSavingWaypoint.getId())
                .withOrder(secondSavingOrder)
                .withCargo(secondSavingCargo)
                .withIsDone(true)
                .withType(WaypointType.SHIPPING)
                .build();

        final List<Order.Waypoint> expected = Arrays.asList(firstUpdatingWaypoint, secondUpdatingWaypoint);

        waypointDao.updateWaypoints(expected);

        final List<Order.Waypoint> actual = waypointDao.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteByIdShouldCorrectlyDeleteWaypointEntity() {
        final Order savingOrder = Order.builder()
                .withStatus(false)
                .withCreationDate(LocalDate.of(1990, 7, 4))
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(savingOrder);

        final Cargo savingCargo = Cargo.builder()
                .withName("Телевизоры")
                .withWeight(310)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        final Order.Waypoint savingWaypoint = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(savingCargo)
                .withIsDone(true)
                .withType(WaypointType.LOADING)
                .build();

        waypointDao.save(savingWaypoint);

        waypointDao.deleteById(savingWaypoint.getId());

        Optional<Order.Waypoint> actual = waypointDao.findById(savingWaypoint.getId());

        assertThat(actual).isEmpty();
    }

    @Test
    void findAllByOrderIdShouldReturnCorrectWaypointEntityList() {
        final Order savingOrder = Order.builder()
                .withStatus(false)
                .withCreationDate(LocalDate.of(1990, 7, 4))
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(savingOrder);

        final Cargo firstSavingCargo = Cargo.builder()
                .withName("Телевизоры")
                .withWeight(310)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(firstSavingCargo);

        final Cargo secondSavingCargo = Cargo.builder()
                .withName("Компьютеры")
                .withWeight(230)
                .withStatus(CargoStatus.SHIPPED)
                .build();

        cargoDao.save(secondSavingCargo);

        final Order.Waypoint firstSavingWaypoint = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(firstSavingCargo)
                .withIsDone(true)
                .withType(WaypointType.LOADING)
                .build();

        final Order.Waypoint secondSavingWaypoint = Order.Waypoint.builder()
                .withOrder(savingOrder)
                .withCargo(secondSavingCargo)
                .withIsDone(false)
                .withType(WaypointType.SHIPPING)
                .build();

        waypointDao.save(firstSavingWaypoint);

        waypointDao.save(secondSavingWaypoint);

        List<Order.Waypoint> expected = Arrays.asList(firstSavingWaypoint, secondSavingWaypoint);

        List<Order.Waypoint> actual = waypointDao.findAllByOrderId(savingOrder.getId());

        assertThat(actual).isEqualTo(expected);
    }
}*/