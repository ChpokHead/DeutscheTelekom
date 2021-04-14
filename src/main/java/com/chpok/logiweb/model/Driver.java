package com.chpok.logiweb.model;

public class Driver {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String personalNumber;
    private final Short monthWorkedHours;
    private final DriverStatus status;
    private final String location;
    private final Truck truck;

    private Driver(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.personalNumber = builder.personalNumber;
        this.monthWorkedHours = builder.monthWorkedHours;
        this.status = builder.status;
        this.location = builder.location;
        this.truck = builder.truck;
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

    public Truck getTruck() {
        return truck;
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String personalNumber;
        private Short monthWorkedHours;
        private DriverStatus status;
        private String location;
        private Truck truck;

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

        public Builder withTruck(Truck truck) {
            this.truck = truck;
            return this;
        }

        public Driver build() {
            return new Driver(this);
        }
    }
}
