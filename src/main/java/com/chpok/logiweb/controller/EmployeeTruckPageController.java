package com.chpok.logiweb.controller;

import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.dto.TruckDto;
import com.chpok.logiweb.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

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

    @GetMapping(value = "/{id}")
    @ResponseBody
    public TruckDto getTruck(@PathVariable Long id) {
        try {
            return truckService.getTruckById(id);
        } catch (DatabaseRuntimeException dre) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Truck not found", dre);
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
