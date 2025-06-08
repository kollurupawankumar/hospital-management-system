package com.ingestion.reception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reception/visitors")
public class ReceptionVisitorController {

    @GetMapping
    public String getAllVisitors(Model model) {
        // Placeholder data
        model.addAttribute("visitors", List.of());
        model.addAttribute("title", "Visitor Management");
        
        return "reception/visitors/list";
    }
}