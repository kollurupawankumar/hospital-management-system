package com.ingestion.opd.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Appointment;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OpdVisitService {

    OpdVisit saveVisit(OpdVisit opdVisit);
    
    Optional<OpdVisit> findById(Long id);
    
    Optional<OpdVisit> findByTokenNumber(String tokenNumber);
    
    List<OpdVisit> findAll();
    
    Page<OpdVisit> findAll(Pageable pageable);
    
    List<OpdVisit> findByPatient(Patient patient);
    
    List<OpdVisit> findByDoctor(Doctor doctor);
    
    List<OpdVisit> findByVisitDate(LocalDate visitDate);
    
    List<OpdVisit> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdVisit> findByStatus(OpdVisit.VisitStatus status);
    
    List<OpdVisit> findTodayVisitsByDoctor(Doctor doctor);
    
    Page<OpdVisit> findVisitsByDateRangeAndStatus(
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<OpdVisit.VisitStatus> statuses,
            Pageable pageable);
    
    Page<OpdVisit> findVisitsByDoctorAndDateRangeAndStatus(
            Doctor doctor,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<OpdVisit.VisitStatus> statuses,
            Pageable pageable);
    
    Long countVisitsByDoctorAndDate(Doctor doctor, LocalDate date);
    
    Page<OpdVisit> findPatientVisitHistory(Patient patient, Pageable pageable);
    
    OpdVisit createVisitFromAppointment(Appointment appointment);
    
    OpdVisit createWalkInVisit(Patient patient, Doctor doctor, String chiefComplaint);
    
    OpdVisit checkInVisit(Long visitId);
    
    OpdVisit startConsultation(Long visitId);
    
    OpdVisit completeConsultation(Long visitId, String diagnosis, String treatmentPlan, String followUpInstructions, LocalDateTime followUpDate);
    
    OpdVisit cancelVisit(Long visitId);
    
    OpdVisit markNoShow(Long visitId);
    
    void deleteVisit(Long id);
}