package com.chpok.logiweb.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "location")
public class Location extends AbstractModel{

    @Column(name = "name")
    private String name;

    public Location() {

    }

    public Location(String name) {
        this.name = name;
    }

    public Location(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
