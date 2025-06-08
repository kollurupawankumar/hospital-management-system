package com.ingestion.inpatient.controller;

import com.ingestion.common.repository.InpatientAdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inpatient/admissions")
public class InpatientAdmissionController {

    @Autowired
    private InpatientAdmissionRepository admissionRepository;

    @GetMapping
    public String getAllAdmissions(Model model) {
        model.addAttribute("admissions", admissionRepository.findAll());
        model.addAttribute("title", "All Admissions");
        
        return "inpatient/admissions-list";
    }
}