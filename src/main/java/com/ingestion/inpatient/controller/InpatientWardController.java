package com.ingestion.inpatient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/inpatient/wards")
public class InpatientWardController {

    @GetMapping
    public String getAllWards(Model model) {
        // Placeholder data
        model.addAttribute("wards", List.of());
        model.addAttribute("title", "Ward Management");
        
        return "inpatient/wards/list";
    }
}