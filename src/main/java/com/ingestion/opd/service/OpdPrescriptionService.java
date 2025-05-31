package com.ingestion.opd.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdMedication;
import com.ingestion.opd.model.OpdPrescription;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OpdPrescriptionService {

    OpdPrescription savePrescription(OpdPrescription prescription);
    
    Optional<OpdPrescription> findById(Long id);
    
    Optional<OpdPrescription> findByPrescriptionNumber(String prescriptionNumber);
    
    List<OpdPrescription> findAll();
    
    List<OpdPrescription> findByOpdVisit(OpdVisit opdVisit);
    
    List<OpdPrescription> findByDoctor(Doctor doctor);
    
    List<OpdPrescription> findByPatient(Patient patient);
    
    List<OpdPrescription> findByDate(LocalDate date);
    
    List<OpdPrescription> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdPrescription> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdPrescription> findByPatientAndDateRange(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    
    OpdPrescription createPrescription(OpdVisit opdVisit, Doctor doctor, String diagnosis, String notes, List<OpdMedication> medications);
    
    OpdPrescription addMedicationToPrescription(Long prescriptionId, OpdMedication medication);
    
    OpdPrescription removeMedicationFromPrescription(Long prescriptionId, Long medicationId);
    
    void deletePrescription(Long id);
}