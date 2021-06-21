package com.chpok.logiweb.dao;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.LocationMap;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LocationMapDaoTest {
    @Autowired
    private LocationMapDao locationMapDao;

    @Autowired
    private LocationDao locationDao;

    @Test
    void saveShouldCorrectlySaveLocationEntity() {
        final Location firstSavingLocation = new Location("Москва");
        final Location secondSavingLocation = new Location("Казань");

        final LocationMap expected = new LocationMap(firstSavingLocation, secondSavingLocation, (short)1012);

        locationDao.save(firstSavingLocation);

        locationDao.save(secondSavingLocation);

        locationMapDao.save(expected);

        final LocationMap actual = locationMapDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByIdShouldReturnCorrectLocationMapEntity() {
        final Location firstSavingLocation = new Location("Оренбург");
        final Location secondSavingLocation = new Location("Волгоград");

        locationDao.save(firstSavingLocation);

        locationDao.save(secondSavingLocation);

        final LocationMap expected = new LocationMap(firstSavingLocation, secondSavingLocation, (short)560);

        locationMapDao.save(expected);

        final LocationMap actual = locationMapDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByStartingAndEndingLocationsIdsShouldReturnCorrectLocationMapEntity() {
        final Location firstSavingLocation = new Location("Крым");
        final Location secondSavingLocation = new Location("Сочи");

        locationDao.save(firstSavingLocation);

        locationDao.save(secondSavingLocation);

        final LocationMap expected = new LocationMap(firstSavingLocation, secondSavingLocation, (short)560);

        locationMapDao.save(expected);

        final LocationMap actual = locationMapDao.findByStartingAndEndingLocationsIds(expected.getStartingLocation().getId(), expected.getEndingLocation().getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

}
