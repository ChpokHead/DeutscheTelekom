package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.Truck;
import org.codehaus.jackson.annotate.JsonIgnore;

public class DriverDto {
    private String firstName;
    private String lastName;
    private Long personalNumber;
    private Short monthWorkedHours;
    private Short nextMonthWorkedHours;
    private Boolean isMonthChanged;
    private Integer status;
    private Location location;
    @JsonIgnore
    private Truck currentTruck;
    @JsonIgnore
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

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Short getNextMonthWorkedHours() {
        return nextMonthWorkedHours;
    }

    public void setNextMonthWorkedHours(Short nextMonthWorkedHours) {
        this.nextMonthWorkedHours = nextMonthWorkedHours;
    }

    public Boolean getMonthChanged() {
        return isMonthChanged;
    }

    public void setMonthChanged(Boolean monthChanged) {
        isMonthChanged = monthChanged;
    }
}
