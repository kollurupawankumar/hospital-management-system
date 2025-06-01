package com.ingestion.controller;

import com.ingestion.security.model.User;
import com.ingestion.security.service.UserService;
import com.ingestion.patient.service.PatientService;
import com.ingestion.patient.service.AppointmentService;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.common.laboratory.LabOrderService;
import com.ingestion.billing.service.InvoiceService;
import com.ingestion.billing.service.PaymentService;
import com.ingestion.inventory.service.ItemService;
import com.ingestion.inventory.service.InventoryService;
import com.ingestion.common.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private LabOrderService labOrderService;
    
    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Optional<User> userOpt = userService.getUserByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            
            // Add comprehensive dashboard data for all users
            Map<String, Object> dashboardStats = new HashMap<>();
            
            // Basic statistics for all users using available methods
            dashboardStats.put("totalPatients", patientService.getAllPatients().size());
            dashboardStats.put("todayAppointments", 0); // Placeholder
            dashboardStats.put("pendingLabOrders", 0); // Placeholder
            dashboardStats.put("lowStockItems", itemService.findItemsNeedingReorder().size());
            
            // Role-specific data
            if (user.getRoles().contains(User.UserRole.ROLE_ADMIN)) {
                dashboardStats.put("totalDoctors", 0); // Placeholder
                dashboardStats.put("totalRevenue", 0); // Placeholder
                dashboardStats.put("systemUsers", 0); // Placeholder
                model.addAttribute("dashboardStats", dashboardStats);
                return "redirect:/admin/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_DOCTOR)) {
                // Add doctor-specific stats
                model.addAttribute("dashboardStats", dashboardStats);
                return "redirect:/doctor/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_NURSE)) {
                model.addAttribute("dashboardStats", dashboardStats);
                return "redirect:/nurse/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_LAB_TECHNICIAN)) {
                model.addAttribute("dashboardStats", dashboardStats);
                return "redirect:/lab/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_RECEPTIONIST)) {
                model.addAttribute("dashboardStats", dashboardStats);
                return "redirect:/reception/dashboard";
            }
            
            // For users without specific roles, show general dashboard
            model.addAttribute("dashboardStats", dashboardStats);
        }
        
        return "dashboard/home";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        
        // Dashboard Statistics using available methods
        Map<String, Object> stats = new HashMap<>();
        
        // Patient Statistics
        stats.put("totalPatients", patientService.getAllPatients().size());
        stats.put("newPatientsToday", 0); // Placeholder
        stats.put("newPatientsThisWeek", 0); // Placeholder
        stats.put("newPatientsThisMonth", 0); // Placeholder
        
        // Doctor Statistics
        stats.put("totalDoctors", 0); // Placeholder
        stats.put("activeDoctors", 0); // Placeholder
        
        // Appointment Statistics
        stats.put("totalAppointmentsToday", 0); // Placeholder
        stats.put("pendingAppointments", 0); // Placeholder
        stats.put("completedAppointmentsToday", 0); // Placeholder
        stats.put("cancelledAppointmentsToday", 0); // Placeholder
        
        // Lab Order Statistics
        stats.put("pendingLabOrders", 0); // Placeholder
        stats.put("completedLabOrdersToday", 0); // Placeholder
        stats.put("totalLabOrdersThisMonth", 0); // Placeholder
        
        // Financial Statistics
        stats.put("totalRevenueToday", 0); // Placeholder
        stats.put("totalRevenueThisMonth", 0); // Placeholder
        stats.put("pendingPayments", 0); // Placeholder
        stats.put("totalCollectedToday", 0); // Placeholder
        
        // Inventory Statistics
        stats.put("lowStockItems", itemService.findItemsNeedingReorder().size());
        stats.put("expiredItems", inventoryService.findExpiredItems().size());
        stats.put("expiringItemsNext30Days", inventoryService.findExpiringItems(30).size());
        stats.put("pendingPurchaseOrders", 0); // Placeholder
        
        // User Statistics
        stats.put("totalUsers", 0); // Placeholder
        stats.put("activeUsers", 0); // Placeholder
        
        model.addAttribute("stats", stats);
        
        // Recent Activities using available methods
        model.addAttribute("recentPatients", patientService.getAllPatients().stream().limit(5).toList());
        model.addAttribute("todayAppointments", List.of()); // Placeholder
        model.addAttribute("pendingLabOrders", List.of()); // Placeholder
        model.addAttribute("recentInvoices", List.of()); // Placeholder
        model.addAttribute("lowStockItems", itemService.findItemsNeedingReorder().stream().limit(5).toList());
        
        // Chart Data - using placeholder data
        model.addAttribute("monthlyPatientData", List.of(10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65));
        model.addAttribute("monthlyRevenueData", List.of(1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500));
        model.addAttribute("departmentWiseAppointments", Map.of("Cardiology", 25, "Neurology", 20, "Orthopedics", 30, "Pediatrics", 15));
        
        return "dashboard/admin";
    }
    
    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "doctor/simplified-dashboard";
    }
    
    @GetMapping("/nurse/dashboard")
    public String nurseDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "dashboard/nurse";
    }
    
    @GetMapping("/lab/dashboard")
    public String labDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "dashboard/lab";
    }
    
    @GetMapping("/reception/dashboard")
    public String receptionDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "dashboard/reception";
    }
}