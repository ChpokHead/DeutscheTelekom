package com.chpok.logiweb.model;

import com.chpok.logiweb.model.enums.TruckStatus;
import com.chpok.logiweb.model.enums.util.PostgreSQLEnumType;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "truck")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Truck extends AbstractModel{
    public static final short TRUCK_AVERAGE_SPEED_IN_KMH = 70;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "drivers_shift")
    private Short driversShift;

    @Column(name = "capacity")
    private Short capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "truck_status")
    @Type(type="pgsql_enum")
    private TruckStatus status;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @JsonIgnore
    @OneToMany(mappedBy = "currentTruck", fetch = FetchType.LAZY)
    private List<Driver> currentDrivers;

    @OneToOne
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    public Truck() {

    }

    private Truck(Builder builder) {
        this.id = builder.id;
        this.regNumber = builder.regNumber;
        this.driversShift = builder.driversShift;
        this.capacity = builder.capacity;
        this.status = builder.status;
        this.location = builder.location;
        this.currentDrivers = builder.currentDrivers;
        this.currentOrder = builder.currentOrder;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public Short getDriversShift() {
        return driversShift;
    }

    public Short getCapacity() {
        return capacity;
    }

    public TruckStatus getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public List<Driver> getCurrentDrivers() {
        return currentDrivers;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public static class Builder {
        private Long id;
        private String regNumber;
        private Short driversShift;
        private Short capacity;
        private TruckStatus status;
        private Location location;
        private List<Driver> currentDrivers;
        private Order currentOrder;

        private Builder() {}

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withRegNumber(String regNumber) {
            this.regNumber = regNumber;
            return this;
        }

        public Builder withDriversShift(Short driversShift) {
            this.driversShift = driversShift;
            return this;
        }

        public Builder withCapacity(Short capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder withStatus(TruckStatus status) {
            this.status = status;
            return this;
        }

        public Builder withLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder withCurrentDrivers(List<Driver> currentDrivers) {
            this.currentDrivers = currentDrivers;
            return this;
        }

        public Builder withCurrentOrder(Order currentOrder) {
            this.currentOrder = currentOrder;
            return this;
        }

        public Truck build() {
            return new Truck(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return Objects.equals(regNumber, truck.regNumber) && Objects.equals(driversShift, truck.driversShift) && Objects.equals(capacity, truck.capacity) && status == truck.status && Objects.equals(location, truck.location) && Objects.equals(currentOrder, truck.currentOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, driversShift, capacity, status, location, currentOrder);
    }
}
