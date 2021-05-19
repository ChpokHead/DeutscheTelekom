package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class EmployeeMainPageController {
    private final UserService userService;

    public EmployeeMainPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/employeePage")
    public String getEmployeeMainPage(HttpSession session, Model model) {
        final EmployeeDto currentEmployee = userService.getEmployeeByUserId((Long) session.getAttribute("userId"));

        model.addAttribute("currentEmployee", currentEmployee);

        return "employeePage";
    }
}
