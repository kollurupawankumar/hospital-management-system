package com.ingestion.patient.controller;

import com.ingestion.patient.dto.PatientDTO;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.service.PatientService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/patients")

public class PatientController {

    @Autowired
    private PatientService patientService;
    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    @GetMapping
    public String getAllPatients(Model model) {
        log.info("Fetching all patients for display");
        List<PatientDTO> patients = patientService.getAllPatients()
                .stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());
        
        model.addAttribute("patients", patients);
        return "patients/list";
    }

    @GetMapping("/search")
    public String searchPatients(@RequestParam String searchTerm, Model model) {
        log.info("Searching patients with term: {}", searchTerm);
        List<PatientDTO> patients = patientService.searchPatients(searchTerm)
                .stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());
        
        model.addAttribute("patients", patients);
        model.addAttribute("searchTerm", searchTerm);
        return "patients/list";
    }

    @GetMapping("/{id}")
    public String getPatientDetails(@PathVariable Long id, Model model) {
        log.info("Fetching details for patient with ID: {}", id);
        Optional<Patient> patientOpt = patientService.getPatientById(id);
        
        if (patientOpt.isPresent()) {
            PatientDTO patientDTO = PatientDTO.fromEntity(patientOpt.get());
            model.addAttribute("patient", patientDTO);
            return "patients/details";
        } else {
            log.warn("Patient with ID: {} not found", id);
            return "redirect:/patients";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("Displaying patient creation form");
        model.addAttribute("patient", new PatientDTO());
        model.addAttribute("genders", Patient.Gender.values());
        return "patients/form";
    }

    @PostMapping
    public String createPatient(@Valid @ModelAttribute("patient") PatientDTO patientDTO,
                               BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.info("Processing patient creation: {}", patientDTO);
        
        if (result.hasErrors()) {
            log.warn("Validation errors in patient form: {}", result.getAllErrors());
            model.addAttribute("genders", Patient.Gender.values());
            return "patients/form";
        }
        
        Patient patient = patientDTO.toEntity();
        patientService.savePatient(patient);
        
        redirectAttributes.addFlashAttribute("successMessage", "Patient created successfully");
        return "redirect:/patients";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Displaying edit form for patient with ID: {}", id);
        Optional<Patient> patientOpt = patientService.getPatientById(id);
        
        if (patientOpt.isPresent()) {
            PatientDTO patientDTO = PatientDTO.fromEntity(patientOpt.get());
            model.addAttribute("patient", patientDTO);
            model.addAttribute("genders", Patient.Gender.values());
            return "patients/form";
        } else {
            log.warn("Patient with ID: {} not found for editing", id);
            return "redirect:/patients";
        }
    }

    @PostMapping("/{id}")
    public String updatePatient(@PathVariable Long id,
                               @Valid @ModelAttribute("patient") PatientDTO patientDTO,
                               BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.info("Processing update for patient with ID: {}", id);
        
        if (result.hasErrors()) {
            log.warn("Validation errors in patient update form: {}", result.getAllErrors());
            model.addAttribute("genders", Patient.Gender.values());
            return "patients/form";
        }
        
        Optional<Patient> existingPatientOpt = patientService.getPatientById(id);
        if (existingPatientOpt.isPresent()) {
            Patient patient = patientDTO.toEntity();
            patient.setId(id);
            patientService.savePatient(patient);
            
            redirectAttributes.addFlashAttribute("successMessage", "Patient updated successfully");
        } else {
            log.warn("Patient with ID: {} not found for update", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Patient not found");
        }
        
        return "redirect:/patients";
    }

    @GetMapping("/{id}/delete")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Processing deletion for patient with ID: {}", id);
        Optional<Patient> patientOpt = patientService.getPatientById(id);
        
        if (patientOpt.isPresent()) {
            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Patient deleted successfully");
        } else {
            log.warn("Patient with ID: {} not found for deletion", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Patient not found");
        }
        
        return "redirect:/patients";
    }
}