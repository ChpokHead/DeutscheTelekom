package com.chpok.logiweb.dto;

public class WaypointDto {
    private Long id;
    private Integer type;
    private String location;
    private String cargoName;
    private Long orderId;
    private Long cargoId;

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
