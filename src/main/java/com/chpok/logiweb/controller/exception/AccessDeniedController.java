package com.chpok.logiweb.controller.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {
    @GetMapping("/accessDenied")
    public String getAccessDeniedPage() {
        return "/forbiddenPage";
    }
}
