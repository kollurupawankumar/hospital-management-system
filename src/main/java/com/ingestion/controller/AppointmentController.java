package com.ingestion.controller;

import com.ingestion.patient.model.Appointment;
import com.ingestion.patient.service.AppointmentService;
import com.ingestion.patient.service.PatientService;
import com.ingestion.security.service.UserService;
import com.ingestion.security.model.User;
import com.ingestion.patient.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllAppointments(Model model, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        
        // Get appointments based on user role
        List<Appointment> appointments;
        if (currentUser.getRoles().contains(User.UserRole.ROLE_ADMIN)) {
            appointments = appointmentService.getAllAppointments();
        } else if (currentUser.getRoles().contains(User.UserRole.ROLE_DOCTOR)) {
            appointments = appointmentService.getAppointmentsByDoctor(currentUser);
        } else {
            appointments = appointmentService.getAllAppointments(); // For now, show all
        }
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("currentUser", currentUser);
        
        return "appointments/list";
    }

    @GetMapping("/new")
    public String newAppointment(Model model) {
        model.addAttribute("appointment", new Appointment());
        
        // Get all patients and doctors for the form
        List<Patient> patients = patientService.getAllPatients();
        List<User> doctors = userService.getUsersByRole(User.UserRole.ROLE_DOCTOR);
        
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        
        return "appointments/form";
    }

    @PostMapping
    public String createAppointment(@ModelAttribute Appointment appointment, Model model) {
        try {
            appointmentService.saveAppointment(appointment);
            model.addAttribute("success", "Appointment scheduled successfully!");
            return "redirect:/appointments";
        } catch (Exception e) {
            model.addAttribute("error", "Error scheduling appointment: " + e.getMessage());
            return "appointments/form";
        }
    }

    @GetMapping("/{id}")
    public String viewAppointment(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment == null) {
            model.addAttribute("error", "Appointment not found");
            return "appointments/list";
        }
        
        model.addAttribute("appointment", appointment);
        return "appointments/details";
    }

    @GetMapping("/{id}/edit")
    public String editAppointment(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment == null) {
            model.addAttribute("error", "Appointment not found");
            return "appointments/list";
        }
        
        model.addAttribute("appointment", appointment);
        
        // Get all patients and doctors for the form
        List<Patient> patients = patientService.getAllPatients();
        List<User> doctors = userService.getUsersByRole(User.UserRole.ROLE_DOCTOR);
        
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        
        return "appointments/form";
    }

    @PostMapping("/{id}")
    public String updateAppointment(@PathVariable Long id, @ModelAttribute Appointment appointment, Model model) {
        try {
            appointment.setId(id);
            appointmentService.saveAppointment(appointment);
            model.addAttribute("success", "Appointment updated successfully!");
            return "redirect:/appointments";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating appointment: " + e.getMessage());
            return "appointments/form";
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/{id}/cancel")
    @ResponseBody
    public String cancelAppointment(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            if (appointment != null) {
                appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
                appointmentService.saveAppointment(appointment);
                return "success";
            }
            return "error";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/{id}/complete")
    @ResponseBody
    public String completeAppointment(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            if (appointment != null) {
                appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
                appointmentService.saveAppointment(appointment);
                return "success";
            }
            return "error";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/calendar")
    public String appointmentCalendar(Model model, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        
        // Get appointments for calendar view
        List<Appointment> appointments;
        if (currentUser.getRoles().contains(User.UserRole.ROLE_ADMIN)) {
            appointments = appointmentService.getAllAppointments();
        } else if (currentUser.getRoles().contains(User.UserRole.ROLE_DOCTOR)) {
            appointments = appointmentService.getAppointmentsByDoctor(currentUser);
        } else {
            appointments = appointmentService.getAllAppointments();
        }
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("currentUser", currentUser);
        
        return "appointments/calendar";
    }
}