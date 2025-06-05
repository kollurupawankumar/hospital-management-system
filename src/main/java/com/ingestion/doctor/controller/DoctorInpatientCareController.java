package com.ingestion.doctor.controller;

import com.ingestion.doctor.dto.DischargeSummaryDTO;
import com.ingestion.doctor.dto.InpatientNoteDTO;
import com.ingestion.doctor.dto.RoundNoteDTO;
import com.ingestion.doctor.model.DischargeSummary;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.doctor.model.InpatientNote;
import com.ingestion.doctor.model.RoundNote;
import com.ingestion.common.model.inpatient.InpatientAdmission;
import com.ingestion.doctor.service.InpatientCareService;
import com.ingestion.patient.model.Patient;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.patient.service.PatientService;
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
@RequestMapping("/doctor/inpatient-care")

public class DoctorInpatientCareController {

    @Autowired
    private  InpatientCareService inpatientCareService;
    @Autowired
    private  PatientService patientService;
    @Autowired
    private  DoctorService doctorService;

    @GetMapping("/patients")
    public String getInpatients(
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing inpatients for doctor ID: {}", doctorId);
        
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (doctorOpt.isEmpty()) {
            //log.warn("Doctor ID: {} not found", doctorId);
            return "redirect:/error";
        }
        
        Doctor doctor = doctorOpt.get();
        List<InpatientAdmission> inpatientAdmissions = inpatientCareService.getCurrentInpatients();
        
        model.addAttribute("doctor", doctor);
        model.addAttribute("inpatients", inpatientAdmissions);
        
        return "doctor/inpatients-list";
    }

    @GetMapping("/patient/{patientId}")
    public String getPatientInpatientCare(
            @PathVariable Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing inpatient care for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("inpatientNotes", inpatientCareService.getInpatientNotesByPatientId(patientId));
        model.addAttribute("roundNotes", inpatientCareService.getRoundNotesByPatientId(patientId));
        model.addAttribute("dischargeSummaries", inpatientCareService.getDischargeSummariesByPatientId(patientId));
        model.addAttribute("currentAdmission", inpatientCareService.getCurrentAdmissionForPatient(patientId));
        
        return "doctor/patient-inpatient-care";
    }

    @GetMapping("/notes/new")
    public String showNewInpatientNoteForm(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Showing new inpatient note form for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        InpatientNoteDTO noteDTO = new InpatientNoteDTO();
        noteDTO.setPatientId(patientId);
        noteDTO.setDoctorId(doctorId);
        
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("noteDTO", noteDTO);
        model.addAttribute("noteTypes", inpatientCareService.getInpatientNoteTypes());
        
        return "doctor/inpatient-note-form";
    }

