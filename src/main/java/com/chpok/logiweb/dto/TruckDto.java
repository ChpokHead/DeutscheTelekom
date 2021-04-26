package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Location;

public class TruckDto {
    private Long id;
    private String regNumber;
    private Short driversShift;
    private Short capacity;
    private Integer status;
    private String location;
    //private List<Long> currentDrivers;

    public TruckDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public Short getDriversShift() {
        return driversShift;
    }

    public Short getCapacity() {
        return capacity;
    }

    public Integer getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

   /* public List<Long> getCurrentDrivers() {
        return currentDrivers;
    }*/

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public void setDriversShift(Short driversShift) {
        this.driversShift = driversShift;
    }

    public void setCapacity(Short capacity) {
        this.capacity = capacity;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }
/*
    public void setCurrentDrivers(List<Long> currentDrivers) {
        this.currentDrivers = currentDrivers;
    }*/
}
