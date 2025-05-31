package com.ingestion.doctor.controller;

import com.ingestion.patient.model.Appointment;

import com.ingestion.patient.service.AppointmentService;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.patient.service.PatientService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//import java.util.//logging.//logger;

@Controller
@RequestMapping("/doctor")
public class DoctorDashboardController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private  PatientService patientService;

    
    @GetMapping("/dashboard/{doctorId}")
    public String getDoctorDashboard(@PathVariable Long doctorId, Model model) {
        ////log.info("Accessing dashboard for doctor ID: "+ doctorId);
        
        return doctorService.getDoctorById(doctorId)
            .map(doctor -> {
                model.addAttribute("doctor", doctor);
                
                // Get today's appointments
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDay = today.atStartOfDay();
                LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusSeconds(1);
                
                // Add all required data to the model
                model.addAttribute("todayAppointments", 
                    appointmentService.getAppointmentsByDoctorBetweenDates(doctorId, startOfDay, endOfDay));
                model.addAttribute("upcomingAppointments", 
                    appointmentService.getUpcomingAppointmentsByDoctorId(doctorId));
                model.addAttribute("telemedicineAppointments", 
                    appointmentService.getUpcomingTelemedicineAppointmentsByDoctorId(doctorId));
                model.addAttribute("recentPatients", 
                    patientService.getRecentPatientsByDoctorId(doctorId, 5));
                model.addAttribute("pendingTasks", 
                    doctorService.getPendingTasksForDoctor(doctorId));
                
                return "doctor/dashboard";
            })
            .orElseGet(() -> {
                ////log.warning("Doctor with ID: {} not found"+ doctorId);
                return "redirect:/error";
            });
    }

    @GetMapping("/appointments/{doctorId}")
    public String getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String view,
            @RequestParam(required = false) LocalDate date,
            Model model) {
        
        ////log.info("Accessing appointments for doctor ID: {} with view: {}");
        
        return doctorService.getDoctorById(doctorId)
            .map(doctor -> {
                model.addAttribute("doctor", doctor);
                
                // Use default date if not provided
                LocalDate selectedDate = Optional.ofNullable(date).orElse(LocalDate.now());
                model.addAttribute("selectedDate", selectedDate);
                
                // Determine view type and fetch appropriate appointments
                String viewType = Optional.ofNullable(view).orElse("day");
                
                switch (viewType) {
                    case "week":
                        handleWeekView(doctorId, selectedDate, model);
                        break;
                    case "month":
                        handleMonthView(doctorId, selectedDate, model);
                        break;
                    default: // day view
                        handleDayView(doctorId, selectedDate, model);
                        break;
                }
                
                return "doctor/appointments";
            })
            .orElseGet(() -> {
                ////log.warn("Doctor with ID: {} not found", doctorId);
                return "redirect:/error";
            });
    }
    
    private void handleDayView(Long doctorId, LocalDate date, Model model) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusSeconds(1);
        
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorBetweenDates(
                doctorId, startOfDay, endOfDay);
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("view", "day");
    }
    
    private void handleWeekView(Long doctorId, LocalDate date, Model model) {
        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.plusDays(1).atStartOfDay().minusSeconds(1);
        
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorBetweenDates(
                doctorId, startDateTime, endDateTime);
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("startOfWeek", startOfWeek);
        model.addAttribute("endOfWeek", endOfWeek);
        model.addAttribute("view", "week");
    }
    
    private void handleMonthView(Long doctorId, LocalDate date, Model model) {
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.plusDays(1).atStartOfDay().minusSeconds(1);
        
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorBetweenDates(
                doctorId, startDateTime, endDateTime);
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("startOfMonth", startOfMonth);
        model.addAttribute("endOfMonth", endOfMonth);
        model.addAttribute("view", "month");
    }

    @GetMapping("/patients/{doctorId}")
    public String getDoctorPatients(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String searchTerm,
            Model model) {
        
        //log.info("Accessing patients for doctor ID: {} with search term: {}", doctorId, searchTerm);
        
        return doctorService.getDoctorById(doctorId)
            .map(doctor -> {
                model.addAttribute("doctor", doctor);
                
                // Determine if we need to search or just get all patients
                boolean hasSearchTerm = Optional.ofNullable(searchTerm)
                    .filter(term -> !term.trim().isEmpty())
                    .isPresent();
                
                if (hasSearchTerm) {
                    model.addAttribute("searchTerm", searchTerm);
                    model.addAttribute("patients", patientService.searchPatientsByDoctorId(doctorId, searchTerm));
                } else {
                    model.addAttribute("patients", patientService.getPatientsByDoctorId(doctorId));
                }
                
                return "doctor/patients";
            })
            .orElseGet(() -> {
                //log.warn("Doctor with ID: {} not found", doctorId);
                return "redirect:/error";
            });
    }

    @GetMapping("/telemedicine/{doctorId}")
    public String getTelemedicineAppointments(@PathVariable Long doctorId, Model model) {
        //log.info("Accessing telemedicine appointments for doctor ID: {}", doctorId);
        
        return doctorService.getDoctorById(doctorId)
            .map(doctor -> {
                model.addAttribute("doctor", doctor);
                model.addAttribute("upcomingTelemedicine", 
                    appointmentService.getUpcomingTelemedicineAppointmentsByDoctorId(doctorId));
                
                return "doctor/telemedicine";
            })
            .orElseGet(() -> {
                //log.warn("Doctor with ID: {} not found", doctorId);
                return "redirect:/error";
            });
    }

    @GetMapping("/duty-roster/{doctorId}")
    public String getDutyRoster(
            @PathVariable Long doctorId,
            @RequestParam(required = false) LocalDate month,
            Model model) {
        
        //log.info("Accessing duty roster for doctor ID: {}", doctorId);
        
        return doctorService.getDoctorById(doctorId)
            .map(doctor -> {
                model.addAttribute("doctor", doctor);
                
                // Use current month if not specified
                LocalDate selectedMonth = Optional.ofNullable(month)
                    .orElse(LocalDate.now().withDayOfMonth(1));
                
                model.addAttribute("selectedMonth", selectedMonth);
                model.addAttribute("dutyRoster", doctorService.getDutyRosterForMonth(doctorId, selectedMonth));
                
                return "doctor/duty-roster";
            })
            .orElseGet(() -> {
                //log.warn("Doctor with ID: {} not found", doctorId);
                return "redirect:/error";
            });
    }
}