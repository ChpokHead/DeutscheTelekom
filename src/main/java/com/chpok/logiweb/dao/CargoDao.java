package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.Cargo;

import java.util.List;

public interface CargoDao extends CrudDao<Cargo> {
    List<Cargo> findByName(String name);
}
