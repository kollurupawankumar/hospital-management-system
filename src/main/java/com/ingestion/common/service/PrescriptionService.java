package com.ingestion.common.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Medicine;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.pharmacy.model.PrescriptionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrescriptionService {

    // Basic CRUD operations
    Prescription savePrescription(Prescription prescription);
    
    Optional<Prescription> findById(Long id);
    
    Optional<Prescription> findByPrescriptionNumber(String prescriptionNumber);
    
    List<Prescription> findAll();
    
    void deletePrescription(Long id);
    
    // Find by relationships
    List<Prescription> findByPatient(Patient patient);
    
    List<Prescription> findByPatientId(Long patientId);
    
    List<Prescription> findByDoctor(Doctor doctor);
    
    List<Prescription> findByDoctorId(Long doctorId);
    
    List<Prescription> findByOpdVisit(OpdVisit opdVisit);
    
    // Find by status and flags
    List<Prescription> findByStatus(Prescription.PrescriptionStatus status);
    
    List<Prescription> findByIsDispensed(Boolean isDispensed);
    
    List<Prescription> findByIsBilled(Boolean isBilled);
    
    List<Prescription> findByIsPaid(Boolean isPaid);
    
    // Find by date ranges
    List<Prescription> findByPrescriptionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Prescription> findByPatientIdAndDateRange(Long patientId, LocalDate startDate, LocalDate endDate);
    
    // Ordered queries
    List<Prescription> findByPatientOrderByPrescriptionDateDesc(Long patientId);
    
    List<Prescription> findByDoctorOrderByPrescriptionDateDesc(Long doctorId);
    
    List<Prescription> findByStatusOrderByPrescriptionDateDesc(Prescription.PrescriptionStatus status);
    
    List<Prescription> findByPatientIdOrderByDateDesc(Long patientId);
    
    List<Prescription> findByDoctorIdOrderByDateDesc(Long doctorId);
    
    // Active and pending prescriptions
    List<Prescription> findActivePrescriptionsByPatientId(Long patientId);
    
    List<Prescription> findPendingPrescriptions();
    
    List<Prescription> findPendingPrescriptionsByPatient(Long patientId);
    
    // Paginated queries
    Page<Prescription> findRecentPrescriptions(LocalDateTime startDate, Pageable pageable);
    
    // Count queries
    Long countPendingPrescriptions();
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Long countByStatus(Prescription.PrescriptionStatus status);
    
    Long countByPatientId(Long patientId);
    
    Long countByDoctorId(Long doctorId);
    
    // Search functionality
    List<Prescription> searchPrescriptionsByMedication(String medicationName);
    
    List<Prescription> searchPrescriptions(String searchTerm);
    
    // Creation methods
    Prescription createPrescription(Patient patient, Doctor doctor, OpdVisit opdVisit, 
                                   String diagnosis, String notes, List<PrescriptionItem> items);
    
    Prescription createPrescription(Long patientId, Long doctorId, String diagnosis, 
                                   LocalDate followUpDate, List<PrescriptionItem> items);
    
    Prescription createPrescriptionFromDTO(com.ingestion.doctor.dto.PrescriptionDTO prescriptionDTO);
    
    // Item management
    Prescription addItemToPrescription(Long prescriptionId, Medicine medicine, String dosage, 
                                      String frequency, String duration, PrescriptionItem.AdministrationRoute route, 
                                      String instructions, Integer quantity);
    
    Prescription removeItemFromPrescription(Long prescriptionId, Long itemId);
    
    // Status management
    Prescription markAsDispensed(Long id, String dispensedBy);
    
    Prescription markAsBilled(Long id);
    
    Prescription markAsPaid(Long id);
    
    Prescription cancel(Long id);
    
    Prescription expire(Long id);
    
    Prescription deactivatePrescription(Long prescriptionId);
    
    Prescription activatePrescription(Long prescriptionId);
    
    // Item dispensing
    PrescriptionItem dispenseItem(Long prescriptionId, Long itemId, Integer quantity, String batchNumber);
    
    // Validation methods
    boolean isPrescriptionNumberUnique(String prescriptionNumber);
    
    boolean isPrescriptionNumberUnique(String prescriptionNumber, Long excludeId);
    
    boolean canDispensePrescription(Long prescriptionId);
    
    boolean canCancelPrescription(Long prescriptionId);
    
    boolean canEditPrescription(Long prescriptionId);
    
    // Business logic methods
    void sendPrescriptionNotification(Prescription prescription);
    
    void checkPrescriptionExpiry();
    
    List<Prescription> findExpiringPrescriptions(int daysFromNow);
    
    List<Prescription> findExpiredPrescriptions();
}