package com.chpok.logiweb.model;

import com.chpok.logiweb.model.enums.CargoStatus;

public class Cargo {
    private Long id;
    private String name;
    private Integer weight;
    private CargoStatus status;

    private Cargo(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.weight = builder.weight;
        this.status = builder.status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    public CargoStatus getStatus() {
        return status;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Integer weight;
        private CargoStatus status;

        private Builder() {}

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withWeight(Integer weight) {
            this.weight = weight;
            return this;
        }

        public Builder withStatus(CargoStatus status) {
            this.status = status;
            return this;
        }

        public Cargo build() {
            return new Cargo(this);
        }
    }
}
