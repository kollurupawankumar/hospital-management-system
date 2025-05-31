package com.ingestion.reception.controller;

import com.ingestion.reception.dto.PatientRegistrationDTO;
import com.ingestion.reception.model.PatientRegistration;
import com.ingestion.reception.service.PatientRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reception/registration")
public class PatientRegistrationController {

    private final PatientRegistrationService patientRegistrationService;

    public PatientRegistrationController(PatientRegistrationService patientRegistrationService) {
        this.patientRegistrationService = patientRegistrationService;
    }

    @GetMapping
    public String registrationHome(Model model) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        List<PatientRegistrationDTO> todayRegistrations = patientRegistrationService.getRegistrationsByDateRange(startOfDay, endOfDay);
        long todayCount = patientRegistrationService.countRegistrationsByDateRange(startOfDay, endOfDay);
        
        model.addAttribute("todayRegistrations", todayRegistrations);
        model.addAttribute("todayCount", todayCount);
        
        return "reception/registration/index";
    }

    @GetMapping("/new")
    public String showNewRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new PatientRegistrationDTO());
        model.addAttribute("registrationTypes", PatientRegistration.RegistrationType.values());
        
        return "reception/registration/new-registration";
    }

    @PostMapping("/new")
    public String registerNewPatient(
            @Valid @ModelAttribute("registrationDTO") PatientRegistrationDTO registrationDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "reception/registration/new-registration";
        }
        
        try {
            PatientRegistrationDTO savedRegistration = patientRegistrationService.registerNewPatient(registrationDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Patient registered successfully with registration number: " + savedRegistration.getRegistrationNumber());
            return "redirect:/reception/registration/details/" + savedRegistration.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error registering patient: " + e.getMessage());
            return "redirect:/reception/registration/new";
        }
    }

    @GetMapping("/existing")
    public String showExistingPatientRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new PatientRegistrationDTO());
        model.addAttribute("registrationTypes", PatientRegistration.RegistrationType.values());
        
        return "reception/registration/existing-patient";
    }

    @PostMapping("/existing")
    public String registerExistingPatient(
            @Valid @ModelAttribute("registrationDTO") PatientRegistrationDTO registrationDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "reception/registration/existing-patient";
        }
        
        try {
            PatientRegistrationDTO savedRegistration = patientRegistrationService.registerExistingPatient(registrationDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Patient registered successfully with registration number: " + savedRegistration.getRegistrationNumber());
            return "redirect:/reception/registration/details/" + savedRegistration.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error registering patient: " + e.getMessage());
            return "redirect:/reception/registration/existing";
        }
    }

    @GetMapping("/details/{id}")
    public String showRegistrationDetails(@PathVariable Long id, Model model) {
        PatientRegistrationDTO registration = patientRegistrationService.getRegistrationById(id);
        model.addAttribute("registration", registration);
        
        return "reception/registration/details";
    }

    @GetMapping("/search")
    public String searchRegistrations(
            @RequestParam(required = false) String registrationNumber,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) PatientRegistration.RegistrationType type,
            Model model) {
        
        List<PatientRegistrationDTO> registrations = null;
        
        if (registrationNumber != null && !registrationNumber.isEmpty()) {
            try {
                PatientRegistrationDTO registration = patientRegistrationService.getRegistrationByNumber(registrationNumber);
                registrations = List.of(registration);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Registration not found with number: " + registrationNumber);
            }
        } else if (patientId != null) {
            registrations = patientRegistrationService.getRegistrationsByPatientId(patientId);
        } else if (startDate != null && endDate != null) {
            if (type != null) {
                registrations = patientRegistrationService.getRegistrationsByTypeAndDateRange(type, startDate, endDate);
            } else {
                registrations = patientRegistrationService.getRegistrationsByDateRange(startDate, endDate);
            }
        }
        
        model.addAttribute("registrations", registrations);
        model.addAttribute("registrationTypes", PatientRegistration.RegistrationType.values());
        
        return "reception/registration/search";
    }

    @GetMapping("/patient/{patientId}")
    public String getPatientRegistrations(@PathVariable Long patientId, Model model) {
        List<PatientRegistrationDTO> registrations = patientRegistrationService.getRegistrationsByPatientId(patientId);
        model.addAttribute("registrations", registrations);
        
        return "reception/registration/patient-registrations";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateRegistration(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            patientRegistrationService.deactivateRegistration(id);
            redirectAttributes.addFlashAttribute("successMessage", "Registration deactivated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deactivating registration: " + e.getMessage());
        }
        
        return "redirect:/reception/registration/details/" + id;
    }
}