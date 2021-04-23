package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.enums.TruckStatus;
import com.chpok.logiweb.service.TruckService;
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
@RequestMapping("/employeeTruck")
public class EmployeeTruckPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeTruck";

    @Autowired
    private TruckService truckService;

    @GetMapping
    public String getTrucks(Model model) {
        model.addAttribute("trucks", truckService.getAllTrucks());

        model.addAttribute("truck", new TruckDto());

        return "employeeTruckPage";
    }

    @PutMapping
    public String updateTruck(@ModelAttribute TruckDto truckDto) {
        final Truck updatingTruck = Truck.builder()
                .withId(truckDto.getId())
                .withRegNumber(truckDto.getRegNumber())
                .withDriversShift(truckDto.getDriversShift())
                .withCapacity(truckDto.getCapacity())
                .withLocation(truckDto.getLocation())
                .withStatus(TruckStatus.fromInteger(truckDto.getStatus())).build();

        truckService.updateTruck(updatingTruck);

        return REDIRECT_TO_MAIN_PAGE;
    }

    @DeleteMapping
    public String deleteTruck(@RequestParam(name = "delete-truck-id")Long deletingTruckId) {
        if (deletingTruckId != null) {
            truckService.deleteTruck(deletingTruckId);
        }

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PostMapping
    public String addTruck(@ModelAttribute TruckDto truckDto) {
        final Truck addingTruck = Truck.builder()
                .withRegNumber(truckDto.getRegNumber())
                .withDriversShift(truckDto.getDriversShift())
                .withCapacity(truckDto.getCapacity())
                .withLocation(truckDto.getLocation())
                .withStatus(TruckStatus.fromInteger(truckDto.getStatus())).build();

        truckService.saveTruck(addingTruck);

        return REDIRECT_TO_MAIN_PAGE;
    }

}
