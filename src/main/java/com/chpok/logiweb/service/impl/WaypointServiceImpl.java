package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.WaypointDao;
import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.dto.WaypointsPair;
import com.chpok.logiweb.service.WaypointService;
import com.chpok.logiweb.service.mapper.WaypointMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WaypointServiceImpl implements WaypointService {
    private final WaypointDao waypointDao;
    private final WaypointMapper waypointMapper;

    public WaypointServiceImpl(WaypointDao waypointDao, WaypointMapper waypointMapper) {
        this.waypointDao = waypointDao;
        this.waypointMapper = waypointMapper;
    }

    @Override
    public List<WaypointDto> getAllWaypoints() {
        return waypointDao.findAll().stream().map(waypointMapper::mapWaypointToWaypointDto).collect(Collectors.toList());
    }

    @Override
    public void saveWaypoint(WaypointDto waypoint) {
        waypointDao.save(waypointMapper.mapWaypointDtoToWaypoint(waypoint));
    }

    @Override
    public void deleteWaypoint(Long id) {
        if (id != null) {
            waypointDao.deleteById(id);
        }
    }

    @Override
    public List<WaypointDto> getAllWaypointsByOrderId(Long id) {
        if (id != null) {
            return waypointDao.findAllByOrderId(id).stream().map(waypointMapper::mapWaypointToWaypointDto).collect(Collectors.toList());
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
}
