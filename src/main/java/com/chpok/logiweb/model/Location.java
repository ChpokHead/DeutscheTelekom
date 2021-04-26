package com.chpok.logiweb.model;

import javax.persistence.*;

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

    public String getName() {
        return name;
    }
}
