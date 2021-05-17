package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.dto.WaypointsPair;
import com.chpok.logiweb.model.Order;

import java.util.List;

public interface WaypointService {
    List<WaypointDto> getAllWaypoints();
    WaypointDto getWaypointById(Long id);
    void saveWaypoint(WaypointDto waypoint);
    void deleteWaypoint(Long id);
    void updateWaypoint(WaypointDto waypoint);
    List<WaypointDto> getAllWaypointsByOrderId(Long id);
    List<WaypointsPair> getAllWaypointsPairByOrderId(Long id);
    void updateWaypointsStatus(List<Order.Waypoint> waypoints);
    void updateWaypoints(List<Order.Waypoint> waypoints);
    boolean checkAllWaypointsComplete(List<Order.Waypoint> waypoints);
    List<WaypointDto> getAllLoadingWaypointsByOrderId(Long orderId);
}
