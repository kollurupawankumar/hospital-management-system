package com.ingestion.controller;

import com.ingestion.security.model.User;
import com.ingestion.security.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {

    private final UserService userService;

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
            
            // Redirect based on role
            if (user.getRoles().contains(User.UserRole.ROLE_ADMIN)) {
                return "redirect:/admin/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_DOCTOR)) {
                return "redirect:/doctor/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_NURSE)) {
                return "redirect:/nurse/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_LAB_TECHNICIAN)) {
                return "redirect:/lab/dashboard";
            } else if (user.getRoles().contains(User.UserRole.ROLE_RECEPTIONIST)) {
                return "redirect:/reception/dashboard";
            }
        }
        
        return "dashboard/home";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
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