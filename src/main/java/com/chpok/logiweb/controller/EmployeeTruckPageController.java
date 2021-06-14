package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.service.LocationService;
import com.chpok.logiweb.service.TruckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/employeeTruck")
public class EmployeeTruckPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeTruck";

    private final TruckService truckService;
    private final LocationService locationService;

    public EmployeeTruckPageController(TruckService truckService, LocationService locationService) {
        this.truckService = truckService;
        this.locationService = locationService;
    }

    @GetMapping
    public String getTrucks(Model model) {
        model.addAttribute("trucks", truckService.getAllTrucks());

        model.addAttribute("truck", new TruckDto());

        model.addAttribute("locations", locationService.getAllLocations());

        return "employeeTruckPage";
    }

    @GetMapping(value = "/trucks", produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String getTrucks(@RequestParam(name = "location") Optional<String> locationName) {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            if (locationName.isPresent()) {
                return mapper.writeValueAsString(truckService.getTrucksAtLocationByName(locationName.get()));
            } else {
                return mapper.writeValueAsString(truckService.getAllTrucks());
            }
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }

    @GetMapping(value = "/trucks/{id}", produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String getTruck(@PathVariable Long id) {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            return mapper.writeValueAsString(truckService.getTruckById(id));
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }

    @PutMapping
    public String updateTruck(@ModelAttribute TruckDto truckDto) {
        truckService.updateTruck(truckDto);

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
        truckService.saveTruck(truckDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

}
