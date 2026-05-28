package com.soap.envmonitorsystem2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login.html";
    }
}
