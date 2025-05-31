package com.ingestion.patient.controller;

import com.ingestion.patient.dto.MedicalRecordDTO;
import com.ingestion.patient.model.MedicalRecord;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.service.MedicalRecordService;
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
@RequestMapping("/patients/{patientId}/records")
public class MedicalRecordController {

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordController.class);
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private PatientService patientService;

    @GetMapping
    public String getAllMedicalRecords(@PathVariable Long patientId, Model model) {
        log.info("Fetching all medical records for patient ID: {}", patientId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (!patientOpt.isPresent()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/patients";
        }
        
        Patient patient = patientOpt.get();
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByPatientIdOrderByDateDesc(patientId)
                .stream()
                .map(MedicalRecordDTO::fromEntity)
                .collect(Collectors.toList());
        
        model.addAttribute("patient", patient);
        model.addAttribute("records", records);
        return "records/list";
    }

    @GetMapping("/{id}")
    public String getMedicalRecordDetails(@PathVariable Long patientId, 
                                         @PathVariable Long id, Model model) {
        log.info("Fetching details for medical record ID: {} of patient ID: {}", id, patientId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/patients";
        }
        
        Optional<MedicalRecord> recordOpt = medicalRecordService.getMedicalRecordById(id);
        if (recordOpt.isEmpty()) {
            log.warn("Medical record with ID: {} not found", id);
            return "redirect:/patients/" + patientId + "/records";
        }
        
        MedicalRecord record = recordOpt.get();
        if (!record.getPatient().getId().equals(patientId)) {
            log.warn("Medical record ID: {} does not belong to patient ID: {}", id, patientId);
            return "redirect:/patients/" + patientId + "/records";
        }
        
        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("record", MedicalRecordDTO.fromEntity(record));
        return "records/details";
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable Long patientId, Model model) {
        log.info("Displaying medical record creation form for patient ID: {}", patientId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/patients";
        }
        
        MedicalRecordDTO recordDTO = new MedicalRecordDTO();
        recordDTO.setPatientId(patientId);
        recordDTO.setPatientName(patientOpt.get().getFirstName() + " " + patientOpt.get().getLastName());
        
        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("record", recordDTO);
        return "records/form";
    }

    @PostMapping
    public String createMedicalRecord(@PathVariable Long patientId,
                                     @Valid @ModelAttribute("record") MedicalRecordDTO recordDTO,
                                     BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.info("Processing medical record creation for patient ID: {}", patientId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/patients";
        }
        
        if (result.hasErrors()) {
            log.warn("Validation errors in medical record form: {}", result.getAllErrors());
            model.addAttribute("patient", patientOpt.get());
            return "records/form";
        }
        
        Patient patient = patientOpt.get();
        MedicalRecord record = recordDTO.toEntity(patient);
        medicalRecordService.saveMedicalRecord(record);
        
        redirectAttributes.addFlashAttribute("successMessage", "Medical record created successfully");
        return "redirect:/patients/" + patientId + "/records";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long patientId, @PathVariable Long id, Model model) {
        log.info("Displaying edit form for medical record ID: {} of patient ID: {}", id, patientId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/patients";
        }
        
        Optional<MedicalRecord> recordOpt = medicalRecordService.getMedicalRecordById(id);
        if (recordOpt.isEmpty()) {
            log.warn("Medical record with ID: {} not found", id);
            return "redirect:/patients/" + patientId + "/records";
        }
        
        MedicalRecord record = recordOpt.get();
        if (!record.getPatient().getId().equals(patientId)) {
            log.warn("Medical record ID: {} does not belong to patient ID: {}", id, patientId);
            return "redirect:/patients/" + patientId + "/records";
        }
        
        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("record", MedicalRecordDTO.fromEntity(record));
        return "records/form";
    }

    @PostMapping("/{id}")
    public String updateMedicalRecord(@PathVariable Long patientId, @PathVariable Long id,
                                     @Valid @ModelAttribute("record") MedicalRecordDTO recordDTO,
                                     BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.info("Processing update for medical record ID: {} of patient ID: {}", id, patientId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/patients";
        }
        
        if (result.hasErrors()) {
            log.warn("Validation errors in medical record update form: {}", result.getAllErrors());
            model.addAttribute("patient", patientOpt.get());
            return "records/form";
        }
        
        Optional<MedicalRecord> existingRecordOpt = medicalRecordService.getMedicalRecordById(id);
        if (existingRecordOpt.isEmpty()) {
            log.warn("Medical record with ID: {} not found for update", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Medical record not found");
            return "redirect:/patients/" + patientId + "/records";
        }
        
        Patient patient = patientOpt.get();
        MedicalRecord record = recordDTO.toEntity(patient);
        record.setId(id);
        medicalRecordService.saveMedicalRecord(record);
        
        redirectAttributes.addFlashAttribute("successMessage", "Medical record updated successfully");
        return "redirect:/patients/" + patientId + "/records";
    }

    @GetMapping("/{id}/delete")
    public String deleteMedicalRecord(@PathVariable Long patientId, @PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        log.info("Processing deletion for medical record ID: {} of patient ID: {}", id, patientId);
        
        Optional<MedicalRecord> recordOpt = medicalRecordService.getMedicalRecordById(id);
        if (recordOpt.isEmpty()) {
            log.warn("Medical record with ID: {} not found for deletion", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Medical record not found");
            return "redirect:/patients/" + patientId + "/records";
        }
        
        MedicalRecord record = recordOpt.get();
        if (!record.getPatient().getId().equals(patientId)) {
            log.warn("Medical record ID: {} does not belong to patient ID: {}", id, patientId);
            redirectAttributes.addFlashAttribute("errorMessage", "Medical record does not belong to this patient");
            return "redirect:/patients/" + patientId + "/records";
        }
        
        medicalRecordService.deleteMedicalRecord(id);
        redirectAttributes.addFlashAttribute("successMessage", "Medical record deleted successfully");
        return "redirect:/patients/" + patientId + "/records";
    }
}