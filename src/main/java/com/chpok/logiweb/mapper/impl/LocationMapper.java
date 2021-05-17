package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.LocationDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper implements Mapper<Location, LocationDto> {
    public Location mapDtoToEntity(LocationDto locationDto) {
        return new Location(locationDto.getId(), locationDto.getName());
    }

    public LocationDto mapEntityToDto(Location location) {
        final LocationDto locationDto = new LocationDto();

        locationDto.setId(location.getId());
        locationDto.setName(location.getName());

        return locationDto;
    }
}
