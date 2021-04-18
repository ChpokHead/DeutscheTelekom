package com.chpok.logiweb.controller;

import com.chpok.logiweb.form.TruckForm;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.TruckStatus;
import com.chpok.logiweb.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class EmployeeTruckPageController {
    @Autowired
    TruckService truckService;

    @GetMapping("/employeeTruckPage")
    String getEmployeeTrucks(Model model) {
        model.addAttribute("trucks", truckService.findAll());
        model.addAttribute("truckForm", new TruckForm());
        return "employeeTruckPage";
    }

    @PostMapping("/employeeTruckPage")
    ModelAndView updateEmployeeTrucks(@ModelAttribute TruckForm truckForm) {
        final Truck truck = Truck.builder()
                .withId(truckForm.getId())
                .withRegNumber(truckForm.getRegNumber())
                .withDriversShift(truckForm.getDriversShift())
                .withCapacity(truckForm.getCapacity())
                .withLocation(truckForm.getLocation())
                .withStatus(TruckStatus.fromInteger(truckForm.getStatus())).build();

        truckService.update(truck);

        return new ModelAndView("employeeTruckPage", "trucks", truckService.findAll());
    }
}
