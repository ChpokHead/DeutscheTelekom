package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.dao.LocationDao;
import com.chpok.logiweb.dao.OrderDao;
import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.WaypointType;
import org.springframework.stereotype.Component;

@Component
public class WaypointMapper {
    private final LocationDao locationDao;
    private final OrderDao orderDao;
    private final CargoDao cargoDao;

    public WaypointMapper(LocationDao locationDao, OrderDao orderDao, CargoDao cargoDao) {
        this.cargoDao = cargoDao;
        this.locationDao = locationDao;
        this.orderDao = orderDao;
    }

    public WaypointDto mapWaypointToWaypointDto(Order.Waypoint waypoint) {
        final WaypointDto waypointDto = new WaypointDto();

        waypointDto.setId(waypoint.getId());

        if (waypoint.getLocation() != null) {
            waypointDto.setLocation(waypoint.getLocation().getName());
        }

        if (waypoint.getCargo() != null) {
            waypointDto.setCargoId(waypoint.getCargo().getId());
            waypointDto.setCargoName(waypoint.getCargo().getName());
        }

        if (waypoint.getOrder() != null) {
            waypointDto.setOrderId(waypoint.getOrder().getId());
        }

        waypointDto.setType(waypoint.getType().ordinal());

        return waypointDto;
    }

    public Order.Waypoint mapWaypointDtoToWaypoint(WaypointDto waypointDto) {
        final Location location = locationDao.findByName(waypointDto.getLocation()).orElse(null);
        final Cargo cargo = cargoDao.findById(waypointDto.getCargoId()).orElse(null);
        final Order order = orderDao.findById(waypointDto.getOrderId()).orElse(null);

        return new Order.Waypoint(location, cargo, WaypointType.fromInteger(waypointDto.getType()), order);
    }
}
