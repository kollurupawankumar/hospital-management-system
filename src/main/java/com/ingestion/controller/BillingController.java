package com.ingestion.controller;

import com.ingestion.billing.service.InvoiceService;
import com.ingestion.billing.service.PaymentService;
import com.ingestion.security.service.UserService;
import com.ingestion.security.model.User;
import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String billingDashboard(Model model, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        
        // Get billing statistics
        List<Invoice> recentInvoices = invoiceService.findRecentInvoices(5);
        List<Payment> recentPayments = paymentService.findRecentPayments(5);
        
        // Calculate totals
        BigDecimal totalRevenue = invoiceService.getTotalRevenue();
        BigDecimal pendingAmount = invoiceService.getPendingAmount();
        Long totalInvoices = invoiceService.getTotalInvoiceCount();
        Long paidInvoices = invoiceService.getPaidInvoiceCount();
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("recentInvoices", recentInvoices);
        model.addAttribute("recentPayments", recentPayments);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("pendingAmount", pendingAmount);
        model.addAttribute("totalInvoices", totalInvoices);
        model.addAttribute("paidInvoices", paidInvoices);
        model.addAttribute("activeModule", "billing");
        
        return "billing/dashboard";
    }

    @GetMapping("/invoices")
    public String invoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String patientName,
            Model model, 
            Authentication authentication) {
        
        User currentUser = userService.findByUsername(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Invoice> invoices;
        if (status != null && !status.isEmpty()) {
            invoices = invoiceService.findByStatus(status, pageable);
        } else if (patientName != null && !patientName.isEmpty()) {
            invoices = invoiceService.findByPatientNameContaining(patientName, pageable);
        } else {
            invoices = invoiceService.findAll(pageable);
        }
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("invoices", invoices);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", invoices.getTotalPages());
        model.addAttribute("status", status);
        model.addAttribute("patientName", patientName);
        model.addAttribute("activeModule", "billing");
        
        return "billing/invoices";
    }

    @GetMapping("/payments")
    public String payments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String date,
            Model model, 
            Authentication authentication) {
        
        User currentUser = userService.findByUsername(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Payment> payments;
        if (method != null && !method.isEmpty()) {
            payments = paymentService.findByPaymentMethod(method, pageable);
        } else if (date != null && !date.isEmpty()) {
            LocalDate paymentDate = LocalDate.parse(date);
            payments = paymentService.findByPaymentDate(paymentDate, pageable);
        } else {
            payments = paymentService.findAll(pageable);
        }
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("payments", payments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", payments.getTotalPages());
        model.addAttribute("method", method);
        model.addAttribute("date", date);
        model.addAttribute("activeModule", "billing");
        
        return "billing/payments";
    }

    @GetMapping("/reports")
    public String reports(
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model, 
            Authentication authentication) {
        
        User currentUser = userService.findByUsername(authentication.getName());
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("reportType", reportType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("activeModule", "billing");
        
        return "billing/reports";
    }

    @GetMapping("/invoices/{id}")
    public String viewInvoice(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Invoice invoice = invoiceService.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("invoice", invoice);
        model.addAttribute("activeModule", "billing");
        
        return "billing/invoice-details";
    }
}