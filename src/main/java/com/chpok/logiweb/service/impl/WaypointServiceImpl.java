package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.WaypointDao;
import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.dto.WaypointsPair;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.model.enums.WaypointType;
import com.chpok.logiweb.service.CargoService;
import com.chpok.logiweb.service.WaypointService;
import com.chpok.logiweb.mapper.impl.WaypointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WaypointServiceImpl implements WaypointService {
    private final WaypointDao waypointDao;
    private final WaypointMapper waypointMapper;

    @Autowired
    private CargoService cargoService;

    public WaypointServiceImpl(WaypointDao waypointDao, WaypointMapper waypointMapper) {
        this.waypointDao = waypointDao;
        this.waypointMapper = waypointMapper;
    }

    @Override
    public List<WaypointDto> getAllWaypoints() {
        return waypointDao.findAll().stream().map(waypointMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public WaypointDto getWaypointById(Long id) {
        return null;
    }

    @Override
    public void saveWaypoint(WaypointDto waypoint) {
        waypointDao.save(waypointMapper.mapDtoToEntity(waypoint));
    }

    @Override
    public void deleteWaypoint(Long id) {
        waypointDao.deleteById(id);
    }

    @Override
    public void updateWaypoint(WaypointDto waypoint) {
        waypointDao.update(waypointMapper.mapDtoToEntity(waypoint));
    }

    @Override
    public List<WaypointDto> getAllWaypointsByOrderId(Long id) {
        if (id != null) {
            return waypointDao.findAllByOrderId(id).stream().map(waypointMapper::mapEntityToDto).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<WaypointsPair> getAllWaypointsPairByOrderId(Long id) {
        final List<WaypointDto> waypoints = getAllWaypointsByOrderId(id);
        final List<WaypointsPair> waypointsPairs = new ArrayList<>();

        for (int i = 0; i < waypoints.size(); i++) {
            final WaypointsPair pair = new WaypointsPair();

            pair.getPair().add(waypoints.get(i));
            pair.getPair().add(waypoints.get(i + 1));

            waypointsPairs.add(pair);
            
            i++;
        }

        return waypointsPairs;
    }

    @Override
    public void updateWaypointsStatus(List<Order.Waypoint> waypoints) {
        final List<Order.Waypoint> updatedWaypoints = new ArrayList<>();

        for (Order.Waypoint waypoint : waypoints) {
            final Order.Waypoint updatingWaypoint = waypointDao.findById(waypoint.getId()).get();

            updatingWaypoint.setIsDone(waypoint.getIsDone());

            updateWaypointCargoStatus(updatingWaypoint);

            updatedWaypoints.add(updatingWaypoint);
        }

        updateWaypoints(updatedWaypoints);
    }

    private void updateWaypointCargoStatus(Order.Waypoint waypoint) {
        if (waypoint.getType().equals(WaypointType.LOADING)) {
            cargoService.updateCargoStatus(waypoint.getCargo().getId(), CargoStatus.SHIPPED);
        } else {
            cargoService.updateCargoStatus(waypoint.getCargo().getId(), CargoStatus.DELIVERED);
        }
    }

    @Override
    public void updateWaypoints(List<Order.Waypoint> waypoints) {
        for (Order.Waypoint waypoint : waypoints) {
            waypointDao.update(waypoint);
        }
    }

    @Override
    public boolean checkAllWaypointsComplete(List<Order.Waypoint> waypoints) {
        for (Order.Waypoint waypoint : waypoints) {
            if (Boolean.FALSE.equals(waypoint.getIsDone())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<WaypointDto> getAllLoadingWaypointsByOrderId(Long orderId) {
        if (orderId != null) {
            List<Order.Waypoint> orderWaypoints = waypointDao.findAllByOrderId(orderId);

            if (orderWaypoints.isEmpty()) {
                return Collections.emptyList();
            } else {
                return orderWaypoints.stream()
                        .filter(waypoint -> waypoint.getType() == WaypointType.LOADING)
                        .map(waypointMapper::mapEntityToDto).collect(Collectors.toList());
            }
        } else {
            return Collections.emptyList();
        }
    }

}
