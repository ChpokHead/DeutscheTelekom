package com.chpok.logiweb.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "location_map")
public class LocationMap extends AbstractModel {
    @OneToOne
    @JoinColumn(name = "starting_location")
    private Location startingLocation;

    @OneToOne
    @JoinColumn(name = "ending_location")
    private Location endingLocation;

    @Column(name = "distance")
    private Short distance;

    public LocationMap(Long id, Location startingLocation, Location endingLocation, Short distance) {
        this.id = id;
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
        this.distance = distance;
    }

    public LocationMap(Location startingLocation, Location endingLocation, Short distance) {
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
        this.distance = distance;
    }

    public LocationMap() {
    }

    public Location getStartingLocation() {
        return startingLocation;
    }

    public Location getEndingLocation() {
        return endingLocation;
    }

    public Short getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationMap that = (LocationMap) o;
        return startingLocation.equals(that.startingLocation) && endingLocation.equals(that.endingLocation) && distance.equals(that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingLocation, endingLocation, distance);
    }
}
