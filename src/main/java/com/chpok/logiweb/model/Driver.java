package com.chpok.logiweb.model;

import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.util.PostgreSQLEnumType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "driver")
@DynamicInsert
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Driver extends AbstractModel {
    public static final short DRIVERS_MONTH_WORKING_HOURS_LIMIT = 176;
    public static final short DRIVERS_DAY_WORKING_HOURS_LIMIT = 9;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "month_worked_hours")
    private Short monthWorkedHours;

    @Column(name = "next_month_worked_hours")
    private Short nextMonthWorkedHours;

    @Column(name = "is_month_changed")
    private Boolean isMonthChanged;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "driver_status")
    @Type(type = "pgsql_enum")
    private DriverStatus status;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "current_truck_id")
    private Truck currentTruck;

    @ManyToOne
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    public Driver() {

    }

    private Driver(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.monthWorkedHours = builder.monthWorkedHours;
        this.status = builder.status;
        this.location = builder.location;
        this.currentTruck = builder.currentTruck;
        this.currentOrder = builder.currentOrder;
        this.nextMonthWorkedHours = builder.nextMonthWorkedHours;
        this.isMonthChanged = builder.isMonthChanged;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Short getMonthWorkedHours() {
        return monthWorkedHours;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public Order getCurrentOrder() {
        return currentOrder;
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

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private Short monthWorkedHours;
        private DriverStatus status;
        private Location location;
        private Truck currentTruck;
        private Order currentOrder;
        private Short nextMonthWorkedHours;
        private Boolean isMonthChanged;

        private Builder(){

        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withMonthWorkedHours(Short monthWorkedHours) {
            this.monthWorkedHours = monthWorkedHours;
            return this;
        }

        public Builder withNextMonthWorkedHours(Short nextMonthWorkedHours) {
            this.nextMonthWorkedHours = nextMonthWorkedHours;
            return this;
        }

        public Builder withIsMonthChanged(Boolean isMonthChanged) {
            this.isMonthChanged = isMonthChanged;
            return this;
        }

        public Builder withStatus(DriverStatus status) {
            this.status = status;
            return this;
        }

        public Builder withLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder withCurrentTruck(Truck truck) {
            this.currentTruck = truck;
            return this;
        }

        public Builder withCurrentOrder(Order order) {
            this.currentOrder = order;
            return this;
        }

        public Driver build() {
            return new Driver(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return firstName.equals(driver.firstName) && lastName.equals(driver.lastName) && monthWorkedHours.equals(driver.monthWorkedHours) && status == driver.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, monthWorkedHours, status, location, currentTruck, currentOrder);
    }
}
