package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.model.Order;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    void saveOrder(OrderDto order);
    void deleteOrder(Long id);
    void updateOrder(OrderDto order);
    void updateOrderStatus(Order order);
    OrderDto getOrderById(Long id);
    boolean checkOrderCompleted(Order order);
}
