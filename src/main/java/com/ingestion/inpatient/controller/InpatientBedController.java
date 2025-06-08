package com.ingestion.inpatient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/inpatient/beds")
public class InpatientBedController {

    @GetMapping
    public String getAllBeds(Model model) {
        // Placeholder data
        model.addAttribute("beds", List.of());
        model.addAttribute("title", "Bed Management");
        
        return "inpatient/beds/list";
    }
}