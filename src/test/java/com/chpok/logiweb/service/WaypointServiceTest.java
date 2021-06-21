package com.chpok.logiweb.service;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.dao.WaypointDao;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.dto.WaypointsPair;
import com.chpok.logiweb.mapper.impl.WaypointMapper;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.model.enums.WaypointType;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class WaypointServiceTest {
    @Mock
    private WaypointMapper waypointMapper;
    @Mock
    private WaypointDao waypointDao;
    @Mock
    private CargoService cargoService;
    @Autowired
    @InjectMocks
    private WaypointService waypointService;

    @Test
    void saveWaypointShouldProperlySaveWaypoint() {
        final Order.Waypoint savingWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.SHIPPING)
                .withIsDone(false).build();

        WaypointDto savingWaypointDto = new WaypointDto();
        savingWaypointDto.setId(1L);
        savingWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        savingWaypointDto.setIsDone(false);

        when(waypointMapper.mapDtoToEntity(savingWaypointDto)).thenReturn(savingWaypoint);

        when(waypointDao.save(savingWaypoint)).thenReturn(savingWaypoint.getId());

        waypointService.saveWaypoint(savingWaypointDto);

        verify(waypointDao).save(savingWaypoint);
    }

    @Test
    void deleteWaypointShouldProperlyDeleteWaypointById() {
        final Long deletingWaypointId = 1L;

        doNothing().when(waypointDao).deleteById(deletingWaypointId);

        waypointService.deleteWaypoint(deletingWaypointId);

        verify(waypointDao).deleteById(deletingWaypointId);
    }

    @Test
    void updateWaypointShouldProperlyUpdateWaypoint() {
        final Order.Waypoint updatingWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withIsDone(true).build();

        WaypointDto updatingWaypointDto = new WaypointDto();
        updatingWaypointDto.setId(2L);
        updatingWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        updatingWaypointDto.setIsDone(true);

        when(waypointMapper.mapDtoToEntity(updatingWaypointDto)).thenReturn(updatingWaypoint);

        doNothing().when(waypointDao).update(updatingWaypoint);

        waypointService.updateWaypoint(updatingWaypointDto);

        verify(waypointDao).update(updatingWaypoint);
    }

    @Test
    void getAllWaypointsByOrderIdShouldReturnCorrectWaypointListIfOrderIdIsValid() {
        final Long orderId = 10L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(true).build();

        WaypointDto firstWaypointDto = new WaypointDto();
        firstWaypointDto.setId(2L);
        firstWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        firstWaypointDto.setOrder(Order.builder().withId(orderId).build());
        firstWaypointDto.setIsDone(true);

        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(false).build();

        WaypointDto secondWaypointDto = new WaypointDto();
        secondWaypointDto.setId(5L);
        secondWaypointDto.setType(WaypointType.LOADING.ordinal());
        secondWaypointDto.setOrder(Order.builder().withId(orderId).build());
        secondWaypointDto.setIsDone(false);

        when(waypointDao.findAllByOrderId(orderId)).thenReturn(Arrays.asList(firstWaypoint, secondWaypoint));

        when(waypointMapper.mapEntityToDto(firstWaypoint)).thenReturn(firstWaypointDto);
        when(waypointMapper.mapEntityToDto(secondWaypoint)).thenReturn(secondWaypointDto);

        final List<WaypointDto> expected = Arrays.asList(firstWaypointDto, secondWaypointDto);
        final List<WaypointDto> actual = waypointService.getAllWaypointsByOrderId(orderId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllWaypointsByOrderIdShouldReturnEmptyListIfOrderIdIsNotValid() {
        final Long orderId = null;

        List<WaypointDto> actual = waypointService.getAllWaypointsByOrderId(orderId);

        assertThat(actual).isEmpty();
    }

    @Test
    void getAllWaypointsPairByOrderIdShouldReturnCorrectWaypointPairs() {
        final Long orderId = 10L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(true).build();

        WaypointDto firstWaypointDto = new WaypointDto();
        firstWaypointDto.setId(2L);
        firstWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        firstWaypointDto.setOrder(Order.builder().withId(orderId).build());
        firstWaypointDto.setIsDone(true);

        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(false).build();

        WaypointDto secondWaypointDto = new WaypointDto();
        secondWaypointDto.setId(5L);
        secondWaypointDto.setType(WaypointType.LOADING.ordinal());
        secondWaypointDto.setOrder(Order.builder().withId(orderId).build());
        secondWaypointDto.setIsDone(false);

        when(waypointDao.findAllByOrderId(orderId)).thenReturn(Arrays.asList(firstWaypoint, secondWaypoint));

        when(waypointMapper.mapEntityToDto(firstWaypoint)).thenReturn(firstWaypointDto);
        when(waypointMapper.mapEntityToDto(secondWaypoint)).thenReturn(secondWaypointDto);

        WaypointsPair pair = new WaypointsPair();

        pair.setPair(Arrays.asList(firstWaypointDto, secondWaypointDto));

        List<WaypointsPair> expected = Collections.singletonList(pair);

        List<WaypointsPair> actual = waypointService.getAllWaypointsPairByOrderId(orderId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateWaypointsShouldProperlyUpdateWaypointList() {
        final Long orderId = 10L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(true).build();

        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(false).build();

        waypointService.updateWaypoints(Arrays.asList(firstWaypoint, secondWaypoint));

        verify(waypointDao).update(firstWaypoint);
        verify(waypointDao).update(secondWaypoint);
    }

    @Test
    void updateWaypointsStatusShouldProperlyUpdateWaypointsStatus() {
        final Long orderId = 10L;
        final Order.Waypoint newFirstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(true).build();

        final Order.Waypoint newSecondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(true).build();

        final Order.Waypoint oldFirstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(false).build();

        WaypointDto oldFirstWaypointDto = new WaypointDto();
        oldFirstWaypointDto.setId(2L);
        oldFirstWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        oldFirstWaypointDto.setOrder(Order.builder().withId(orderId).build());
        oldFirstWaypointDto.setCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build());
        oldFirstWaypointDto.setIsDone(false);

        final Order.Waypoint oldSecondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(false).build();

        WaypointDto oldSecondWaypointDto = new WaypointDto();
        oldSecondWaypointDto.setId(5L);
        oldSecondWaypointDto.setType(WaypointType.LOADING.ordinal());
        oldSecondWaypointDto.setOrder(Order.builder().withId(orderId).build());
        oldSecondWaypointDto.setCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build());
        oldSecondWaypointDto.setIsDone(false);

        when(waypointDao.findById(oldFirstWaypoint.getId())).thenReturn(Optional.of(oldFirstWaypoint));
        when(waypointDao.findById(oldSecondWaypoint.getId())).thenReturn(Optional.of(oldSecondWaypoint));

        doNothing().when(cargoService).updateCargoStatus(oldFirstWaypoint.getCargo().getId(), CargoStatus.DELIVERED);
        doNothing().when(cargoService).updateCargoStatus(oldSecondWaypoint.getCargo().getId(), CargoStatus.SHIPPED);

        waypointService.updateWaypointsStatus(Arrays.asList(newFirstWaypoint, newSecondWaypoint));

        verify(waypointDao).update(oldFirstWaypoint);
        verify(waypointDao).update(oldSecondWaypoint);

        assertThat(oldFirstWaypoint.getIsDone()).isTrue();
        assertThat(oldSecondWaypoint.getIsDone()).isTrue();
    }

    @Test
    void checkAllWaypointsCompleteShouldReturnTrueIfAllWaypointsAreDone() {
        final Long orderId = 10L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(true).build();

        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(true).build();

        assertThat(waypointService.checkAllWaypointsComplete(Arrays.asList(firstWaypoint, secondWaypoint))).isTrue();
    }

    @Test
    void checkAllWaypointsCompleteShouldReturnFalseIfNotAllWaypointsAreDone() {
        final Long orderId = 10L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(true).build();

        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withCargo(Cargo.builder().withId(1L).withName("Столы").withWeight(120).build())
                .withIsDone(false).build();

        assertThat(waypointService.checkAllWaypointsComplete(Arrays.asList(firstWaypoint, secondWaypoint))).isFalse();
    }

    @Test
    void getAllLoadingWaypointsByOrderIdShouldReturnCorrectWaypointList() {
        final Long orderId = 10L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(true).build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(5L)
                .withType(WaypointType.LOADING)
                .withOrder(Order.builder().withId(orderId).build())
                .withIsDone(false).build();

        when(waypointDao.findAllByOrderId(orderId)).thenReturn(Arrays.asList(firstWaypoint, secondWaypoint));

        WaypointDto firstWaypointDto = new WaypointDto();
        firstWaypointDto.setId(2L);
        firstWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        firstWaypointDto.setOrder(Order.builder().withId(orderId).build());
        firstWaypointDto.setIsDone(true);

        WaypointDto secondWaypointDto = new WaypointDto();
        secondWaypointDto.setId(5L);
        secondWaypointDto.setType(WaypointType.LOADING.ordinal());
        secondWaypointDto.setOrder(Order.builder().withId(orderId).build());
        secondWaypointDto.setIsDone(false);

        when(waypointDao.findAllByOrderId(orderId)).thenReturn(Arrays.asList(firstWaypoint, secondWaypoint));

        when(waypointMapper.mapEntityToDto(secondWaypoint)).thenReturn(secondWaypointDto);

        final List<WaypointDto> expected = Collections.singletonList(secondWaypointDto);

        final List<WaypointDto> actual = waypointService.getAllLoadingWaypointsByOrderId(orderId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllWaypointsShouldReturnEmptyListIfOrderIdIsNull() {
        final Long orderId = null;

        assertThat(waypointService.getAllLoadingWaypointsByOrderId(orderId)).isEmpty();
    }

    @Test
    void getWaypointByIdShouldReturnCorrectWaypointIfIdIsPresent() {
        final Long findingWaypointId = 1L;

        final Order.Waypoint foundWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withIsDone(true).build();

        WaypointDto foundWaypointDto = new WaypointDto();
        foundWaypointDto.setId(2L);
        foundWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        foundWaypointDto.setIsDone(true);

        when(waypointDao.findById(findingWaypointId)).thenReturn(Optional.of(foundWaypoint));

        when(waypointMapper.mapEntityToDto(foundWaypoint)).thenReturn(foundWaypointDto);

        final WaypointDto actual = waypointService.getWaypointById(findingWaypointId);

        assertThat(actual).isEqualTo(foundWaypointDto);
    }

    @Test
    void getWaypointByIdShouldThrowEntityNotFoundExceptionIfIdIsNotPresent() {
        final Long findingWaypointId = 1L;

        when(waypointDao.findById(findingWaypointId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> waypointService.getWaypointById(findingWaypointId));
    }

}
