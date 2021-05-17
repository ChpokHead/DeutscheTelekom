package com.chpok.logiweb.dto;

import com.chpok.logiweb.model.Location;

public class LocationMapDto {
    private Long id;
    private Location startingLocation;
    private Location endingLocation;
    private Short distance;

    public LocationMapDto(Long id, Location startingLocation, Location endingLocation, Short distance) {
        this.id = id;
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
        this.distance = distance;
    }

    public LocationMapDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(Location startingLocation) {
        this.startingLocation = startingLocation;
    }

    public Location getEndingLocation() {
        return endingLocation;
    }

    public void setEndingLocation(Location endingLocation) {
        this.endingLocation = endingLocation;
    }

    public Short getDistance() {
        return distance;
    }

    public void setDistance(Short distance) {
        this.distance = distance;
    }
}
