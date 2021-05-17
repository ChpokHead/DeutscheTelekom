package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.LocationMapDao;
import com.chpok.logiweb.model.LocationMap;
import com.chpok.logiweb.service.LocationMapService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LocationMapServiceImpl implements LocationMapService {
    private final LocationMapDao locationMapDao;

    public LocationMapServiceImpl(LocationMapDao locationMapDao) {
        this.locationMapDao = locationMapDao;
    }

    @Override
    public Short getDistanceBetweenLocationsByIds(Long startingLocationId, Long endingLocationId) {
        Optional<LocationMap> locationMap = locationMapDao.findByStartingAndEndingLocationsIds(startingLocationId, endingLocationId);

        return locationMap.map(LocationMap::getDistance).orElse(null);
    }
}
