package com.chpok.logiweb.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.DriverStatus;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DriverDaoTest {
    @Autowired
    private DriverDao driverDao;

    @Autowired
    private TruckDao truckDao;

    @Test
    void saveShouldCorrectlySaveDriverEntity() {
        final Driver expected = Driver.builder()
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short) 23)
                .withStatus(DriverStatus.RESTING)
                .build();

        driverDao.save(expected);

        final Driver actual = driverDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByIdShouldReturnCorrectDriverEntity() {
        final Driver expected = Driver.builder()
                .withFirstName("Александр")
                .withLastName("Михайлов")
                .withMonthWorkedHours((short) 11)
                .withStatus(DriverStatus.DRIVING)
                .build();

        driverDao.save(expected);

        final Driver actual = driverDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllShouldReturnCorrectDriverList() {
        final Driver firstSavingDriver = Driver.builder()
                .withFirstName("Александр")
                .withLastName("Михайлов")
                .withMonthWorkedHours((short) 11)
                .withStatus(DriverStatus.DRIVING)
                .build();

        final Driver secondSavingDriver = Driver.builder()
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short) 23)
                .withStatus(DriverStatus.RESTING)
                .build();

        driverDao.save(firstSavingDriver);

        driverDao.save(secondSavingDriver);

        final List<Driver> expected = Arrays.asList(firstSavingDriver, secondSavingDriver);

        final List<Driver> actual = driverDao.findAll();

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void updateShouldCorrectlyUpdateDriverEntity() {
        final Driver savingDriver = Driver.builder()
                .withFirstName("Александр")
                .withLastName("Михайлов")
                .withMonthWorkedHours((short) 11)
                .withStatus(DriverStatus.DRIVING)
                .build();

        driverDao.save(savingDriver);

        final Driver expected = Driver.builder()
                .withId(savingDriver.getId())
                .withFirstName("Никита")
                .withLastName("Павлов")
                .withMonthWorkedHours((short) 45)
                .withStatus(DriverStatus.RESTING)
                .build();


        driverDao.update(expected);

        final Driver actual = driverDao.findById(savingDriver.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void deleteByIdShouldCorrectlyDeleteDriverEntity() {
        final Driver savingDriver = Driver.builder()
                .withFirstName("Никита")
                .withLastName("Иванов")
                .withMonthWorkedHours((short) 4)
                .withStatus(DriverStatus.SHIFTING)
                .build();

        driverDao.save(savingDriver);

        driverDao.deleteById(savingDriver.getId());

        final Optional<Driver> actual = driverDao.findById(savingDriver.getId());

        assertThat(actual).isEmpty();
    }

    @Test
    void findByCurrentTruckIdShouldReturnCorrectDriverList() {
        final Truck savedTruck = Truck.builder()
                .withId(1L)
                .withCapacity((short)12)
                .withRegNumber("FG32343")
                .build();

        truckDao.save(savedTruck);

        final Driver firstSavingDriver = Driver.builder()
                .withFirstName("Никита")
                .withLastName("Иванов")
                .withMonthWorkedHours((short) 4)
                .withStatus(DriverStatus.SHIFTING)
                .withCurrentTruck(savedTruck)
                .build();

        final Driver secondSavingDriver = Driver.builder()
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short) 23)
                .withStatus(DriverStatus.RESTING)
                .withCurrentTruck(savedTruck)
                .build();

        driverDao.save(firstSavingDriver);

        driverDao.save(secondSavingDriver);

        final List<Driver> expected = Arrays.asList(firstSavingDriver, secondSavingDriver);

        final List<Driver> actual = driverDao.findByCurrentTruckId(savedTruck.getId());

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllDriversWithoutCurrentOrderShouldReturnCorrectDriverLIst() {
        final Driver firstSavingDriver = Driver.builder()
                .withFirstName("Никита")
                .withLastName("Иванов")
                .withMonthWorkedHours((short) 65)
                .withStatus(DriverStatus.SHIFTING)
                .build();

        final Driver secondSavingDriver = Driver.builder()
                .withFirstName("Михаил")
                .withLastName("Иванов")
                .withMonthWorkedHours((short) 23)
                .withStatus(DriverStatus.RESTING)
                .build();

        driverDao.save(firstSavingDriver);

        driverDao.save(secondSavingDriver);

        final List<Driver> expected = Arrays.asList(firstSavingDriver, secondSavingDriver);

        final List<Driver> actual = driverDao.findAllDriversWithoutCurrentOrder();

        assertThat(actual).isEqualTo(expected);
    }
}
