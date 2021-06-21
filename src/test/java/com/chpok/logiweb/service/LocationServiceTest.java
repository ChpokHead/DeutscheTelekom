package com.chpok.logiweb.service;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dto.LocationDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.mapper.impl.LocationMapper;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.TruckStatus;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class LocationServiceTest {
    @Mock
    private LocationMapper locationMapper;
    @Mock
    private LocationDao locationDao;
    @Autowired
    @InjectMocks
    private LocationService locationService;

    @Test
    void getAllLocationsShouldReturnCorrectLocationList() {
        final Location firstLocation = new Location(9L, "Москва");

        final LocationDto firstLocationDto = new LocationDto();

        firstLocationDto.setId(firstLocation.getId());
        firstLocationDto.setName(firstLocation.getName());

        final Location secondLocation = new Location(10L, "Омск");

        final LocationDto secondLocationDto = new LocationDto();

        secondLocationDto.setId(secondLocation.getId());
        secondLocationDto.setName(secondLocation.getName());

        final List<LocationDto> expected =
                Arrays.asList(firstLocationDto, secondLocationDto);

        when(locationDao.findAll()).thenReturn(Arrays.asList(firstLocation, secondLocation));
        when(locationMapper.mapEntityToDto(firstLocation)).thenReturn(firstLocationDto);
        when(locationMapper.mapEntityToDto(secondLocation)).thenReturn(secondLocationDto);

        final List<LocationDto> actual = locationService.getAllLocations();

        assertThat(actual).isEqualTo(expected);
    }
}
