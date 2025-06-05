package com.ingestion.patient.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.patient.model.Appointment;
import com.ingestion.patient.model.Appointment.AppointmentStatus;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.repository.AppointmentRepository;
import com.ingestion.doctor.repository.DoctorRepository;
import com.ingestion.patient.repository.PatientRepository;
import com.ingestion.common.service.NotificationService;
import com.ingestion.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;
    
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        log.debug("Fetching all appointments");
        return appointmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Appointment getAppointmentById(Long id) {
        log.debug("Fetching appointment with ID: {}", id);
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + id));
    }

    @Transactional
    public Appointment saveAppointment(Appointment appointment) {
        log.debug("Saving appointment: {}", appointment);
        
        boolean isNew = appointment.getId() == null;
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        if (isNew) {
            notificationService.sendAppointmentConfirmation(savedAppointment);
        }
        
        return savedAppointment;
    }

    @Transactional
    public void deleteAppointment(Long id) {
        log.debug("Deleting appointment with ID: {}", id);
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointment.setCancellationReason("Deleted by user");
            appointmentRepository.save(appointment);
            
            notificationService.sendAppointmentCancellation(appointment);
        } else {
            log.warn("Appointment with ID: {} not found for deletion", id);
        }
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        log.debug("Fetching appointments for patient ID: {}", patientId);
        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        log.debug("Fetching appointments for doctor ID: {}", doctorId);
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctor(User doctor) {
        log.debug("Fetching appointments for doctor: {}", doctor.getUsername());
        // For now, return all appointments since we don't have a direct doctor-user relationship
        // In a real system, you'd have a proper mapping between User and Doctor entities
        return appointmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointmentsByPatientId(Long patientId) {
        log.debug("Fetching upcoming appointments for patient ID: {}", patientId);
        return appointmentRepository.findUpcomingAppointmentsByPatientId(patientId, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Appointment> getPastAppointmentsByPatientId(Long patientId) {
        log.debug("Fetching past appointments for patient ID: {}", patientId);
        return appointmentRepository.findPastAppointmentsByPatientId(patientId, LocalDateTime.now());
    }

    @Transactional
    public Appointment scheduleAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate, String purpose, boolean isTelemedicine) {
        log.debug("Scheduling appointment for patient ID: {} with doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        
        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();
            
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDate(appointmentDate);
            appointment.setEndTime(appointmentDate.plusMinutes(30)); // Default 30 min appointment
            appointment.setPurpose(purpose);
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointment.setIsTelemedicine(isTelemedicine);
            
            if (isTelemedicine) {
                // Generate a unique meeting link (simplified for example)
                String meetingId = "meet-" + System.currentTimeMillis();
                appointment.setMeetingLink("https://hospital-telemedicine.com/" + meetingId);
            }
            
            Appointment savedAppointment = appointmentRepository.save(appointment);
            notificationService.sendAppointmentConfirmation(savedAppointment);
            
            return savedAppointment;
        } else {
            log.error("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            throw new IllegalArgumentException("Patient or Doctor not found");
        }
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus status, String reason) {
        log.debug("Updating appointment ID: {} status to: {}", appointmentId, status);
        
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            AppointmentStatus oldStatus = appointment.getStatus();
            appointment.setStatus(status);
            
            if (status == AppointmentStatus.CANCELLED) {
                appointment.setCancellationReason(reason);
                notificationService.sendAppointmentCancellation(appointment);
            } else if (oldStatus != status) {
                notificationService.sendAppointmentStatusUpdate(appointment, oldStatus);
            }
            
            return appointmentRepository.save(appointment);
        } else {
            log.error("Appointment ID: {} not found", appointmentId);
            throw new IllegalArgumentException("Appointment not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingTelemedicineAppointmentsByPatientId(Long patientId) {
        log.debug("Fetching upcoming telemedicine appointments for patient ID: {}", patientId);
        return appointmentRepository.findUpcomingTelemedicineAppointmentsByPatientId(patientId, LocalDateTime.now());
    }

    @Transactional
    public void sendAppointmentReminders() {
        log.debug("Sending appointment reminders");
        
        // Get appointments in the next 24 hours that haven't had reminders sent
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        
        List<Appointment> upcomingAppointments = appointmentRepository.findAppointmentsForReminders(now, tomorrow);
        
        for (Appointment appointment : upcomingAppointments) {
            notificationService.sendAppointmentReminder(appointment);
            appointment.setReminderSent(true);
            appointmentRepository.save(appointment);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorBetweenDates(Long doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching appointments for doctor ID: {} between {} and {}", doctorId, startDate, endDate);
        return appointmentRepository.findAppointmentsByDoctorBetweenDates(doctorId, startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointmentsByDoctorId(Long doctorId) {
        log.debug("Fetching upcoming appointments for doctor ID: {}", doctorId);
        return appointmentRepository.findUpcomingAppointmentsByDoctorId(doctorId, LocalDateTime.now());
    }
    
    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingTelemedicineAppointmentsByDoctorId(Long doctorId) {
        log.debug("Fetching upcoming telemedicine appointments for doctor ID: {}", doctorId);
        return appointmentRepository.findUpcomingTelemedicineAppointmentsByDoctorId(doctorId, LocalDateTime.now());
    }
}