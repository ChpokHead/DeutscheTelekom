package com.chpok.logiweb.model;

import java.util.List;

public class Order {
    private final Long id;
    private final Boolean isCompleted;
    private final Truck truck;
    private final List<Driver> drivers;
    private final List<Waypoint> waypoints;

    private Order(Builder builder) {
        this.id = builder.id;
        this.isCompleted = builder.isCompleted;
        this.truck = builder.truck;
        this.drivers = builder.drivers;
        this.waypoints = builder.waypoints;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Boolean isCompleted;
        private Truck truck;
        private List<Driver> drivers;
        private List<Waypoint> waypoints;

        private Builder() {}

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withIsCompleted(Boolean isCompleted) {
            this.isCompleted = isCompleted;
            return this;
        }

        public Builder withTruck(Truck truck) {
            this.truck = truck;
            return this;
        }

        public Builder withDrivers(List<Driver> drivers) {
            this.drivers = drivers;
            return this;
        }

        public Builder withWaypoints(List<Waypoint> waypoints) {
            this.waypoints = waypoints;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
    public class Waypoint {
        private final String location;
        private final Cargo cargo;
        private final WaypointType type;

        public Waypoint(String location, Cargo cargo, WaypointType type) {
            this.location = location;
            this.cargo = cargo;
            this.type = type;
        }

        public String getLocation() {
            return location;
        }

        public Cargo getCargo() {
            return cargo;
        }

        public WaypointType getType() {
            return type;
        }
    }
}
