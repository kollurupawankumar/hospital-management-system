package com.ingestion.reception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reception/tokens")
public class ReceptionTokenController {

    @GetMapping
    public String getAllTokens(Model model) {
        // Placeholder data
        model.addAttribute("tokens", List.of());
        model.addAttribute("title", "Token Management");
        
        return "reception/tokens/list";
    }
}