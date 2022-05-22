package com.chpok.logiweb.controller;

import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.User;
import com.chpok.logiweb.model.enums.UserRole;
import com.chpok.logiweb.config.security.UserDetailsImpl;
import com.chpok.logiweb.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@SessionAttributes({"currentUser"})
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getMainPage(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            prepareErrorMessage(model);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "loginPage";
        }

        User loggedUser = ((UserDetailsImpl)authentication.getPrincipal()).getUserDetails();

        if (loggedUser.getRole().equals(UserRole.ROLE_EMPLOYEE)) {
            return "redirect:employeePage";
        } else {
            return "redirect:driverPage";
        }
    }

    @GetMapping(value = "/logout")
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);

        session.setComplete();

        return "loginPage";
    }

    @PostMapping("/postLogin")
    public String login(Model model, HttpSession session) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        validatePrinciple(authentication.getPrincipal());

        User loggedUser = ((UserDetailsImpl)authentication.getPrincipal()).getUserDetails();

        model.addAttribute("currentUser", loggedUser.getUsername());

        session.setAttribute("userId", loggedUser.getId());

        if (loggedUser.getRole().equals(UserRole.ROLE_EMPLOYEE)) {
            return "redirect:employeePage";
        } else {
            return "redirect:driverPage";
        }
    }

    @PostMapping("/validateDriverUser")
    @ResponseBody
    public ResponseEntity validateDriverUser(@RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) {
        String json;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            Driver driver = Driver.builder().withFirstName("Иван").withLastName("Иванов").build();

            json = mapper.writeValueAsString(driver);
        } catch (JsonProcessingException e) {
            json = "";
            return new ResponseEntity(json, HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(json);
    }


    private void validatePrinciple(Object principal) {
        if (!(principal instanceof UserDetailsImpl)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }

    private void prepareErrorMessage(Model model) {
        model.addAttribute("errorMessage", "Username or Password is incorrect!");
    }
}
