package com.ingestion.nursing.service;

import com.ingestion.common.model.inpatient.VitalSign;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VitalSignService {
    
    VitalSign saveVitalSign(VitalSign vitalSign);
    
    Optional<VitalSign> findById(Long id);
    
    List<VitalSign> findByPatient(Patient patient);
    
    Page<VitalSign> findByPatient(Patient patient, Pageable pageable);
    
    List<VitalSign> findByPatientAndDateRange(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    
    List<VitalSign> findAbnormalVitalsByPatient(Patient patient);
    
    VitalSign findLatestByPatient(Patient patient);
    
    List<VitalSign> findRecentAbnormalVitals(int hoursBack);
    
    VitalSign recordVitalSigns(Patient patient, Double temperature, String temperatureUnit,
                             Integer pulseRate, Integer respiratoryRate, Integer bloodPressureSystolic,
                             Integer bloodPressureDiastolic, Integer oxygenSaturation, Integer painScore,
                             Double height, Double weight, Integer bloodGlucose, String notes, User recordedBy);
    
    void deleteVitalSign(Long id);
}