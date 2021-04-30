package com.chpok.logiweb.dto;

import java.util.List;

public class OrderDto {
    private Long id;
    private Boolean isCompleted;
    private String currentTruckRegNum;
    private List<Long> currentDrivers;
    private List<WaypointDto> waypoints;

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

    public String getCurrentTruckRegNum() {
        return currentTruckRegNum;
    }

    public void setCurrentTruckRegNum(String currentTruckRegNum) {
        this.currentTruckRegNum = currentTruckRegNum;
    }

    public List<Long> getCurrentDrivers() {
        return currentDrivers;
    }

    public void setCurrentDrivers(List<Long> currentDrivers) {
        this.currentDrivers = currentDrivers;
    }

    public List<WaypointDto> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<WaypointDto> waypoints) {
        this.waypoints = waypoints;
    }
}
