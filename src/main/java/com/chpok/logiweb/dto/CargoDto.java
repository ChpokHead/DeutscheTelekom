package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Order;

import java.util.List;

public class CargoDto {
    private Long id;
    private String name;
    private Integer weight;
    private Integer status;
    private List<Order.Waypoint> waypoints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Order.Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Order.Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}
