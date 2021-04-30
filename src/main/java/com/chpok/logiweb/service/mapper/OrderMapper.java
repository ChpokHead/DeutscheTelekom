package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.model.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final WaypointMapper waypointMapper;

    public OrderMapper(WaypointMapper waypointMapper) {
        this.waypointMapper = waypointMapper;
    }

    public OrderDto mapOrderToOrderDto(Order order) {
        final OrderDto orderDto = new OrderDto();

        orderDto.setId(order.getId());
        //orderDto.setCurrentTruckRegNum(order.getTruck().getRegNumber());
        orderDto.setIsCompleted(order.getCompleted());
        //orderDto.setCurrentDrivers(order.getDrivers());
        orderDto.setWaypoints(order.getWaypoints().stream().map(waypointMapper::mapWaypointToWaypointDto).collect(Collectors.toList()));

        return orderDto;
    }

    public Order mapOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto.getWaypoints() == null) {
            return Order.builder()
                    .withId(orderDto.getId())
                    .withIsCompleted(orderDto.getIsCompleted())
                    .build();
        } else {
            return Order.builder()
                    .withId(orderDto.getId())
                    .withIsCompleted(orderDto.getIsCompleted())
                    .withWaypoints(orderDto.getWaypoints().stream().map(waypointMapper::mapWaypointDtoToWaypoint).collect(Collectors.toList()))
                    .build();
        }
    }
}
