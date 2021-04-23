package com.chpok.logiweb.dto;

public class DriverDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String personalNumber;
    private Short monthWorkedHours;
    private Integer status;
    private String location;
    private Long truckId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
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

    public Long getTruckId() {
        return truckId;
    }

    public void setTruckId(Long truckId) {
        this.truckId = truckId;
    }
}
