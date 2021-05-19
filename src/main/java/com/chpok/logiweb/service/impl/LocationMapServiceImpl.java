package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.LocationMapDao;
import com.chpok.logiweb.service.LocationMapService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@Component
public class LocationMapServiceImpl implements LocationMapService {
    private static final Logger LOGGER = LogManager.getLogger(LocationMapServiceImpl.class);

    private final LocationMapDao locationMapDao;

    public LocationMapServiceImpl(LocationMapDao locationMapDao) {
        this.locationMapDao = locationMapDao;
    }

    @Override
    public Short getDistanceBetweenLocationsByIds(Long startingLocationId, Long endingLocationId) {
        try {
            return locationMapDao.findByStartingAndEndingLocationsIds(startingLocationId, endingLocationId)
                    .orElseThrow(NoSuchElementException::new).getDistance();
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting distance between locations by ids exception");

            throw new EntityNotFoundException();
        }
    }
}
