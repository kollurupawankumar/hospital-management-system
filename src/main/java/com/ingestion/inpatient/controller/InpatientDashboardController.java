package com.ingestion.inpatient.controller;

import com.ingestion.common.repository.InpatientAdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/inpatient")
public class InpatientDashboardController {

    @Autowired
    private InpatientAdmissionRepository admissionRepository;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        // Dashboard statistics
        Map<String, Object> stats = new HashMap<>();
        
        // Basic statistics
        stats.put("totalAdmissions", admissionRepository.count());
        stats.put("activeAdmissions", 0); // Placeholder
        stats.put("totalBeds", 120); // Placeholder
        stats.put("occupiedBeds", 89); // Placeholder
        stats.put("availableBeds", 31); // Placeholder
        stats.put("occupancyRate", "74%"); // Placeholder
        
        model.addAttribute("stats", stats);
        model.addAttribute("recentAdmissions", admissionRepository.findAll());
        
        return "inpatient/dashboard";
    }
}