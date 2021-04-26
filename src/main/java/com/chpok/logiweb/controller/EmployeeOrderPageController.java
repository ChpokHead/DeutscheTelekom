package com.chpok.logiweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employeeOrder")
public class EmployeeOrderPageController {
    @GetMapping
    String getOrders() {
        return "employeeOrderPage";
    }
}
