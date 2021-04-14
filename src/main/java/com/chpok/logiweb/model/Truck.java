package com.chpok.logiweb.model;

public class Truck {
    private final Long id;
    private final String regNumber;
    private final Short driversShift;
    private final Short capacity;
    private final TruckStatus status;
    private final String location;

    private Truck(Builder builder) {
        this.id = builder.id;
        this.regNumber = builder.regNumber;
        this.driversShift = builder.driversShift;
        this.capacity = builder.capacity;
        this.status = builder.status;
        this.location = builder.location;
    }

    public static Builder builder() {
        return new Builder();
    }

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

    public String getLocation() {
        return location;
    }

    public static class Builder {
        private Long id;
        private String regNumber;
        private Short driversShift;
        private Short capacity;
        private TruckStatus status;
        private String location;

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

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Truck build() {
            return new Truck(this);
        }
    }
}
