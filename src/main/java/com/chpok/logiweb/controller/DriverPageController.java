package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/driverPage")
public class DriverPageController {
    private final DriverService driverService;
    private final TruckService truckService;
    private final UserService userService;
    private final OrderService orderService;
    private final WaypointService waypointService;

    public DriverPageController(DriverService driverService, TruckService truckService, UserService userService, OrderService orderService, WaypointService waypointService) {
        this.driverService = driverService;
        this.truckService = truckService;
        this.userService = userService;
        this.orderService = orderService;
        this.waypointService = waypointService;
    }

    @GetMapping
    public String getDriverPage(Model model, HttpSession session) {
        final Driver driver = userService.getUserById((Long) session.getAttribute("userId")).getDriver();

        model.addAttribute("driver", driverService.getDriverById(driver.getId()));

        model.addAttribute("shiftworkers", truckService.getDriverShiftworkers(driverService.getDriverById(driver.getId())));

        model.addAttribute("orderCompleted", orderService.checkOrderCompleted(driver.getCurrentOrder()));

        return "driverPage";
    }

    @PutMapping
    public String updateDriver(@ModelAttribute DriverDto driver) {
        driverService.updateDriverStatus(driver);

        return "redirect:/driverPage";
    }

    @PutMapping("/updateOrder")
    public String updateOrder(@ModelAttribute DriverDto driver) {
        waypointService.updateWaypointsStatus(driver.getCurrentOrder().getWaypoints());

        return "redirect:/driverPage";
    }

    @PutMapping("/completeOrder")
    public String completeOrder(@ModelAttribute DriverDto driver) {
        driverService.setDriverOrderToNull(driver.getPersonalNumber());
        orderService.updateOrderStatus(driver.getCurrentOrder());

        return "redirect:/driverPage";
    }

}
