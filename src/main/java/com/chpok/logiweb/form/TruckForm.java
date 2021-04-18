package com.chpok.logiweb.form;

import com.chpok.logiweb.model.TruckStatus;

public class TruckForm {
    private Long id;
    private String regNumber;
    private Short driversShift;
    private Short capacity;
    private Integer status;
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Short getDriversShift() {
        return driversShift;
    }

    public void setDriversShift(Short driversShift) {
        this.driversShift = driversShift;
    }

    public Short getCapacity() {
        return capacity;
    }

    public void setCapacity(Short capacity) {
        this.capacity = capacity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
