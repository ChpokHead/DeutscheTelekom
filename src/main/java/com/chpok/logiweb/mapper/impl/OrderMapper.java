package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.OrderStatus;

import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements Mapper<Order, OrderDto> {
    public OrderDto mapEntityToDto(Order order) {
        if (order == null) {
            return null;
        }

        final OrderDto orderDto = new OrderDto();

        orderDto.setId(order.getId());
        orderDto.setStatus(order.getStatus().ordinal());
        orderDto.setCurrentDrivers(order.getDrivers());
        orderDto.setWaypoints(order.getWaypoints());
        orderDto.setCurrentTruck(order.getCurrentTruck());
        orderDto.setCreationDate(order.getCreationDate());
        orderDto.setStartDate(order.getStartDate());
        orderDto.setEndDate(order.getEndDate());

        return orderDto;
    }

    public Order mapDtoToEntity(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        return Order.builder()
                .withId(orderDto.getId())
                .withStatus(OrderStatus.fromInteger(orderDto.getStatus()))
                .withWaypoints(orderDto.getWaypoints())
                .withDrivers(orderDto.getCurrentDrivers())
                .withTruck(orderDto.getCurrentTruck())
                .withCreationDate(orderDto.getCreationDate())
                .withStartDate(orderDto.getStartDate())
                .withEndDate(orderDto.getEndDate())
                .build();
    }

}
