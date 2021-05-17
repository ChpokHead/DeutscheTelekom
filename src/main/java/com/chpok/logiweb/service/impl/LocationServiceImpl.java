package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dto.LocationDto;
import com.chpok.logiweb.service.LocationService;
import com.chpok.logiweb.mapper.impl.LocationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {
    private final LocationMapper locationMapper;
    private final LocationDao locationDao;

    public LocationServiceImpl(LocationDao locationDao, LocationMapper locationMapper) {
        this.locationDao = locationDao;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<LocationDto> getAllLocations() {
        return locationDao.findAll().stream().map(locationMapper::mapEntityToDto).collect(Collectors.toList());
    }
}
