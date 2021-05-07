package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Order;

public class WaypointDto {
    private Long id;
    private Integer type;
    private Location location;
    private Order order;
    private Cargo cargo;
    private Boolean isDone;

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
