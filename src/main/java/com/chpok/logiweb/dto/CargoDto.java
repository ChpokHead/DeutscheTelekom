package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Order;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CargoDto cargoDto = (CargoDto) o;
        return Objects.equals(id, cargoDto.id) && Objects.equals(name, cargoDto.name) && Objects.equals(weight, cargoDto.weight) && Objects.equals(status, cargoDto.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, weight, status);
    }

}
