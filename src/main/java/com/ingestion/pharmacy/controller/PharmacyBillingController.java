package com.ingestion.pharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pharmacy/billing")
public class PharmacyBillingController {

    @GetMapping
    public String getAllBills(Model model) {
        // Placeholder data
        model.addAttribute("bills", List.of());
        model.addAttribute("title", "Pharmacy Billing");
        
        return "pharmacy/billing/list";
    }
}