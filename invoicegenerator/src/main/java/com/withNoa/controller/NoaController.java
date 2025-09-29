package com.withNoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class NoaController {

    @GetMapping("/noa")
    public String noaHome(Model model) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy HH:mm"));

        model.addAttribute("dateTime", formattedDateTime);
        return "noa-home";
    }
}
