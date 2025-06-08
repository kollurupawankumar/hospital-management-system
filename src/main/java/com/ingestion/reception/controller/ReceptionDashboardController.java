package com.ingestion.reception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reception")
public class ReceptionDashboardController {

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        // Dashboard statistics
        Map<String, Object> stats = new HashMap<>();
        
        // Basic statistics
        stats.put("todayRegistrations", 25);
        stats.put("pendingTokens", 12);
        stats.put("activeVisitors", 8);
        stats.put("totalAppointments", 45);
        stats.put("checkedInPatients", 32);
        stats.put("waitingPatients", 13);
        
        model.addAttribute("stats", stats);
        
        return "reception/dashboard";
    }
}