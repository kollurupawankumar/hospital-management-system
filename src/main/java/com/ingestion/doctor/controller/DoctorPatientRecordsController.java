package com.ingestion.doctor.controller;

import com.ingestion.doctor.dto.DiagnosisDTO;
import com.ingestion.doctor.dto.TreatmentPlanDTO;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.doctor.service.DiagnosisService;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.doctor.service.TreatmentPlanService;
import com.ingestion.patient.model.*;
import com.ingestion.patient.service.*;

import com.ingestion.pharmacy.model.Prescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/doctor/patients")

public class DoctorPatientRecordsController {

    private static final Logger log = LoggerFactory.getLogger(DoctorPatientRecordsController.class);
    @Autowired
    private PatientService patientService;
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private LabReportService labReportService;
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private DiagnosisService diagnosisService;
    @Autowired
    private TreatmentPlanService treatmentPlanService;
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{patientId}/records")
    public String getPatientMedicalRecords(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long doctorId,
            Model model) {
        
        log.info("Accessing medical records for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (!patientOpt.isPresent()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        model.addAttribute("patient", patient);
        
        if (doctorId != null) {
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            doctorOpt.ifPresent(doctor -> model.addAttribute("doctor", doctor));
        }
        
        // Get medical records
        List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecordsByPatientIdOrderByDateDesc(patientId);
        model.addAttribute("medicalRecords", medicalRecords);
        
        return "doctor/patient-records";
    }

    @GetMapping("/{patientId}/lab-reports")
    public String getPatientLabReports(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long doctorId,
            Model model) {
        
        log.info("Accessing lab reports for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (!patientOpt.isPresent()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        model.addAttribute("patient", patient);
        
        if (doctorId != null) {
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            doctorOpt.ifPresent(doctor -> model.addAttribute("doctor", doctor));
        }
        
        // Get lab reports
        List<LabReport> labReports = labReportService.getLabReportsByPatientId(patientId);
        model.addAttribute("labReports", labReports);
        
        return "doctor/patient-lab-reports";
    }

    @GetMapping("/{patientId}/prescriptions")
    public String getPatientPrescriptions(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long doctorId,
            Model model) {
        
        log.info("Accessing prescriptions for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (!patientOpt.isPresent()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        model.addAttribute("patient", patient);
        
        if (doctorId != null) {
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            doctorOpt.ifPresent(doctor -> model.addAttribute("doctor", doctor));
        }
        
        // Get prescriptions
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        model.addAttribute("prescriptions", prescriptions);
        
        return "doctor/patient-prescriptions";
    }

    @GetMapping("/{patientId}/diagnoses")
    public String getPatientDiagnoses(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long doctorId,
            Model model) {
        
        log.info("Accessing diagnoses for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (!patientOpt.isPresent()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        model.addAttribute("patient", patient);
        
        if (doctorId != null) {
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            doctorOpt.ifPresent(doctor -> model.addAttribute("doctor", doctor));
        }
        
        // Get diagnoses
        model.addAttribute("diagnoses", diagnosisService.getDiagnosesByPatientId(patientId));
        
        return "doctor/patient-diagnoses";
    }

    @GetMapping("/{patientId}/diagnoses/new")
    public String showNewDiagnosisForm(
            @PathVariable Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        log.info("Showing new diagnosis form for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("doctor", doctorOpt.get());
        model.addAttribute("diagnosisDTO", new DiagnosisDTO());
        
        return "doctor/diagnosis-form";
    }

    @PostMapping("/{patientId}/diagnoses")
    public String createDiagnosis(
            @PathVariable Long patientId,
            @RequestParam Long doctorId,
            @Valid @ModelAttribute("diagnosisDTO") DiagnosisDTO diagnosisDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        log.info("Creating diagnosis for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        if (result.hasErrors()) {
            Optional<Patient> patientOpt = patientService.getPatientById(patientId);
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            
            if (patientOpt.isPresent() && doctorOpt.isPresent()) {
                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("doctor", doctorOpt.get());
            }
            
            return "doctor/diagnosis-form";
        }
        
        diagnosisService.createDiagnosis(patientId, doctorId, diagnosisDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Diagnosis added successfully");
        
        return "redirect:/doctor/patients/" + patientId + "/diagnoses?doctorId=" + doctorId;
    }

    @GetMapping("/{patientId}/treatment-plans")
    public String getPatientTreatmentPlans(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long doctorId,
            Model model) {
        
        log.info("Accessing treatment plans for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (!patientOpt.isPresent()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        model.addAttribute("patient", patient);
        
        if (doctorId != null) {
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            if (doctorOpt.isPresent()) {
                model.addAttribute("doctor", doctorOpt.get());
            }
        }
        
        // Get treatment plans
        model.addAttribute("treatmentPlans", treatmentPlanService.getTreatmentPlansByPatientId(patientId));
        
        return "doctor/patient-treatment-plans";
    }

    @GetMapping("/{patientId}/treatment-plans/new")
    public String showNewTreatmentPlanForm(
            @PathVariable Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        log.info("Showing new treatment plan form for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("doctor", doctorOpt.get());
        model.addAttribute("treatmentPlanDTO", new TreatmentPlanDTO());
        model.addAttribute("diagnoses", diagnosisService.getDiagnosesByPatientId(patientId));
        
        return "doctor/treatment-plan-form";
    }

    @PostMapping("/{patientId}/treatment-plans")
    public String createTreatmentPlan(
            @PathVariable Long patientId,
            @RequestParam Long doctorId,
            @Valid @ModelAttribute("treatmentPlanDTO") TreatmentPlanDTO treatmentPlanDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        log.info("Creating treatment plan for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        if (result.hasErrors()) {
            Optional<Patient> patientOpt = patientService.getPatientById(patientId);
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            
            if (patientOpt.isPresent() && doctorOpt.isPresent()) {
                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("doctor", doctorOpt.get());
                model.addAttribute("diagnoses", diagnosisService.getDiagnosesByPatientId(patientId));
            }
            
            return "doctor/treatment-plan-form";
        }
        
        treatmentPlanService.createTreatmentPlan(patientId, doctorId, treatmentPlanDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Treatment plan added successfully");
        
        return "redirect:/doctor/patients/" + patientId + "/treatment-plans?doctorId=" + doctorId;
    }

    @GetMapping("/{patientId}/inpatient-care")
    public String getPatientInpatientCare(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long doctorId,
            Model model) {
        
        log.info("Accessing inpatient care for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (!patientOpt.isPresent()) {
            log.warn("Patient with ID: {} not found", patientId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        model.addAttribute("patient", patient);
        
        if (doctorId != null) {
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            if (doctorOpt.isPresent()) {
                model.addAttribute("doctor", doctorOpt.get());
            }
        }
        
        // Get inpatient care records
        model.addAttribute("inpatientCare", diagnosisService.getInpatientCareByPatientId(patientId));
        
        return "doctor/patient-inpatient-care";
    }
}