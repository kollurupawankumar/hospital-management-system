package com.ingestion.pharmacy.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Medicine;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.pharmacy.model.PrescriptionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrescriptionService {

    Prescription savePrescription(Prescription prescription);
    
    Optional<Prescription> findById(Long id);
    
    Optional<Prescription> findByPrescriptionNumber(String prescriptionNumber);
    
    List<Prescription> findAll();
    
    List<Prescription> findByPatient(Patient patient);
    
    List<Prescription> findByDoctor(Doctor doctor);
    
    List<Prescription> findByOpdVisit(OpdVisit opdVisit);
    
    List<Prescription> findByStatus(Prescription.PrescriptionStatus status);
    
    List<Prescription> findByIsDispensed(Boolean isDispensed);
    
    List<Prescription> findByIsBilled(Boolean isBilled);
    
    List<Prescription> findByIsPaid(Boolean isPaid);
    
    List<Prescription> findByPrescriptionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Prescription> findByPatientOrderByPrescriptionDateDesc(Long patientId);
    
    List<Prescription> findByDoctorOrderByPrescriptionDateDesc(Long doctorId);
    
    List<Prescription> findByStatusOrderByPrescriptionDateDesc(Prescription.PrescriptionStatus status);
    
    List<Prescription> findPendingPrescriptions();
    
    List<Prescription> findPendingPrescriptionsByPatient(Long patientId);
    
    Page<Prescription> findRecentPrescriptions(LocalDateTime startDate, Pageable pageable);
    
    Long countPendingPrescriptions();
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Prescription createPrescription(Patient patient, Doctor doctor, OpdVisit opdVisit, 
                                   String diagnosis, String notes, List<PrescriptionItem> items);
    
    Prescription addItemToPrescription(Long prescriptionId, Medicine medicine, String dosage, 
                                      String frequency, String duration, PrescriptionItem.AdministrationRoute route, 
                                      String instructions, Integer quantity);
    
    Prescription removeItemFromPrescription(Long prescriptionId, Long itemId);
    
    Prescription markAsDispensed(Long id, String dispensedBy);
    
    Prescription markAsBilled(Long id);
    
    Prescription markAsPaid(Long id);
    
    Prescription cancel(Long id);
    
    Prescription expire(Long id);
    
    PrescriptionItem dispenseItem(Long prescriptionId, Long itemId, Integer quantity, String batchNumber);
    
    void deletePrescription(Long id);
}