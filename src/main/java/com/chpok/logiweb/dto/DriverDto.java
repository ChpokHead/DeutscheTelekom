package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Location;

public class DriverDto {
    private String firstName;
    private String lastName;
    private Long personalNumber;
    private Short monthWorkedHours;
    private Integer status;
    private String location;
    private String currentTruck;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(Long personalNumber) {
        this.personalNumber = personalNumber;
    }

    public Short getMonthWorkedHours() {
        return monthWorkedHours;
    }

    public void setMonthWorkedHours(Short monthWorkedHours) {
        this.monthWorkedHours = monthWorkedHours;
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

    public String getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(String currentTruck) {
        this.currentTruck = currentTruck;
    }
}
