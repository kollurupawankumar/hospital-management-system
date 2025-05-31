package com.ingestion.doctor.controller;

import com.ingestion.doctor.dto.PrescriptionDTO;
import com.ingestion.doctor.dto.PrescriptionItemDTO;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.patient.service.PatientService;
import com.ingestion.patient.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/doctor/prescriptions")
public class DoctorPrescriptionController {

    private static final Logger log = LoggerFactory.getLogger(DoctorPrescriptionController.class);
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;


    @GetMapping("/new")
    public String showNewPrescriptionForm(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        log.info("Showing new prescription form for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setPatientId(patientId);
        prescriptionDTO.setDoctorId(doctorId);
        prescriptionDTO.setPatientName(patient.getFirstName() + " " + patient.getLastName());
        prescriptionDTO.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
        
        // Add one empty medication item by default
        List<PrescriptionItemDTO> items = new ArrayList<>();
        items.add(new PrescriptionItemDTO());
        prescriptionDTO.setPrescriptionItems(items);
        
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("prescriptionDTO", prescriptionDTO);
        
        return "doctor/prescription-form";
    }

    @PostMapping
    public String createPrescription(
            @Valid @ModelAttribute("prescriptionDTO") PrescriptionDTO prescriptionDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        log.info("Creating prescription for patient ID: {} by doctor ID: {}", 
                prescriptionDTO.getPatientId(), prescriptionDTO.getDoctorId());
        
        if (result.hasErrors()) {
            Optional<Patient> patientOpt = patientService.getPatientById(prescriptionDTO.getPatientId());
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(prescriptionDTO.getDoctorId());
            
            if (patientOpt.isPresent() && doctorOpt.isPresent()) {
                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("doctor", doctorOpt.get());
            }
            
            return "doctor/prescription-form";
        }
        
        Prescription prescription = prescriptionService.createPrescriptionFromDTO(prescriptionDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Prescription created successfully");
        
        return "redirect:/doctor/patients/" + prescriptionDTO.getPatientId() + 
                "/prescriptions?doctorId=" + prescriptionDTO.getDoctorId();
    }

    @GetMapping("/{id}")
    public String getPrescriptionDetails(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        log.info("Viewing prescription ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<Prescription> prescriptionOpt = prescriptionService.getPrescriptionById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!prescriptionOpt.isPresent() || !doctorOpt.isPresent()) {
            log.warn("Prescription ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Prescription prescription = prescriptionOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("prescription", prescription);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", prescription.getPatient());
        
        return "doctor/prescription-details";
    }

    @GetMapping("/{id}/print")
    public String printPrescription(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        log.info("Printing prescription ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<Prescription> prescriptionOpt = prescriptionService.getPrescriptionById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!prescriptionOpt.isPresent() || !doctorOpt.isPresent()) {
            log.warn("Prescription ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Prescription prescription = prescriptionOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("prescription", prescription);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", prescription.getPatient());
        
        return "doctor/prescription-print";
    }

    @PostMapping("/{id}/deactivate")
    public String deactivatePrescription(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            RedirectAttributes redirectAttributes) {
        
        log.info("Deactivating prescription ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<Prescription> prescriptionOpt = prescriptionService.getPrescriptionById(id);
        
        if (!prescriptionOpt.isPresent()) {
            log.warn("Prescription ID: {} not found", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Prescription not found");
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Prescription prescription = prescriptionOpt.get();
        prescriptionService.deactivatePrescription(id);
        
        redirectAttributes.addFlashAttribute("successMessage", "Prescription deactivated successfully");
        return "redirect:/doctor/patients/" + prescription.getPatient().getId() + 
                "/prescriptions?doctorId=" + doctorId;
    }

    @GetMapping("/doctor/{doctorId}")
    public String getDoctorPrescriptions(
            @PathVariable Long doctorId,
            Model model) {
        
        log.info("Viewing all prescriptions by doctor ID: {}", doctorId);
        
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!doctorOpt.isPresent()) {
            log.warn("Doctor ID: {} not found", doctorId);
            return "redirect:/error";
        }
        
        Doctor doctor = doctorOpt.get();
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
        
        model.addAttribute("doctor", doctor);
        model.addAttribute("prescriptions", prescriptions);
        
        return "doctor/doctor-prescriptions";
    }
}