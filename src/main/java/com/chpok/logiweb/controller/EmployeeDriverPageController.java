package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.enums.DriverStatus;
import com.chpok.logiweb.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/employeeDriver")
public class EmployeeDriverPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeDriver";

    private DriverService driverService;

    @GetMapping
    public String getDrivers(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());

        model.addAttribute("driver", new DriverDto());

        return "employeeDriverPage";
    }

    @PostMapping
    public String addDriver(@ModelAttribute DriverDto driverDto) {
        final Driver addingDriver = Driver.builder()
                .withFirstName(driverDto.getFirstName())
                .withLastName(driverDto.getLastName())
                .withPersonalNumber(driverDto.getPersonalNumber())
                .withLocation(driverDto.getLocation())
                .build();

        driverService.saveDriver(addingDriver);

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
        final Driver updatingDriver = Driver.builder()
                .withId(driverDto.getId())
                .withFirstName(driverDto.getFirstName())
                .withLastName(driverDto.getLastName())
                .withPersonalNumber(driverDto.getPersonalNumber())
                .withLocation(driverDto.getLocation())
                .withStatus(DriverStatus.fromInteger(driverDto.getStatus())).build();

        driverService.updateDriver(updatingDriver);

        return REDIRECT_TO_MAIN_PAGE;
    }

}
