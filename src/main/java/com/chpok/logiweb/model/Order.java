package com.chpok.logiweb.model;

import com.chpok.logiweb.model.enums.WaypointType;
import com.chpok.logiweb.model.enums.util.PostgreSQLEnumType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customer_order")
@DynamicInsert
public class Order extends AbstractModel{
    @Column(name = "is_completed")
    private Boolean isCompleted;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "current_truck_id")
    private Truck currentTruck;

    @JsonIgnore
    @OneToMany(mappedBy = "currentOrder", fetch = FetchType.LAZY)
    private List<Driver> drivers;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<Waypoint> waypoints;


    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Order() {
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.isCompleted = builder.isCompleted;
        this.currentTruck = builder.currentTruck;
        this.drivers = builder.drivers;
        this.waypoints = builder.waypoints;
        this.creationDate = builder.creationDate;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public static class Builder {
        private Long id;
        private Boolean isCompleted;
        private Truck currentTruck;
        private List<Driver> drivers;
        private List<Waypoint> waypoints;
        private LocalDate creationDate;
        private LocalDate startDate;
        private LocalDate endDate;

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
            this.currentTruck = truck;
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

        public Builder withCreationDate(LocalDate creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(isCompleted, order.isCompleted) && Objects.equals(currentTruck, order.currentTruck) && creationDate.equals(order.creationDate) && Objects.equals(startDate, order.startDate) && Objects.equals(endDate, order.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCompleted, currentTruck, drivers, waypoints, creationDate, startDate, endDate);
    }

    @Entity
    @Table(name = "waypoint")
    @TypeDef(
            name = "pgsql_enum",
            typeClass = PostgreSQLEnumType.class
    )
    @DynamicInsert
    public static class Waypoint extends AbstractModel {
        @OneToOne
        @JoinColumn(name = "location_id")
        private Location location;

        @ManyToOne
        @JoinColumn(name = "cargo_id")
        private Cargo cargo;

        @Enumerated(EnumType.STRING)
        @Column(name = "type", columnDefinition = "waypoint_type")
        @Type(type="pgsql_enum")
        private WaypointType type;

        @ManyToOne
        @JoinColumn(name = "order_id")
        private Order order;

        @Column(name = "is_done")
        private Boolean isDone = false;

        public Waypoint() {

        }

        private Waypoint(Builder builder) {
            this.id = builder.id;
            this.location = builder.location;
            this.cargo = builder.cargo;
            this.type = builder.type;
            this.order = builder.order;
            this.isDone = builder.isDone;
        }

        public static Waypoint.Builder builder() {
            return new Waypoint.Builder();
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

        public Boolean getIsDone() {
            return isDone;
        }

        public void setIsDone(Boolean isDone) {
            this.isDone = isDone;
        }

        public static class Builder {
            private Long id;
            private Location location;
            private Cargo cargo;
            private WaypointType type;
            private Order order;
            private Boolean isDone;

            private Builder() {}

            public Waypoint.Builder withId(Long id) {
                this.id = id;
                return this;
            }

            public Waypoint.Builder withLocation(Location location) {
                this.location = location;
                return this;
            }

            public Waypoint.Builder withCargo(Cargo cargo) {
                this.cargo = cargo;
                return this;
            }

            public Waypoint.Builder withType(WaypointType type) {
                this.type = type;
                return this;
            }

            public Waypoint.Builder withOrder(Order order) {
                this.order = order;
                return this;
            }

            public Waypoint.Builder withIsDone(Boolean isDone) {
                this.isDone = isDone;
                return this;
            }

            public Waypoint build() {
                return new Waypoint(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Waypoint waypoint = (Waypoint) o;
            return Objects.equals(location, waypoint.location) && Objects.equals(cargo, waypoint.cargo) && type == waypoint.type && Objects.equals(order, waypoint.order) && isDone.equals(waypoint.isDone);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location, cargo, type, order, isDone);
        }
    }

}
