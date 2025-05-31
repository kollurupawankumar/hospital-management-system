package com.ingestion.reception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reception")
public class ReceptionHomeController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Reception Dashboard");
        return "reception/home";
    }
}