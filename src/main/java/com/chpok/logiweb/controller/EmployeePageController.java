package com.chpok.logiweb.controller;

import com.chpok.logiweb.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeePageController {
    @GetMapping("/employeePage")
    String getEmployeePage(Model model) {
        return "employeePage";
    }
}
