package com.ingestion.laboratory.controller;

import com.ingestion.laboratory.model.LabAnalyzer;
import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.LabResult;
import com.ingestion.laboratory.service.LabAnalyzerService;
import com.ingestion.common.laboratory.LabOrderService;
import com.ingestion.laboratory.service.LabResultService;
import com.ingestion.laboratory.service.LabSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/laboratory")
public class LaboratoryDashboardController {

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("commonLabOrderService")
    private LabOrderService labOrderService;
    
    @Autowired
    private LabSampleService labSampleService;
    
    @Autowired
    private LabResultService labResultService;
    
    @Autowired
    private LabAnalyzerService labAnalyzerService;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        // Get today's date range
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        
        // Count pending orders
        Long pendingOrdersCount = labOrderService.countByStatus(LabOrder.OrderStatus.ORDERED);
        model.addAttribute("pendingOrdersCount", pendingOrdersCount);
        
        // Count pending samples
        Long pendingSamplesCount = labSampleService.countByStatus(com.ingestion.laboratory.model.LabSample.SampleStatus.PENDING);
        model.addAttribute("pendingSamplesCount", pendingSamplesCount);
        
        // Count pending results
        Long pendingResultsCount = labResultService.countByStatus(LabResult.ResultStatus.PENDING);
        model.addAttribute("pendingResultsCount", pendingResultsCount);
        
        // Count completed today
        Long completedTodayCount = labOrderService.countByDateRange(startOfDay, endOfDay);
        model.addAttribute("completedTodayCount", completedTodayCount);
        
        // Count orders by priority
        Long statOrdersCount = (long) labOrderService.findByPriority(LabOrder.OrderPriority.STAT).size();
        Long urgentOrdersCount = (long) labOrderService.findByPriority(LabOrder.OrderPriority.URGENT).size();
        Long routineOrdersCount = (long) labOrderService.findByPriority(LabOrder.OrderPriority.ROUTINE).size();
        
        model.addAttribute("statOrdersCount", statOrdersCount);
        model.addAttribute("urgentOrdersCount", urgentOrdersCount);
        model.addAttribute("routineOrdersCount", routineOrdersCount);
        
        // Get critical results
        List<LabResult> criticalResults = labResultService.findByIsCritical(true);
        model.addAttribute("criticalResults", criticalResults);
        
        // Get pending orders
        List<LabOrder.OrderStatus> pendingStatuses = Arrays.asList(
                LabOrder.OrderStatus.ORDERED,
                LabOrder.OrderStatus.SAMPLE_COLLECTED,
                LabOrder.OrderStatus.IN_PROCESS
        );
        List<LabOrder> pendingOrders = labOrderService.findByStatusesOrderByPriorityAndOrderDate(pendingStatuses);
        model.addAttribute("pendingOrders", pendingOrders);
        
        // Get active analyzers
        List<LabAnalyzer> analyzers = labAnalyzerService.findAllActiveAnalyzers();
        model.addAttribute("analyzers", analyzers);
        
        return "laboratory/dashboard";
    }
}