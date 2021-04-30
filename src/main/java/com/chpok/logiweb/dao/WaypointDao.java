package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Order;

import java.util.List;

public interface WaypointDao extends CrudDao<Order.Waypoint> {
    List<Order.Waypoint> findAllByOrderId(Long orderId);
}
