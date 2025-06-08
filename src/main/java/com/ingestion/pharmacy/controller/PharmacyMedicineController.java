package com.ingestion.pharmacy.controller;

import com.ingestion.inventory.service.ItemService;
import com.ingestion.inventory.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/pharmacy/medicines")
public class PharmacyMedicineController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public String getAllMedicines(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String search,
            Model model) {
        
        List<Item> medicines;
        String title = "All Medicines";
        
        if ("low-stock".equals(filter)) {
            medicines = itemService.findItemsNeedingReorder();
            title = "Low Stock Medicines";
        } else if ("expired".equals(filter)) {
            medicines = List.of(); // Placeholder - implement expired items logic
            title = "Expired Medicines";
        } else if (search != null && !search.isEmpty()) {
            medicines = itemService.findByNameContaining(search);
            title = "Search Results for: " + search;
        } else {
            medicines = itemService.findAll();
        }
        
        model.addAttribute("medicines", medicines);
        model.addAttribute("title", title);
        model.addAttribute("filter", filter);
        model.addAttribute("search", search);
        
        return "pharmacy/medicines/list";
    }
}