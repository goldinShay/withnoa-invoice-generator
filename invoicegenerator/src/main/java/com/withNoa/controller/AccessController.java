package com.withNoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // This matches your HTML file name
    }
}
