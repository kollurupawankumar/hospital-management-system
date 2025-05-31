package com.ingestion.nursing.service;

import com.ingestion.common.model.inpatient.Department;
import com.ingestion.nursing.model.ShiftHandover;
import com.ingestion.nursing.model.ShiftHandoverPatient;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftHandoverService {
    
    ShiftHandover saveShiftHandover(ShiftHandover shiftHandover);
    
    Optional<ShiftHandover> findById(Long id);
    
    List<ShiftHandover> findByOutgoingNurse(User nurse);
    
    List<ShiftHandover> findByIncomingNurse(User nurse);
    
    Page<ShiftHandover> findByDepartment(Department department, Pageable pageable);
    
    List<ShiftHandover> findByDepartmentAndDateRange(Department department, LocalDateTime startDate, LocalDateTime endDate);
    
    List<ShiftHandover> findByShiftTypeAndDateRange(ShiftHandover.ShiftType shiftType, LocalDateTime startDate, LocalDateTime endDate);
    
    List<ShiftHandover> findUnacknowledgedHandoversByNurse(User nurse);
    
    List<ShiftHandover> findTodayHandovers();
    
    List<ShiftHandover> findUnacknowledgedHandoversOlderThan(int hours);
    
    ShiftHandover createShiftHandover(LocalDateTime handoverDate, ShiftHandover.ShiftType shiftType, 
                                   Department department, User outgoingNurse, User incomingNurse, 
                                   String generalNotes, String pendingTasks, String criticalPatients, 
                                   String medicationIssues, String equipmentIssues, String staffingIssues);
    
    ShiftHandoverPatient addPatientToHandover(Long handoverId, Patient patient, String currentStatus, 
                                           String carePlanUpdates, String medicationUpdates, 
                                           String vitalSignsUpdates, String pendingTasks, 
                                           String specialInstructions, Boolean isCritical);
    
    ShiftHandover acknowledgeHandover(Long handoverId);
    
    ShiftHandoverPatient findLatestHandoverForPatient(Patient patient);
    
    List<ShiftHandoverPatient> findRecentCriticalPatients(int hoursBack);
    
    void deleteShiftHandover(Long id);
    
    void removePatientFromHandover(Long handoverPatientId);
}