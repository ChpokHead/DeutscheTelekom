package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.OrderDao;
import com.chpok.logiweb.dto.*;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Location;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.OrderStatus;
import com.chpok.logiweb.service.*;
import com.chpok.logiweb.mapper.impl.OrderMapper;
import com.chpok.logiweb.mapper.impl.TruckMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);
    private static final int AMOUNT_KG_IN_TONNES = 1000;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TruckService truckService;
    @Autowired
    private TruckMapper truckMapper;
    @Autowired
    private DriverService driverService;
    @Autowired
    private WaypointService waypointService;
    @Autowired
    private LocationMapService locationMapService;
    @Autowired
    private CargoService cargoService;
    @Autowired
    private OrderReportService reportService;
    /*@Autowired
    private KafkaTemplate<String, LogiwebMessage> kafkaTemplate;*/

    @Override
    public List<OrderDto> getAllOrders() {
        try {
            return orderDao.findAll().stream().map(orderMapper::mapEntityToDto).collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all orders exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        try {
            return orderDao.findByStatus(status).stream().map(orderMapper::mapEntityToDto).collect(Collectors.toList());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all orders exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void saveOrder(OrderDto order) {
        try {
            final Long savedOrderId = orderDao.save(orderMapper.mapDtoToEntity(order));

            logOnSuccess("new order was created");

            //sendMessage(new LogiwebMessage("orderSaved", savedOrderId));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("saving order exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateOrder(OrderDto order) {
        try {
            orderDao.update(orderMapper.mapDtoToEntity(order));

            logOnSuccess(String.format("order with id = %d was updated", order.getId()));

            //sendMessage(new LogiwebMessage("orderUpdated", order.getId()));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating order exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateOrderCurrentTruck(Long orderId, TruckDto newTruck) {
        try {
            final OrderDto updatingOrder = getOrderById(orderId);

            truckService.updateTruckCurrentOrder(newTruck.getId(), updatingOrder);

            updatingOrder.setCurrentTruck(truckMapper.mapDtoToEntity(newTruck));

            updateOrder(updatingOrder);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating order current truck by order id exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void deleteOrderCurrentTruckWithCurrentDriversAndDates(Long orderId) {
        try {
            deleteOrderStartAndEndDatesWithDrivers(orderId);

            deleteOrderCurrentTruck(orderId);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting order current truck with current drivers and dates by order id exception");

            throw new EntityNotFoundException();
        }
    }

    private void deleteOrderCurrentTruck(Long orderId) {
        try {
            final OrderDto updatingOrder = getOrderById(orderId);

            truckService.updateTruckWhenCurrentOrderIsDeleted(updatingOrder.getCurrentTruck().getId());

            updatingOrder.setCurrentTruck(null);

            updateOrder(updatingOrder);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting order current truck by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        try {
            final OrderDto updatingOrder = getOrderById(orderId);

            updatingOrder.setStatus(status.ordinal());

            updateOrder(updatingOrder);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating order status exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateOrderStartAndEndDates(Long orderId, LocalDate orderStartDate, LocalDate orderEndDate) {
        try {
            final OrderDto updatingOrder = getOrderById(orderId);

            updatingOrder.setStartDate(orderStartDate);
            updatingOrder.setEndDate(orderEndDate);

            updateOrder(updatingOrder);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating order start and end dates by order id exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void deleteOrderStartAndEndDatesWithDrivers(Long orderId) {
        try {
            final OrderDto updatingOrder = getOrderById(orderId);

            for (Driver driver: updatingOrder.getCurrentDrivers()) {
                driverService.updateDriverWhenOrderIsDeleted(driver.getId());
            }

            updateOrderStartAndEndDates(orderId, null, null);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting order start and end dates with drivers by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public OrderDto getOrderById(Long id) {
        try {
            return orderMapper.mapEntityToDto(orderDao.findById(id).orElseThrow(NoSuchElementException::new));
        } catch (HibernateException | NoSuchElementException | NoResultException e) {
            LOGGER.error("getting order by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public boolean checkOrderIsCompleted(Long orderId) {
        try {
            final OrderDto checkingOrder = getOrderById(orderId);

            return checkingOrder != null && checkingOrder.getWaypoints() != null && waypointService.checkAllWaypointsComplete(checkingOrder.getWaypoints());
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("checking order is completed exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public List<TruckDto> getSuitableTrucksForOrder(Long orderId) {
        try {
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
                    if (waypoint.getCargo().getWeight() > truck.getCapacity() * AMOUNT_KG_IN_TONNES || !loadingWaypoints.get(0).getLocation().equals(truck.getLocation())) {
                        isTruckSuitable = false;
                        break;
                    }
                }

                if (isTruckSuitable) {
                    suitableTrucks.add(truck);
                }
            }

            return suitableTrucks;
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting suitable truck for order by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public Short getOrderDistance(Long orderId) {
        try {
            final List<WaypointsPair> waypointsPairs = waypointService.getAllWaypointsPairByOrderId(orderId);

            short orderDistance = 0;

            for (WaypointsPair pair : waypointsPairs) {
                final List<WaypointDto> orderWaypoints = pair.getPair();

                orderDistance +=
                        locationMapService.getDistanceBetweenLocationsByIds(orderWaypoints.get(0).getLocation().getId(),
                                orderWaypoints.get(1).getLocation().getId());
            }

            return orderDistance;
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting order distance by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public Short getOrderTravelHours(Long orderId) {
        try {
            return (short)(Math.round((double)getOrderDistance(orderId) / Truck.TRUCK_AVERAGE_SPEED_IN_KMH));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting order travel hours by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<DriverDto> getSuitableDriversForOrder(Long orderId) {
        try {
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
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting suitable drivers for order by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateOrderCurrentDriver(Long orderId, DriverDto newDriver) {
        try {
            final OrderDto updatingOrder = getOrderById(orderId);

            newDriver.setCurrentOrder(orderMapper.mapDtoToEntity(updatingOrder));
            newDriver.setCurrentTruck(truckMapper.mapEntityToDto(updatingOrder.getCurrentTruck()));

            driverService.updateDriver(newDriver);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating order current drivers exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void closeOrder(Long orderId) {
        try {
            final OrderDto completingOrder = getOrderById(orderId);

            reportService.createOrderReport(completingOrder, getOrderDistance(orderId));

            final List<Order.Waypoint> orderWaypoints = completingOrder.getWaypoints();
            final List<Driver> orderDrivers = completingOrder.getCurrentDrivers();

            if (completingOrder.getCurrentTruck() != null) {
                truckService.updateTruckLocation(completingOrder.getCurrentTruck().getId(), orderWaypoints.get(orderWaypoints.size() - 1).getLocation());
                truckService.updateTruckCurrentOrder(completingOrder.getCurrentTruck().getId(), null);
            }

            if (!orderDrivers.isEmpty()) {
                for (Driver driver : orderDrivers) {
                    final short updatedDriverMonthWorkedHours = calculateMonthWorkedHoursForOrderCurrentDriver(driver.getMonthWorkedHours(), completingOrder);
                    final Location updatedDriverLocation = orderWaypoints.get(orderWaypoints.size() - 1).getLocation();

                    driverService.updateDriverWhenOrderIsCompleted(driver.getId(), updatedDriverMonthWorkedHours, updatedDriverLocation);
                }
            }

            if (!orderWaypoints.isEmpty()) {
                for (int i = 0; i < orderWaypoints.size(); i+=2) {
                    cargoService.deleteCargo(orderWaypoints.get(i).getCargo().getId());
                }
            }

            orderDao.deleteById(orderId);

            logOnSuccess(String.format("order with id = %d has been completed", orderId));

            //sendMessage(new LogiwebMessage("orderDeleted", orderId));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("completing order by order id exception");

            throw new EntityNotFoundException();
        }
    }

    private short calculateMonthWorkedHoursForOrderCurrentDriver(short driverMonthWorkedHours, OrderDto driverCurrentOrder) {
        final LocalDate travelStartDate = driverCurrentOrder.getStartDate();
        final short orderMinTravelHours = getOrderTravelHours(driverCurrentOrder.getId());
        final short orderMinTravelDays = (short)(orderMinTravelHours / (Driver.DRIVERS_DAY_WORKING_HOURS_LIMIT * driverCurrentOrder.getCurrentDrivers().size()) + 1);
        final LocalDate travelEndDate = travelStartDate.plusDays(orderMinTravelDays);

        if (!travelStartDate.getMonth().equals(travelEndDate.getMonth())) {
            final short daysInNewMonth = (short) (orderMinTravelDays - (travelStartDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth() - travelStartDate.getDayOfMonth() + 1));

            return (short) (daysInNewMonth * Driver.DRIVERS_DAY_WORKING_HOURS_LIMIT);
        } else {
            return (short) (driverMonthWorkedHours + orderMinTravelHours);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        try {
            final OrderDto deletingOrder = getOrderById(id);
            final List<Order.Waypoint> ordersWaypoints = deletingOrder.getWaypoints();
            final List<Driver> ordersDrivers = deletingOrder.getCurrentDrivers();

            if (deletingOrder.getCurrentTruck() != null) {
                truckService.updateTruckCurrentOrder(deletingOrder.getCurrentTruck().getId(), null);
            }

            if (!ordersDrivers.isEmpty()) {
                for (Driver driver : ordersDrivers) {
                    driverService.updateDriverCurrentTruck(driver.getId(), null);
                    driverService.updateDriverCurrentOrder(driver.getId(), null);
                }
            }

            if (!ordersWaypoints.isEmpty()) {
                for (Order.Waypoint waypoint : deletingOrder.getWaypoints()) {
                    waypointService.deleteWaypoint(waypoint.getId());
                }
            }

            orderDao.deleteById(id);

            logOnSuccess(String.format("order with id = %d has been deleted", id));

            //sendMessage(new LogiwebMessage("orderDeleted", id));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting order by order id exception");

            throw new EntityNotFoundException();
        }
    }

    private boolean isDriverOverworking(DriverDto driver, Short orderTravelHours) {
        return driver.getMonthWorkedHours() + orderTravelHours > Driver.DRIVERS_MONTH_WORKING_HOURS_LIMIT;
    }

    private boolean isDriverAndOrderCurrentTruckHasSameLocation(DriverDto driver, OrderDto order) {
        return driver.getLocation().getId().equals(order.getCurrentTruck().getLocation().getId());
    }

    private void logOnSuccess(String logInfo) {
        LOGGER.info(logInfo);
    }

    /*private void sendMessage(LogiwebMessage message) {
        kafkaTemplate.send("logiweb-order", message);
    }*/

}
