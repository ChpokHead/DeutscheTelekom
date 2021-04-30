package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.OrderDto;
import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.dto.WaypointsPair;
import com.chpok.logiweb.service.CargoService;
import com.chpok.logiweb.service.LocationService;
import com.chpok.logiweb.service.OrderService;
import com.chpok.logiweb.service.WaypointService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employeeOrder")
public class EmployeeOrderPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeOrder";

    private final OrderService orderService;
    private final LocationService locationService;
    private final CargoService cargoService;
    private final WaypointService waypointService;

    public EmployeeOrderPageController(OrderService orderService, LocationService locationService, CargoService cargoService, WaypointService waypointService) {
        this.orderService = orderService;
        this.locationService = locationService;
        this.cargoService = cargoService;
        this.waypointService = waypointService;
    }

    @GetMapping
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());

        model.addAttribute("order", new OrderDto());

        return "employeeOrderPage";
    }

    @GetMapping("/edit/{id}")
    public String getOrderEditPage(@PathVariable(name = "id") Long orderId, Model model) {
        model.addAttribute("orderId", orderId);

        model.addAttribute("waypoint", new WaypointsPair());

        model.addAttribute("waypoints", waypointService.getAllWaypointsPairByOrderId(orderId));

        model.addAttribute("locations", locationService.getAllLocations());

        model.addAttribute("cargos", cargoService.getAllCargos());

        return "employeeOrderEditPage";
    }

    @PostMapping("/edit/{id}")
    public String addWaypoint(@PathVariable(name = "id") Long id, @ModelAttribute WaypointsPair waypoints) {
        waypointService.saveWaypoint(waypoints.getPair().get(0));

        waypointService.saveWaypoint(waypoints.getPair().get(1));

        return "redirect:/employeeOrder/edit/" + id;
    }

    @DeleteMapping("/edit/{id}")
    public String deleteWaypointPair(@PathVariable(name = "id") Long id,
                                     @RequestParam(name = "delete-first-waypoint-id") Long firstWaypointId,
                                     @RequestParam(name = "delete-second-waypoint-id") Long secondWaypointId) {
        if (firstWaypointId != null && secondWaypointId != null) {
            waypointService.deleteWaypoint(firstWaypointId);

            waypointService.deleteWaypoint(secondWaypointId);
        }

        return "redirect:/employeeOrder/edit/" + id;
    }

    @DeleteMapping
    public String deleteOrder(@RequestParam(name = "delete-order-id") Long orderId) {
        if (orderId != null) {
            orderService.deleteOrder(orderId);
        }

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PostMapping
    public String addOrder(@ModelAttribute OrderDto orderDto) {
        orderService.saveOrder(orderDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

}
