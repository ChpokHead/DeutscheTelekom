package com.chpok.logiweb.model;

import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.util.PostgreSQLEnumType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "driver")
@DynamicInsert
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Driver extends AbstractModel{
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "personal_number")
    private String personalNumber;

    @Column(name = "month_worked_hours")
    private Short monthWorkedHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "driver_status")
    @Type(type = "pgsql_enum")
    private DriverStatus status;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck currentTruck;

    public Driver() {

    }

    private Driver(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.personalNumber = builder.personalNumber;
        this.monthWorkedHours = builder.monthWorkedHours;
        this.status = builder.status;
        this.location = builder.location;
        this.currentTruck = builder.currentTruck;
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

    public String getPersonalNumber() {
        return personalNumber;
    }

    public Short getMonthWorkedHours() {
        return monthWorkedHours;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String personalNumber;
        private Short monthWorkedHours;
        private DriverStatus status;
        private String location;
        private Truck currentTruck;

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

        public Builder withPersonalNumber(String personalNumber) {
            this.personalNumber = personalNumber;
            return this;
        }

        public Builder withMonthWorkedHours(Short monthWorkedHours) {
            this.monthWorkedHours = monthWorkedHours;
            return this;
        }

        public Builder withStatus(DriverStatus status) {
            this.status = status;
            return this;
        }

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder withCurrentTruck(Truck truck) {
            this.currentTruck = truck;
            return this;
        }

        public Driver build() {
            return new Driver(this);
        }
    }
}
