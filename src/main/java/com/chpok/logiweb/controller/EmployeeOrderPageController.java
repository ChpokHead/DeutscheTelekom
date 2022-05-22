package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.WaypointsPair;
import com.chpok.logiweb.model.enums.OrderStatus;
import com.chpok.logiweb.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/employeeOrder")
public class EmployeeOrderPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeOrder";
    private static final String REDIRECT_TO_ORDER_EDIT_PAGE = "redirect:/employeeOrder/edit/%d";

    private final OrderService orderService;
    private final DriverService driverService;
    private final LocationService locationService;
    private final CargoService cargoService;
    private final WaypointService waypointService;
    private final TruckService truckService;

    public EmployeeOrderPageController(OrderService orderService, DriverService driverService, LocationService locationService, CargoService cargoService, WaypointService waypointService, TruckService truckService) {
        this.orderService = orderService;
        this.driverService = driverService;
        this.locationService = locationService;
        this.cargoService = cargoService;
        this.waypointService = waypointService;
        this.truckService = truckService;
    }

    @GetMapping
    public ModelAndView getOrders(@RequestParam(required = false) Optional<Integer> status) {
        ModelAndView mav = new ModelAndView("employeeOrderPage");

        if (status.isPresent()) {
            switch(status.get()) {
            case 0:
                mav.addObject("orders",
                        orderService.getOrdersByStatus(OrderStatus.COMPLETED));
                break;
            case 1:
                mav.addObject("orders",
                        orderService.getOrdersByStatus(OrderStatus.CLOSED));
                break;
            case 2:
                mav.addObject("orders",
                        orderService.getOrdersByStatus(OrderStatus.IN_PROGRESS));
                break;
            default:
                mav.addObject("orders",
                        orderService.getAllOrders());
                break;
            }
        } else {
            mav.addObject("orders", orderService.getAllOrders());
        }

        mav.addObject("order", new OrderDto());

        return mav;
    }

    @GetMapping(value = "/orders", produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getOrders() {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            return mapper.writeValueAsString(orderService.getAllOrders());
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }

    @GetMapping(value = "/orders/{id}", produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String getOrder(@PathVariable Long id) {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            return mapper.writeValueAsString(orderService.getOrderById(id));
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }

    @GetMapping("/edit/{id}")
    public String getOrderEditPage(@PathVariable(name = "id") Long orderId, Model model) {
        final OrderDto editingOrder = orderService.getOrderById(orderId);

        model.addAttribute("order", editingOrder);

        model.addAttribute("suitableTrucks", orderService.getSuitableTrucksForOrder(orderId));

        model.addAttribute("suitableDrivers", orderService.getSuitableDriversForOrder(orderId));

        model.addAttribute("waypoints", waypointService.getAllWaypointsPairByOrderId(orderId));

        model.addAttribute("waypoint", new WaypointsPair());

        model.addAttribute("locations", locationService.getAllLocations());

        model.addAttribute("cargos", cargoService.getUnoccupiedCargos());

        model.addAttribute("orderTravelHours", orderService.getOrderTravelHours(orderId));

        return "employeeOrderEditPage";
    }

    @PostMapping("/edit/{orderId}/addTruck/{truckId}")
    public String setOrderCurrentTruck(@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "truckId") Long truckId) {
        orderService.updateOrderCurrentTruck(orderId, truckService.getTruckById(truckId));

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, orderId);
    }

    @PostMapping("/edit/{orderId}/addDriver/{driverId}")
    public String setOrderCurrentDriver(@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "driverId") Long driverId) {
        orderService.updateOrderCurrentDriver(orderId, driverService.getDriverById(driverId));

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, orderId);
    }

    @DeleteMapping("/edit/{orderId}/deleteCurrentTruck")
    public String deleteOrderCurrentTruck(@PathVariable(name = "orderId") Long orderId) {
        orderService.deleteOrderCurrentTruckWithCurrentDriversAndDates(orderId);

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, orderId);
    }

    @DeleteMapping("/edit/{orderId}/deleteCurrentDriver/{driverId}")
    public String deleteOrderCurrentDriver(@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "driverId") Long driverId) {
        orderService.updateOrderStartAndEndDates(orderId, null, null);

        driverService.updateDriverCurrentOrder(driverId, null);
        driverService.updateDriverCurrentTruck(driverId, null);

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, orderId);
    }

    @PostMapping("/edit/{orderId}/addStartAndEndDates")
    public String addOrderStartAndEndDates(@PathVariable(name = "orderId") Long orderId, @RequestParam(name = "travel-start")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderStartDate, @RequestParam(name = "travel-end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderEndDate) {
        orderService.updateOrderStartAndEndDates(orderId, orderStartDate, orderEndDate);

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, orderId);
    }

    @DeleteMapping("/edit/{orderId}/deleteStartAndEndDates")
    public String deleteOrderStartAndEndDates(@PathVariable(name = "orderId") Long orderId) {
        orderService.deleteOrderStartAndEndDatesWithDrivers(orderId);

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, orderId);
    }

    @PostMapping("/edit/{id}")
    public String addWaypoint(@PathVariable(name = "id") Long id, @ModelAttribute WaypointsPair waypoints) {
        waypointService.saveWaypoint(waypoints.getPair().get(0));

        waypointService.saveWaypoint(waypoints.getPair().get(1));

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, id);
    }

    @PutMapping("/edit/{id}")
    public String updateWaypoint(@PathVariable(name = "id") Long id, @ModelAttribute WaypointsPair waypoints) {
        waypointService.updateWaypoint(waypoints.getPair().get(0));

        waypointService.updateWaypoint(waypoints.getPair().get(1));

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, id);
    }

    @DeleteMapping("/edit/{id}")
    public String deleteWaypointPair(@PathVariable(name = "id") Long id,
                                     @RequestParam(name = "delete-first-waypoint-id") Long firstWaypointId,
                                     @RequestParam(name = "delete-second-waypoint-id") Long secondWaypointId) {
        if (firstWaypointId != null && secondWaypointId != null) {
            waypointService.deleteWaypoint(firstWaypointId);

            waypointService.deleteWaypoint(secondWaypointId);
        }

        return String.format(REDIRECT_TO_ORDER_EDIT_PAGE, id);
    }

    @DeleteMapping
    public String deleteOrder(@RequestParam(name = "delete-order-id") Long orderId) {
        if (orderId != null) {
            orderService.deleteOrder(orderId);
        }

        return REDIRECT_TO_MAIN_PAGE;
    }

    @GetMapping("/close/{orderId}")
    public String closeOrder(@PathVariable(name = "orderId") Long orderId) {
        if (orderId != null) {
            orderService.closeOrder(orderId);
        }

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PostMapping
    public String addOrder(@ModelAttribute OrderDto orderDto) {
        orderService.saveOrder(orderDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PostMapping("/search")
    public ModelAndView searchOrder(@RequestParam(name = "orderId") Long orderId) {
        final ModelAndView mav = new ModelAndView("employeeOrderPage");

        if (orderId != null) {
            try {
                mav.addObject("searchId", orderId);
                mav.addObject("orders", Collections.singletonList(orderService.getOrderById(orderId)));
            } catch (EntityNotFoundException nfe) {
                mav.addObject("searchId", orderId);
                mav.addObject("orders", Collections.emptyList());
            }
        } else {
            mav.addObject("orders", orderService.getAllOrders());
        }

        mav.addObject("order", new OrderDto());

        return mav;
    }
}
