package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.OrderDao;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.service.OrderService;
import com.chpok.logiweb.service.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderDao orderDao, OrderMapper orderMapper) {
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
}
