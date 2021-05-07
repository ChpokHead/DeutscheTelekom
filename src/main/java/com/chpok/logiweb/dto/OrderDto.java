package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.Truck;

import java.util.List;

public class OrderDto {
    private Long id;
    private Boolean isCompleted;
    private Truck currentTruck;
    private List<Driver> currentDrivers;
    private List<Order.Waypoint> waypoints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public List<Driver> getCurrentDrivers() {
        return currentDrivers;
    }

    public void setCurrentDrivers(List<Driver> currentDrivers) {
        this.currentDrivers = currentDrivers;
    }

    public List<Order.Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Order.Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}
