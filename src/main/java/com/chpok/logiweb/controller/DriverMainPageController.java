package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/driverPage")
public class DriverMainPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/driverPage";

    private final DriverService driverService;
    private final TruckService truckService;
    private final UserService userService;
    private final OrderService orderService;
    private final WaypointService waypointService;

    public DriverMainPageController(DriverService driverService, TruckService truckService, UserService userService, OrderService orderService, WaypointService waypointService) {
        this.driverService = driverService;
        this.truckService = truckService;
        this.userService = userService;
        this.orderService = orderService;
        this.waypointService = waypointService;
    }

    @GetMapping
    public String getDriverPage(Model model, HttpSession session) {
        final DriverDto currentDriver = userService.getDriverByUserId((Long) session.getAttribute("userId"));

        model.addAttribute("driver", currentDriver);

        model.addAttribute("shiftworker", truckService.getDriverShiftworker(currentDriver));

        model.addAttribute("isOrderCompleted", orderService.checkOrderIsCompleted(currentDriver.getCurrentOrder()));

        return "driverPage";
    }

    @PutMapping
    public String updateDriver(@ModelAttribute DriverDto driver, Model model) {
        driverService.updateDriverAndShiftWorkerStatus(driver.getPersonalNumber(), DriverStatus.fromInteger(driver.getStatus()));

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PutMapping("/updateOrder")
    public String updateOrder(@ModelAttribute DriverDto driver) {
        waypointService.updateWaypointsStatus(driver.getCurrentOrder().getWaypoints());

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PutMapping("/completeOrder")
    public String completeOrder(@ModelAttribute DriverDto driver) {
        orderService.updateOrderStatus(driver.getCurrentOrder());

        return REDIRECT_TO_MAIN_PAGE;
    }

}
