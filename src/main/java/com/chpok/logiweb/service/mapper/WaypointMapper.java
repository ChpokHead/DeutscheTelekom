package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.WaypointType;
import org.springframework.stereotype.Component;

@Component
public class WaypointMapper {
    public WaypointDto mapWaypointToWaypointDto(Order.Waypoint waypoint) {
        final WaypointDto waypointDto = new WaypointDto();

        waypointDto.setId(waypoint.getId());

        if (waypoint.getLocation() != null) {
            waypointDto.setLocation(waypoint.getLocation());
        }

        if (waypoint.getCargo() != null) {
            waypointDto.setCargo(waypoint.getCargo());
        }

        if (waypoint.getOrder() != null) {
            waypointDto.setOrder(waypoint.getOrder());
        }

        waypointDto.setType(waypoint.getType().ordinal());

        waypointDto.setIsDone(waypoint.getIsDone());

        return waypointDto;
    }

    public Order.Waypoint mapWaypointDtoToWaypoint(WaypointDto waypointDto) {
        return new Order.Waypoint(waypointDto.getId(), waypointDto.getLocation(), waypointDto.getCargo(),
                WaypointType.fromInteger(waypointDto.getType()), waypointDto.getOrder(), waypointDto.getIsDone());
    }
}
