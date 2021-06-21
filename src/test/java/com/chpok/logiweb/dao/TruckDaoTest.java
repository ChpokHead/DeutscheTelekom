package com.chpok.logiweb.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.model.enums.TruckStatus;
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
class TruckDaoTest {
    @Autowired
    private TruckDao truckDao;

    @Test
    void saveShouldCorrectlySaveTruckEntity() {
        final Truck expected = Truck.builder()
                .withRegNumber("UG32401")
                .withCapacity((short)32)
                .withStatus(TruckStatus.OK)
                .build();

        truckDao.save(expected);

        final Truck actual = truckDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByIdShouldReturnCorrectTruckEntity() {
        final Truck expected = Truck.builder()
                .withRegNumber("TD24231")
                .withCapacity((short)65)
                .withStatus(TruckStatus.BROKEN)
                .build();

        truckDao.save(expected);

        final Truck actual = truckDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllShouldReturnCorrectTruckList() {
        final Truck firstSavingTruck = Truck.builder()
                .withRegNumber("YY18421")
                .withCapacity((short)65)
                .withStatus(TruckStatus.BROKEN)
                .build();

        final Truck secondSavingTruck = Truck.builder()
                .withRegNumber("PO98211")
                .withCapacity((short)12)
                .withStatus(TruckStatus.OK)
                .build();

        truckDao.save(firstSavingTruck);

        truckDao.save(secondSavingTruck);

        final List<Truck> expected = Arrays.asList(firstSavingTruck, secondSavingTruck);

        final List<Truck> actual = truckDao.findAll();

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void updateShouldCorrectlyUpdateTruckEntity() {
        final Truck savingTruck = Truck.builder()
                .withRegNumber("YY18421")
                .withCapacity((short)65)
                .withStatus(TruckStatus.BROKEN)
                .build();

        truckDao.save(savingTruck);

        final Truck expected = Truck.builder()
                .withId(savingTruck.getId())
                .withRegNumber("TD24231")
                .withCapacity((short)15)
                .withStatus(TruckStatus.BROKEN)
                .build();


        truckDao.update(expected);

        final Truck actual = truckDao.findById(savingTruck.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void deleteByIdShouldCorrectlyDeleteTruckEntity() {
        final Truck savingTruck = Truck.builder()
                .withRegNumber("YY18421")
                .withCapacity((short)65)
                .withStatus(TruckStatus.BROKEN)
                .build();

        truckDao.save(savingTruck);

        truckDao.deleteById(savingTruck.getId());

        final Optional<Truck> actual = truckDao.findById(savingTruck.getId());

        assertThat(actual).isEmpty();
    }
}