    @PostMapping("/notes")
    public String createInpatientNote(
            @Valid @ModelAttribute("noteDTO") InpatientNoteDTO noteDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        //log.info("Creating inpatient note for patient ID: {} by doctor ID: {}", 
                //noteDTO.getPatientId(), noteDTO.getDoctorId());
        
        if (result.hasErrors()) {
            Optional<Patient> patientOpt = patientService.getPatientById(noteDTO.getPatientId());
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(noteDTO.getDoctorId());
            
            if (patientOpt.isPresent() && doctorOpt.isPresent()) {
                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("doctor", doctorOpt.get());
                model.addAttribute("noteTypes", inpatientCareService.getInpatientNoteTypes());
            }
            
            return "doctor/inpatient-note-form";
        }
        
        InpatientNote note = inpatientCareService.createInpatientNoteFromDTO(noteDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Inpatient note created successfully");
        
        return "redirect:/doctor/inpatient-care/patient/" + noteDTO.getPatientId() + 
                "?doctorId=" + noteDTO.getDoctorId();
    }

    @GetMapping("/rounds/new")
    public String showNewRoundNoteForm(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Showing new round note form for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        RoundNoteDTO roundDTO = new RoundNoteDTO();
        roundDTO.setPatientId(patientId);
        roundDTO.setDoctorId(doctorId);
        
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("roundDTO", roundDTO);
        
        return "doctor/round-note-form";
    }

    @PostMapping("/rounds")
    public String createRoundNote(
            @Valid @ModelAttribute("roundDTO") RoundNoteDTO roundDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        //log.info("Creating round note for patient ID: {} by doctor ID: {}", 
                //roundDTO.getPatientId(), roundDTO.getDoctorId());
        
        if (result.hasErrors()) {
            Optional<Patient> patientOpt = patientService.getPatientById(roundDTO.getPatientId());
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(roundDTO.getDoctorId());
            
            if (patientOpt.isPresent() && doctorOpt.isPresent()) {
                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("doctor", doctorOpt.get());
            }
            
            return "doctor/round-note-form";
        }
        
        RoundNote note = inpatientCareService.createRoundNoteFromDTO(roundDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Round note created successfully");
        
        return "redirect:/doctor/inpatient-care/patient/" + roundDTO.getPatientId() + 
                "?doctorId=" + roundDTO.getDoctorId();
    }

    @GetMapping("/discharge/new")
    public String showNewDischargeSummaryForm(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Showing new discharge summary form for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        DischargeSummaryDTO summaryDTO = new DischargeSummaryDTO();
        summaryDTO.setPatientId(patientId);
        summaryDTO.setDoctorId(doctorId);
        
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("summaryDTO", summaryDTO);
        model.addAttribute("diagnoses", inpatientCareService.getDiagnosesByPatientId(patientId));
        model.addAttribute("currentAdmission", inpatientCareService.getCurrentAdmissionForPatient(patientId));
        
        return "doctor/discharge-summary-form";
    }

    @PostMapping("/discharge")
    public String createDischargeSummary(
            @Valid @ModelAttribute("summaryDTO") DischargeSummaryDTO summaryDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        //log.info("Creating discharge summary for patient ID: {} by doctor ID: {}", 
                //summaryDTO.getPatientId(), summaryDTO.getDoctorId());
        
        if (result.hasErrors()) {
            Optional<Patient> patientOpt = patientService.getPatientById(summaryDTO.getPatientId());
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(summaryDTO.getDoctorId());
            
            if (patientOpt.isPresent() && doctorOpt.isPresent()) {
                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("doctor", doctorOpt.get());
                model.addAttribute("diagnoses", inpatientCareService.getDiagnosesByPatientId(summaryDTO.getPatientId()));
                model.addAttribute("currentAdmission", inpatientCareService.getCurrentAdmissionForPatient(summaryDTO.getPatientId()));
            }
            
            return "doctor/discharge-summary-form";
        }
        
        DischargeSummary summary = inpatientCareService.createDischargeSummaryFromDTO(summaryDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Discharge summary created successfully");
        
        return "redirect:/doctor/inpatient-care/patient/" + summaryDTO.getPatientId() + 
                "?doctorId=" + summaryDTO.getDoctorId();
    }

    @GetMapping("/notes/{id}")
    public String viewInpatientNote(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing inpatient note ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<InpatientNote> noteOpt = inpatientCareService.getInpatientNoteById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (noteOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Inpatient note ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        InpatientNote note = noteOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("note", note);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", note.getPatient());
        
        return "doctor/inpatient-note-view";
    }

    @GetMapping("/rounds/{id}")
    public String viewRoundNote(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing round note ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<RoundNote> noteOpt = inpatientCareService.getRoundNoteById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (noteOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Round note ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        RoundNote note = noteOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("note", note);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", note.getPatient());
        
        return "doctor/round-note-view";
    }

    @GetMapping("/discharge/{id}")
    public String viewDischargeSummary(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing discharge summary ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<DischargeSummary> summaryOpt = inpatientCareService.getDischargeSummaryById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!summaryOpt.isPresent() || !doctorOpt.isPresent()) {
            //log.warn("Discharge summary ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        DischargeSummary summary = summaryOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("summary", summary);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", summary.getPatient());
        
        return "doctor/discharge-summary-view";
    }

    @GetMapping("/discharge/{id}/print")
    public String printDischargeSummary(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Printing discharge summary ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<DischargeSummary> summaryOpt = inpatientCareService.getDischargeSummaryById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (summaryOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Discharge summary ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        DischargeSummary summary = summaryOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("summary", summary);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", summary.getPatient());
        
        return "doctor/discharge-summary-print";
    }


}