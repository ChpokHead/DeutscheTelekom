package com.chpok.logiweb.controller;

import com.chpok.logiweb.model.User;
import com.chpok.logiweb.model.enums.UserRole;
import com.chpok.logiweb.config.security.UserDetailsImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@SessionAttributes({"currentUser"})
@Controller
public class LoginController {
    private final Logger logger = LogManager.getLogger();

    @GetMapping("/login")
    public String getMainPage(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            prepareErrorMessage(model);
        }

        return "loginPage";
    }

    @GetMapping(value = "/logout")
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);

        session.setComplete();

        return "redirect:/login";
    }

    @PostMapping("/postLogin")
    String login(Model model, HttpSession session) {
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

    private void validatePrinciple(Object principal) {
        if (!(principal instanceof UserDetailsImpl)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }

    private void prepareErrorMessage(Model model) {
        model.addAttribute("errorMessage", "Username or Password is incorrect!");
    }

}
