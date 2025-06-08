package com.ingestion.pharmacy.controller;

import com.ingestion.inventory.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyDashboardController {
    
    @Autowired
    private ItemService itemService;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        // Get today's date range
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        
        // Dashboard statistics
        Map<String, Object> stats = new HashMap<>();
        
        // Basic statistics
        stats.put("totalMedicines", itemService.findAll().size());
        stats.put("lowStockMedicines", itemService.findItemsNeedingReorder().size());
        stats.put("expiredMedicines", 0); // Placeholder
        stats.put("todaySales", 0); // Placeholder
        stats.put("pendingPrescriptions", 0); // Placeholder
        stats.put("totalRevenue", 0.0); // Placeholder
        
        model.addAttribute("stats", stats);
        model.addAttribute("lowStockItems", itemService.findItemsNeedingReorder());
        
        return "pharmacy/dashboard";
    }
}