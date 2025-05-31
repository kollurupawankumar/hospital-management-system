package com.ingestion.nursing.service;

import com.ingestion.nursing.model.MedicationAdministration;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Medication;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.pharmacy.model.PrescriptionItem;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicationAdministrationService {
    
    MedicationAdministration saveMedicationAdministration(MedicationAdministration medicationAdministration);
    
    Optional<MedicationAdministration> findById(Long id);
    
    List<MedicationAdministration> findByPatient(Patient patient);
    
    Page<MedicationAdministration> findByPatient(Patient patient, Pageable pageable);
    
    List<MedicationAdministration> findByPatientAndStatus(Patient patient, MedicationAdministration.AdministrationStatus status);
    
    List<MedicationAdministration> findByPrescription(Prescription prescription);
    
    List<MedicationAdministration> findByPrescriptionItem(PrescriptionItem prescriptionItem);
    
    List<MedicationAdministration> findByMedication(Medication medication);
    
    List<MedicationAdministration> findByAdministeredBy(User nurse);
    
    List<MedicationAdministration> findByStatusAndTimeRange(
            MedicationAdministration.AdministrationStatus status, LocalDateTime startTime, LocalDateTime endTime);
    
    List<MedicationAdministration> findDueMedications();
    
    List<MedicationAdministration> findUpcomingMedications(int hoursAhead);
    
    List<MedicationAdministration> findOverdueMedications();
    
    List<MedicationAdministration> findStatMedications();
    
    MedicationAdministration scheduleMedication(Patient patient, Prescription prescription, PrescriptionItem prescriptionItem,
                                             Medication medication, String dosage, String route, LocalDateTime scheduledTime,
                                             Boolean isPrn, Boolean isStat, Boolean witnessRequired);
    
    MedicationAdministration administerMedication(Long medicationAdministrationId, User nurse, LocalDateTime administeredTime,
                                               String notes, User witness);
    
    MedicationAdministration markAsMissed(Long medicationAdministrationId, String reason);
    
    MedicationAdministration markAsRefused(Long medicationAdministrationId, String reason);
    
    MedicationAdministration holdMedication(Long medicationAdministrationId, String reason);
    
    MedicationAdministration cancelMedication(Long medicationAdministrationId, String reason);
    
    void deleteMedicationAdministration(Long id);
}