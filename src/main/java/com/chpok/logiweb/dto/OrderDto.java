package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.Truck;
import java.time.LocalDate;
import java.util.List;

public class OrderDto {
    private Long id;
    private Boolean isCompleted;
    private Truck currentTruck;
    private List<Driver> currentDrivers;
    private List<Order.Waypoint> waypoints;
    private LocalDate creationDate;
    private LocalDate startDate;
    private LocalDate endDate;

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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
