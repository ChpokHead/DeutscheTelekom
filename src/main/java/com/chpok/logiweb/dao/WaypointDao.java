package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Order;

import java.util.List;
import java.util.Optional;

public interface WaypointDao extends CrudDao<Order.Waypoint> {
    List<Order.Waypoint> findAllByOrderId(Long orderId);
    Optional<Order.Waypoint> findById(Long id);
    void updateWaypoints(List<Order.Waypoint> waypoints);
}
