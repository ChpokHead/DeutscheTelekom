package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.dto.WaypointsPair;

import java.util.List;

public interface WaypointService {
    List<WaypointDto> getAllWaypoints();
    void saveWaypoint(WaypointDto waypoint);
    void deleteWaypoint(Long id);
    List<WaypointDto> getAllWaypointsByOrderId(Long id);
    List<WaypointsPair> getAllWaypointsPairByOrderId(Long id);
}
