package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dto.LocationDto;
import com.chpok.logiweb.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public Location mapLocationDtoToLocation(LocationDto locationDto) {
        return new Location(locationDto.getId(), locationDto.getName());
    }

    public LocationDto mapLocationToLocationDto(Location location) {
        final LocationDto locationDto = new LocationDto();

        locationDto.setId(location.getId());
        locationDto.setName(location.getName());

        return locationDto;
    }
}
