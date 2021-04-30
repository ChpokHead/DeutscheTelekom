package com.chpok.logiweb.model;

import com.chpok.logiweb.model.enums.WaypointType;
import com.chpok.logiweb.util.PostgreSQLEnumType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer_order")
@DynamicInsert
public class Order extends AbstractModel{
    @Column(name = "is_completed")
    private Boolean isCompleted;

    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @OneToMany(mappedBy = "currentOrder", fetch = FetchType.LAZY)
    private List<Driver> drivers;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Waypoint> waypoints;

    public Order() {
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.isCompleted = builder.isCompleted;
        this.truck = builder.truck;
        this.drivers = builder.drivers;
        this.waypoints = builder.waypoints;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public Truck getTruck() {
        return truck;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
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

    @Entity
    @Table(name = "waypoint")
    @TypeDef(
            name = "pgsql_enum",
            typeClass = PostgreSQLEnumType.class
    )
    public static class Waypoint extends AbstractModel {
        @OneToOne
        @JoinColumn(name = "location_id")
        private Location location;

        @OneToOne
        @JoinColumn(name = "cargo_id")
        private Cargo cargo;

        @Enumerated(EnumType.STRING)
        @Column(name = "type", columnDefinition = "waypoint_type")
        @Type(type="pgsql_enum")
        private WaypointType type;

        @ManyToOne
        @JoinColumn(name = "order_id")
        private Order order;

        public Waypoint() {

        }

        public Waypoint(Location location, Cargo cargo, WaypointType type, Order order) {
            this.location = location;
            this.cargo = cargo;
            this.type = type;
            this.order = order;
        }

        public Location getLocation() {
            return location;
        }

        public Cargo getCargo() {
            return cargo;
        }

        public WaypointType getType() {
            return type;
        }

        public Order getOrder() {
            return order;
        }
    }
}
