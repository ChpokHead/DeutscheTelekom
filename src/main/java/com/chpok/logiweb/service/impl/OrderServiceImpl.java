package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.OrderDao;
import com.chpok.logiweb.dto.*;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.service.*;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {
    private static final short TRUCK_AVERAGE_SPEED_IN_KMH = 70;
    private static final short DRIVERS_MONTH_WORKING_LIMIT = 176;

    private final WaypointService waypointService;
    private final TruckService truckService;
    private final DriverService driverService;
    private final CargoService cargoService;
    private final LocationMapService locationMapService;
    private final OrderDao orderDao;
    private final OrderMapper orderMapper;
    private final TruckMapper truckMapper;
    private final DriverMapper driverMapper;

    public OrderServiceImpl(WaypointService waypointService, DriverService driverService, CargoService cargoService, LocationMapService locationMapService, OrderDao orderDao, OrderMapper orderMapper, TruckService truckService, TruckMapper truckMapper, DriverMapper driverMapper) {
        this.waypointService = waypointService;
        this.driverService = driverService;
        this.cargoService = cargoService;
        this.locationMapService = locationMapService;
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
        this.truckService = truckService;
        this.truckMapper = truckMapper;
        this.driverMapper = driverMapper;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderDao.findAll().stream().map(orderMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void saveOrder(OrderDto order) {
        orderDao.save(orderMapper.mapDtoToEntity(order));
    }

    @Override
    public void updateOrder(OrderDto order) {
        orderDao.update(orderMapper.mapDtoToEntity(order));
    }

    @Override
    public void updateOrderCurrentTruck(Long orderId, TruckDto newTruck) {
        final OrderDto updatingOrder = getOrderById(orderId);

        truckService.updateTruckCurrentOrder(newTruck.getId(), null);

        updatingOrder.setCurrentTruck(truckMapper.mapDtoToEntity(newTruck));

        updateOrder(updatingOrder);
    }

    @Override
    public void deleteOrderCurrentTruckWithCurrentDriversAndDates(Long orderId) {
        final OrderDto updatingOrder = getOrderById(orderId);

        truckService.updateTruckCurrentOrder(updatingOrder.getCurrentTruck().getId(), null);

        deleteOrderStartAndEndDatesWithDrivers(orderId);

        deleteOrderCurrentTruck(orderId);
    }

    private void deleteOrderCurrentTruck(Long orderId) {
        final OrderDto updatingOrder = getOrderById(orderId);

        truckService.updateTruckCurrentOrder(updatingOrder.getCurrentTruck().getId(), null);

        updatingOrder.setCurrentTruck(null);

        updateOrder(updatingOrder);
    }

    @Override
    public void updateOrderStatus(Order order) {
        final OrderDto updatingOrder = getOrderById(order.getId());

        updatingOrder.setIsCompleted(order.getIsCompleted());

        updateOrder(updatingOrder);
    }

    @Override
    public void updateOrderStartAndEndDates(Long orderId, LocalDate orderStartDate, LocalDate orderEndDate) {
        final OrderDto updatingOrder = getOrderById(orderId);

        updatingOrder.setStartDate(orderStartDate);
        updatingOrder.setEndDate(orderEndDate);

        updateOrder(updatingOrder);
    }

    @Override
    public void deleteOrderStartAndEndDatesWithDrivers(Long orderId) {
        final OrderDto updatingOrder = getOrderById(orderId);

        for (Driver driver: updatingOrder.getCurrentDrivers()) {
            driverService.updateDriverCurrentOrder(driver.getId(), null);
        }

        updateOrderStartAndEndDates(orderId, null, null);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderMapper.mapEntityToDto(orderDao.findById(id).get());
    }

    @Override
    public boolean checkOrderIsCompleted(Order order) {
        return order != null && order.getWaypoints() != null && waypointService.checkAllWaypointsComplete(order.getWaypoints());
    }

    @Override
    public List<TruckDto> getSuitableTrucksForOrder(Long orderId) {
        if (getOrderById(orderId).getWaypoints().isEmpty()) {
            return Collections.emptyList();
        }

        final List<TruckDto> suitableTrucks = new ArrayList<>();
        final List<TruckDto> trucksWithOKStatusAndWithoutCurrentOrder = truckService.getTrucksWithOKStatusAndWithoutCurrentOrder();
        final List<WaypointDto> loadingWaypoints = waypointService.getAllLoadingWaypointsByOrderId(orderId);

        boolean isTruckSuitable;

        for (TruckDto truck : trucksWithOKStatusAndWithoutCurrentOrder) {
            isTruckSuitable = true;

            for (WaypointDto waypoint : loadingWaypoints) {
                if (waypoint.getCargo().getWeight() > truck.getCapacity() * 1000 || !loadingWaypoints.get(0).getLocation().equals(truck.getLocation())) {
                    isTruckSuitable = false;
                    break;
                }
            }

            if (isTruckSuitable) {
                suitableTrucks.add(truck);
            }
        }

        return suitableTrucks;
    }

    @Override
    public Short getOrderDistance(Long orderId) {
        final List<WaypointsPair> waypointsPairs = waypointService.getAllWaypointsPairByOrderId(orderId);

        short orderDistance = 0;

        for (WaypointsPair pair : waypointsPairs) {
            final List<WaypointDto> orderWaypoints = pair.getPair();

            orderDistance +=
                    locationMapService.getDistanceBetweenLocationsByIds(orderWaypoints.get(0).getLocation().getId(), orderWaypoints.get(1).getLocation().getId());
        }

        return orderDistance;
    }

    @Override
    public Short getOrderTravelHours(Long orderId) {
        return (short)(Math.round((double)getOrderDistance(orderId) / TRUCK_AVERAGE_SPEED_IN_KMH));
    }

    @Override
    public List<DriverDto> getSuitableDriversForOrder(Long orderId) {
        final OrderDto order = getOrderById(orderId);

        if (order.getWaypoints().isEmpty() || order.getCurrentTruck() == null) {
            return Collections.emptyList();
        }

        final List<DriverDto> driversWithoutCurrentOrder = driverService.getAllDriversWithoutCurrentOrder();
        final Short orderTravelHours = getOrderTravelHours(orderId);
        final List<DriverDto> suitableDrivers = new ArrayList<>();

        for (DriverDto driver : driversWithoutCurrentOrder) {
            if (isDriverAndOrderCurrentTruckHasSameLocation(driver, order) && !isDriverOverworking(driver, orderTravelHours)) {
                suitableDrivers.add(driver);
            }
        }

        return suitableDrivers;
    }

    @Override
    public void updateOrderCurrentDriver(Long orderId, DriverDto driver) {
        final OrderDto updatingOrder = getOrderById(orderId);

        driver.setCurrentOrder(orderMapper.mapDtoToEntity(updatingOrder));
        driver.setCurrentTruck(updatingOrder.getCurrentTruck());

        driverService.updateDriver(driver);
    }

    @Override
    public void completeOrder(Long orderId) {
        final OrderDto completingOrder = getOrderById(orderId);
        final List<Order.Waypoint> orderWaypoints = completingOrder.getWaypoints();
        final List<Driver> orderDrivers = completingOrder.getCurrentDrivers();

        deleteCurrentOrderFromCurrentTruck(completingOrder);

        if (!orderDrivers.isEmpty()) {
            for (Driver driver : orderDrivers) {
                driverService.updateDriversMonthWorkedHours(driver.getId(), (short)(driver.getMonthWorkedHours() + getOrderTravelHours(completingOrder.getId())));
                driverService.deleteDriverCurrentOrderWithCurrentTruck(driver.getId());
            }
        }

        if (!orderWaypoints.isEmpty()) {
            for (int i = 0; i < orderWaypoints.size(); i+=2) {
                cargoService.deleteCargo(orderWaypoints.get(i).getCargo().getId());
            }
        }

        orderDao.deleteById(orderId);
    }

    @Override
    public void deleteOrder(Long id) {
        final OrderDto deletingOrder = getOrderById(id);
        final List<Order.Waypoint> ordersWaypoints = deletingOrder.getWaypoints();
        final List<Driver> ordersDrivers = deletingOrder.getCurrentDrivers();

        deleteCurrentOrderFromCurrentTruck(deletingOrder);

        if (!ordersDrivers.isEmpty()) {
            for (Driver driver : ordersDrivers) {
                driverService.deleteDriverCurrentOrderWithCurrentTruck(driver.getId());
            }
        }

        if (!ordersWaypoints.isEmpty()) {
            for (Order.Waypoint waypoint : deletingOrder.getWaypoints()) {
                waypointService.deleteWaypoint(waypoint.getId());
            }
        }

        orderDao.deleteById(id);
    }

    private void deleteCurrentOrderFromCurrentTruck(OrderDto deletingOrder) {
        if (deletingOrder.getCurrentTruck() != null) {
            truckService.updateTruckCurrentOrder(deletingOrder.getCurrentTruck().getId(), null);
        }
    }

    private boolean isDriverOverworking(DriverDto driver, Short orderTravelHours) {
        return driver.getMonthWorkedHours() + orderTravelHours > DRIVERS_MONTH_WORKING_LIMIT;
    }

    private boolean isDriverAndOrderCurrentTruckHasSameLocation(DriverDto driver, OrderDto order) {
        return driver.getLocation().getId().equals(order.getCurrentTruck().getLocation().getId());
    }

}
