package com.chpok.logiweb.controller;

import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.service.DriverService;
import com.chpok.logiweb.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/employeeDriver")
public class EmployeeDriverPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeDriver";

    private DriverService driverService;
    private LocationService locationService;

    public EmployeeDriverPageController(DriverService driverService, LocationService locationService) {
        this.driverService = driverService;
        this.locationService = locationService;
    }

    @GetMapping
    public String getDrivers(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());

        model.addAttribute("driver", new DriverDto());

        model.addAttribute("locations", locationService.getAllLocations());

        return "employeeDriverPage";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public DriverDto getDriver(@PathVariable Long id) {
        try {
            return driverService.getDriverById(id);
        } catch (DatabaseRuntimeException dre) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Driver not found", dre);
        }
    }

    @PostMapping
    public String addDriver(@ModelAttribute DriverDto driverDto) {
        driverService.saveDriver(driverDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

    @DeleteMapping
    public String deleteDriver(@RequestParam(name = "delete-driver-id") Long id) {
        if (id != null) {
            driverService.deleteDriver(id);
        }

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PutMapping
    public String updateDriver(@ModelAttribute DriverDto driverDto) {
        driverService.updateDriver(driverDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

}
