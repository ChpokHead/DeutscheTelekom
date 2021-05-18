package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    void saveOrder(OrderDto order);
    void deleteOrder(Long id);
    void updateOrder(OrderDto order);
    void updateOrderCurrentTruck(Long orderId, TruckDto newTruck);
    void deleteOrderCurrentTruckWithCurrentDriversAndDates(Long orderId);
    void updateOrderStatus(Order order);
    void updateOrderStartAndEndDates(Long orderId, LocalDate orderStartDate, LocalDate orderEndDate);
    void deleteOrderStartAndEndDatesWithDrivers(Long orderId);
    OrderDto getOrderById(Long id);
    boolean checkOrderIsCompleted(Order order);
    List<TruckDto> getSuitableTrucksForOrder(Long orderId);
    Short getOrderDistance(Long orderId);
    Short getOrderTravelHours(Long orderId);
    List<DriverDto> getSuitableDriversForOrder(Long orderId);
    void updateOrderCurrentDriver(Long orderId, DriverDto newDriver);
    void completeOrder(Long orderId);
}
