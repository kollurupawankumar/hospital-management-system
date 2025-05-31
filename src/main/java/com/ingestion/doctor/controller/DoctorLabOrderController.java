package com.ingestion.doctor.controller;

import com.ingestion.doctor.dto.LabOrderDTO;
import com.ingestion.doctor.dto.LabTestDTO;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.doctor.service.DoctorLabOrderServiceAdapter;
import com.ingestion.patient.model.LabReport;
import com.ingestion.patient.model.Patient;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.patient.service.LabReportService;
import com.ingestion.patient.service.PatientService;

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
@RequestMapping("/doctor/lab-orders")

public class DoctorLabOrderController {

    @Autowired
    private DoctorLabOrderServiceAdapter labOrderService;
    @Autowired
    private  PatientService patientService;
    @Autowired
    private  DoctorService doctorService;
    @Autowired
    private  LabReportService labReportService;

    @GetMapping("/new")
    public String showNewLabOrderForm(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Showing new lab order form for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            //log.warn("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        LabOrderDTO labOrderDTO = new LabOrderDTO();
        labOrderDTO.setPatientId(patientId);
        labOrderDTO.setDoctorId(doctorId);
        labOrderDTO.setPatientName(patient.getFirstName() + " " + patient.getLastName());
        labOrderDTO.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
        
        // Add one empty test by default
        List<LabTestDTO> tests = new ArrayList<>();
        tests.add(new LabTestDTO());
        labOrderDTO.setLabTests(tests);
        
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("labOrderDTO", labOrderDTO);
        model.addAttribute("labTestTypes", labOrderService.getAllLabTestTypes());
        
        return "doctor/lab-order-form";
    }

    @PostMapping
    public String createLabOrder(
            @Valid @ModelAttribute("labOrderDTO") LabOrderDTO labOrderDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        //log.info("Creating lab order for patient ID: {} by doctor ID: {}", 
                //labOrderDTO.getPatientId(), labOrderDTO.getDoctorId());
        
        if (result.hasErrors()) {
            Optional<Patient> patientOpt = patientService.getPatientById(labOrderDTO.getPatientId());
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(labOrderDTO.getDoctorId());
            
            if (patientOpt.isPresent() && doctorOpt.isPresent()) {
                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("doctor", doctorOpt.get());
                model.addAttribute("labTestTypes", labOrderService.getAllLabTestTypes());
            }
            
            return "doctor/lab-order-form";
        }
        
        LabOrder labOrder = labOrderService.createLabOrderFromDTO(labOrderDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Lab order created successfully");
        
        return "redirect:/doctor/patients/" + labOrderDTO.getPatientId() + 
                "/lab-reports?doctorId=" + labOrderDTO.getDoctorId();
    }

    @GetMapping("/{id}")
    public String getLabOrderDetails(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing lab order ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<LabOrder> labOrderOpt = labOrderService.getLabOrderById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!labOrderOpt.isPresent() || !doctorOpt.isPresent()) {
            //log.warn("Lab order ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        LabOrder labOrder = labOrderOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("labOrder", labOrder);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", labOrder.getPatient());
        
        return "doctor/lab-order-details";
    }

    @GetMapping("/{id}/print")
    public String printLabOrder(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Printing lab order ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<LabOrder> labOrderOpt = labOrderService.getLabOrderById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!labOrderOpt.isPresent() || !doctorOpt.isPresent()) {
            //log.warn("Lab order ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        LabOrder labOrder = labOrderOpt.get();
        Doctor doctor = doctorOpt.get();
        
        model.addAttribute("labOrder", labOrder);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", labOrder.getPatient());
        
        return "doctor/lab-order-print";
    }

    @GetMapping("/doctor/{doctorId}")
    public String getDoctorLabOrders(
            @PathVariable Long doctorId,
            Model model) {
        
        //log.info("Viewing all lab orders by doctor ID: {}", doctorId);
        
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!doctorOpt.isPresent()) {
            //log.warn("Doctor ID: {} not found", doctorId);
            return "redirect:/error";
        }
        
        Doctor doctor = doctorOpt.get();
        List<LabOrder> labOrders = labOrderService.getLabOrdersByDoctorId(doctorId);
        
        model.addAttribute("doctor", doctor);
        model.addAttribute("labOrders", labOrders);
        
        return "doctor/doctor-lab-orders";
    }

    @GetMapping("/reports/{id}")
    public String viewLabReport(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing lab report ID: {} by doctor ID: {}", id, doctorId);
        
        Optional<LabReport> reportOpt = labReportService.getLabReportById(id);
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!reportOpt.isPresent() || !doctorOpt.isPresent()) {
            //log.warn("Lab report ID: {} or Doctor ID: {} not found", id, doctorId);
            return "redirect:/doctor/dashboard/" + doctorId;
        }
        
        LabReport report = reportOpt.get();
        Doctor doctor = doctorOpt.get();
        
        // Mark as viewed by doctor
        labReportService.markLabReportAsViewedByDoctor(id);
        
        model.addAttribute("report", report);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", report.getPatient());
        
        return "doctor/lab-report-view";
    }

    @GetMapping("/pending-results")
    public String getPendingLabResults(
            @RequestParam Long doctorId,
            Model model) {
        
        //log.info("Viewing pending lab results for doctor ID: {}", doctorId);
        
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        
        if (!doctorOpt.isPresent()) {
            //log.warn("Doctor ID: {} not found", doctorId);
            return "redirect:/error";
        }
        
        Doctor doctor = doctorOpt.get();
        List<LabOrder> pendingOrders = labOrderService.getPendingLabOrdersByDoctorId(doctorId);
        
        model.addAttribute("doctor", doctor);
        model.addAttribute("pendingOrders", pendingOrders);
        
        return "doctor/pending-lab-results";
    }
}