package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.service.DriverService;
import com.chpok.logiweb.service.LocationService;
import com.chpok.logiweb.service.TruckService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/employeeDriver")
public class EmployeeDriverPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeDriver";

    private final DriverService driverService;
    private final LocationService locationService;
    private final TruckService truckService;

    public EmployeeDriverPageController(DriverService driverService, LocationService locationService, TruckService truckService) {
        this.driverService = driverService;
        this.locationService = locationService;
        this.truckService = truckService;
    }

    @GetMapping
    public String getDrivers(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());

        model.addAttribute("trucks", truckService.getAllTrucks());

        model.addAttribute("driver", new DriverDto());

        model.addAttribute("locations", locationService.getAllLocations());

        return "employeeDriverPage";
    }

    @GetMapping(value = "/{id}", produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getDriver(@PathVariable Long id) {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(driverService.getDriverById(id));
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
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
