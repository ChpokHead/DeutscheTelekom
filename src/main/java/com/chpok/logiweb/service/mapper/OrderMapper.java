package com.chpok.logiweb.service.mapper;

import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.model.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto mapOrderToOrderDto(Order order) {
        final OrderDto orderDto = new OrderDto();

        orderDto.setId(order.getId());
        orderDto.setIsCompleted(order.getIsCompleted());
        orderDto.setCurrentDrivers(order.getDrivers());
        orderDto.setWaypoints(order.getWaypoints());
        orderDto.setCurrentTruck(order.getTruck());

        return orderDto;
    }

    public Order mapOrderDtoToOrder(OrderDto orderDto) {
        return Order.builder()
                .withId(orderDto.getId())
                .withIsCompleted(orderDto.getIsCompleted())
                .withWaypoints(orderDto.getWaypoints())
                .withDrivers(orderDto.getCurrentDrivers())
                .withTruck(orderDto.getCurrentTruck())
                .build();
    }

}
