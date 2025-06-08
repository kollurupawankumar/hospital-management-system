package com.ingestion.pharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pharmacy/prescriptions")
public class PharmacyPrescriptionController {

    @GetMapping
    public String getAllPrescriptions(Model model) {
        // Placeholder data
        model.addAttribute("prescriptions", List.of());
        model.addAttribute("title", "All Prescriptions");
        
        return "pharmacy/prescriptions/list";
    }
}