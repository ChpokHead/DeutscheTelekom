package com.chpok.logiweb.service;

import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.dao.LocationMapDao;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.LocationMap;
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
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class LocationMapServiceTest {
    @Mock
    private LocationMapDao locationMapDao;
    @Autowired
    @InjectMocks
    private LocationMapService locationMapService;

    @Test
    void getDistanceBetweenLocationsByIdsShouldReturnCorrectDistanceIfIdsArePresent() {
        final short expected = 706;
        final LocationMap foundLocationMap =
                new LocationMap(1L, new Location(8L, "Москва"),
                        new Location(9L, "Санкт-Петербург"), expected);

        when(locationMapDao.findByStartingAndEndingLocationsIds(8L, 9L)).thenReturn(Optional.of(foundLocationMap));

        final short actual = locationMapService.getDistanceBetweenLocationsByIds(8L, 9L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDistanceBetweenLocationsByIdsShouldThrowEntityNotFoundExceptionIfIdIsAreNotPresent() {
        when(locationMapDao.findByStartingAndEndingLocationsIds(null, null)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> locationMapService.getDistanceBetweenLocationsByIds(null, null));
    }
}
