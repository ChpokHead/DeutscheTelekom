package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.LocationMapDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.LocationMap;
import org.springframework.stereotype.Component;

@Component
public class LocationMapMapper implements Mapper<LocationMap, LocationMapDto> {
    @Override
    public LocationMap mapDtoToEntity(LocationMapDto dto) {
        return new LocationMap(dto.getId(), dto.getStartingLocation(), dto.getEndingLocation(), dto.getDistance());
    }

    @Override
    public LocationMapDto mapEntityToDto(LocationMap entity) {
        return new LocationMapDto(entity.getId(), entity.getStartingLocation(), entity.getEndingLocation(), entity.getDistance());
    }

}
