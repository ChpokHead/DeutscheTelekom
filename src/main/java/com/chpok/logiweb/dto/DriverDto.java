package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Order;

import java.util.Objects;

public class DriverDto {
    private String firstName;
    private String lastName;
    private Long personalNumber;
    private Short monthWorkedHours;
    private Integer status;
    private Location location;
    private TruckDto currentTruck;
    private Order currentOrder;

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TruckDto getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(TruckDto currentTruck) {
        this.currentTruck = currentTruck;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverDto driverDto = (DriverDto) o;
        return firstName.equals(driverDto.firstName) && lastName.equals(driverDto.lastName) && personalNumber.equals(driverDto.personalNumber) && Objects.equals(monthWorkedHours, driverDto.monthWorkedHours) && Objects.equals(status, driverDto.status) && Objects.equals(location, driverDto.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, personalNumber, monthWorkedHours, status, location);
    }

}
