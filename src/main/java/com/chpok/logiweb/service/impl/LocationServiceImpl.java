package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    LocationDao locationDao;

    @Override
    public List<String> getAllLocations() {
        return locationDao.findAll().stream().map(Location::getName).collect(Collectors.toList());
    }
}
