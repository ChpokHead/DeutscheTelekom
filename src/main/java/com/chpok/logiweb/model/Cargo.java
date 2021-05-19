package com.chpok.logiweb.model;

import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.model.enums.util.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cargo")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Cargo extends AbstractModel {
    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "cargo_status")
    @Type(type="pgsql_enum")
    private CargoStatus status;

    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private List<Order.Waypoint> waypoints;

    public Cargo() {
    }

    private Cargo(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.weight = builder.weight;
        this.status = builder.status;
        this.waypoints = builder.waypoints;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
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

    public void setStatus(CargoStatus status) {
        this.status = status;
    }

    public List<Order.Waypoint> getWaypoints() {
        return waypoints;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Integer weight;
        private CargoStatus status;
        private List<Order.Waypoint> waypoints;

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

        public Builder withWaypoints(List<Order.Waypoint> waypoints) {
            this.waypoints = waypoints;
            return this;
        }

        public Cargo build() {
            return new Cargo(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        return name.equals(cargo.name) && weight.equals(cargo.weight) && status == cargo.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, status);
    }
}
