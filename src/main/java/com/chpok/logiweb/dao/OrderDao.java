package com.chpok.logiweb.dao;

import java.util.List;

import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.OrderStatus;

public interface OrderDao extends CrudDao<Order>{
    List<Order> findByStatus(OrderStatus status);
}
