package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.WaypointType;
import org.springframework.stereotype.Component;

@Component
public class WaypointMapper implements Mapper<Order.Waypoint, WaypointDto> {
    public WaypointDto mapEntityToDto(Order.Waypoint waypoint) {
        final WaypointDto waypointDto = new WaypointDto();

        waypointDto.setId(waypoint.getId());

        waypointDto.setLocation(waypoint.getLocation());

        waypointDto.setCargo(waypoint.getCargo());

        waypointDto.setOrder(waypoint.getOrder());

        waypointDto.setType(waypoint.getType().ordinal());

        waypointDto.setIsDone(waypoint.getIsDone());

        return waypointDto;
    }

    public Order.Waypoint mapDtoToEntity(WaypointDto waypointDto) {
        return Order.Waypoint.builder()
                .withId(waypointDto.getId())
                .withLocation(waypointDto.getLocation())
                .withCargo(waypointDto.getCargo())
                .withType(WaypointType.fromInteger(waypointDto.getType()))
                .withOrder(waypointDto.getOrder())
                .withIsDone(waypointDto.getIsDone())
                .build();
    }

}
