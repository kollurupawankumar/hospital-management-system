package com.ingestion.laboratory.controller;

import com.ingestion.laboratory.model.LabResult;
import com.ingestion.laboratory.service.LabResultService;
import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.common.laboratory.LabOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/laboratory/results")
public class LabResultController {

    @Autowired
    private LabResultService labResultService;
    
    @Autowired
    @Qualifier("commonLabOrderService")
    private LabOrderService labOrderService;

    @GetMapping
    public String getAllResults(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String critical,
            @RequestParam(required = false) String date,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("resultDate").descending());
        List<LabResult> results;
        
        if (status != null && !status.isEmpty()) {
            try {
                LabResult.ResultStatus resultStatus = LabResult.ResultStatus.valueOf(status);
                results = labResultService.findByStatus(resultStatus);
                model.addAttribute("status", status);
            } catch (IllegalArgumentException e) {
                results = labResultService.findAll();
                model.addAttribute("errorMessage", "Invalid status");
            }
        } else if ("true".equals(critical)) {
            results = labResultService.findByIsCritical(true);
            model.addAttribute("critical", critical);
        } else if (date != null && !date.isEmpty()) {
            try {
                LocalDate searchDate = LocalDate.parse(date);
                LocalDateTime startOfDay = searchDate.atStartOfDay();
                LocalDateTime endOfDay = searchDate.atTime(LocalTime.MAX);
                results = labResultService.findByResultDateRange(startOfDay, endOfDay);
                model.addAttribute("date", date);
            } catch (Exception e) {
                results = labResultService.findAll();
                model.addAttribute("errorMessage", "Invalid date format");
            }
        } else {
            // Get all results
            results = labResultService.findAll();
        }
        
        model.addAttribute("results", results);
        model.addAttribute("statuses", LabResult.ResultStatus.values());
        
        return "laboratory/results/list";
    }

    @GetMapping("/pending")
    public String getPendingResults(Model model) {
        List<LabResult> pendingResults = labResultService.findByStatus(LabResult.ResultStatus.PENDING);
        model.addAttribute("results", pendingResults);
        model.addAttribute("title", "Pending Results");
        return "laboratory/results/list";
    }

    @GetMapping("/critical")
    public String getCriticalResults(Model model) {
        List<LabResult> criticalResults = labResultService.findByIsCritical(true);
        model.addAttribute("results", criticalResults);
        model.addAttribute("title", "Critical Results");
        return "laboratory/results/list";
    }

    @GetMapping("/today")
    public String getTodayResults(Model model) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        List<LabResult> todayResults = labResultService.findByResultDateRange(startOfDay, endOfDay);
        model.addAttribute("results", todayResults);
        model.addAttribute("title", "Today's Results");
        return "laboratory/results/list";
    }
}