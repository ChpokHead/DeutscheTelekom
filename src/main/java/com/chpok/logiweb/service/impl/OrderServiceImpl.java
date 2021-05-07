package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.OrderDao;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.service.OrderService;
import com.chpok.logiweb.service.WaypointService;
import com.chpok.logiweb.service.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {
    private final WaypointService waypointService;
    private final OrderDao orderDao;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(WaypointService waypointService, OrderDao orderDao, OrderMapper orderMapper) {
        this.waypointService = waypointService;
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderDao.findAll().stream().map(orderMapper::mapOrderToOrderDto).collect(Collectors.toList());
    }

    @Override
    public void saveOrder(OrderDto order) {
        orderDao.save(orderMapper.mapOrderDtoToOrder(order));
    }

    @Override
    public void deleteOrder(Long id) {
        orderDao.deleteById(id);
    }

    @Override
    public void updateOrder(OrderDto order) {
        orderDao.update(orderMapper.mapOrderDtoToOrder(order));
    }

    @Override
    public void updateOrderStatus(Order order) {
        final OrderDto updatingOrder = getOrderById(order.getId());

        updatingOrder.setIsCompleted(order.getIsCompleted());

        updateOrder(updatingOrder);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderMapper.mapOrderToOrderDto(orderDao.findById(id).get());
    }

    @Override
    public boolean checkOrderCompleted(Order order) {
        return order != null && order.getWaypoints() != null && waypointService.checkAllWaypointsComplete(order.getWaypoints());
    }

}
