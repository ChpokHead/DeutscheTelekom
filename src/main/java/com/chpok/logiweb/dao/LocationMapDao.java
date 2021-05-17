package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.LocationMap;

import java.util.Optional;

public interface LocationMapDao extends CrudDao<LocationMap> {
    Optional<LocationMap> findByStartingAndEndingLocationsIds(Long startingLocationId, Long endingLocationId);
}
