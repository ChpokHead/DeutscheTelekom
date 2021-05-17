package com.chpok.logiweb.dao;

import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Employee;
import com.chpok.logiweb.model.Location;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LocationDaoTest {
    @Autowired
    private LocationDao locationDao;

    @Test
    void saveShouldCorrectlySaveLocationEntity() {
        final Location expected = new Location("Москва");

        locationDao.save(expected);

        final Location actual = locationDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByIdShouldReturnCorrectLocationEntity() {
        final Location expected = new Location("Тула");

        locationDao.save(expected);

        final Location actual = locationDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnCorrectLocationList() {
        final Location firstSavingLocation = new Location("Санкт-Петербург");

        final Location secondSavingLocation = new Location("Москва");

        locationDao.save(firstSavingLocation);

        locationDao.save(secondSavingLocation);

        final List<Location> expected = Arrays.asList(firstSavingLocation, secondSavingLocation);

        final List<Location> actual = locationDao.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByNameShouldReturnCorrectLocationEntity() {
        final Location expected = new Location("Санкт-Петербург");

        locationDao.save(expected);

        final Location actual = locationDao.findByName(expected.getName()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }
}
