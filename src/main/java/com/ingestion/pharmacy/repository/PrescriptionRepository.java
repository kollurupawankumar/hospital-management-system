package com.ingestion.pharmacy.repository;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<Prescription> findByPrescriptionNumber(String prescriptionNumber);
    
    List<Prescription> findByPatient(Patient patient);
    
    List<Prescription> findByDoctor(Doctor doctor);
    
    List<Prescription> findByOpdVisit(OpdVisit opdVisit);
    
    List<Prescription> findByStatus(Prescription.PrescriptionStatus status);
    
    List<Prescription> findByIsDispensed(Boolean isDispensed);
    
    List<Prescription> findByIsBilled(Boolean isBilled);
    
    List<Prescription> findByIsPaid(Boolean isPaid);
    
    List<Prescription> findByPrescriptionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByPatientOrderByPrescriptionDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT p FROM Prescription p WHERE p.doctor.id = :doctorId ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByDoctorOrderByPrescriptionDateDesc(@Param("doctorId") Long doctorId);
    
    @Query("SELECT p FROM Prescription p WHERE p.status = :status ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByStatusOrderByPrescriptionDateDesc(@Param("status") Prescription.PrescriptionStatus status);
    
    @Query("SELECT p FROM Prescription p WHERE p.isDispensed = false AND p.status = 'ACTIVE' ORDER BY p.prescriptionDate DESC")
    List<Prescription> findPendingPrescriptions();
    
    @Query("SELECT p FROM Prescription p WHERE p.isDispensed = false AND p.status = 'ACTIVE' AND p.patient.id = :patientId ORDER BY p.prescriptionDate DESC")
    List<Prescription> findPendingPrescriptionsByPatient(@Param("patientId") Long patientId);
    
    @Query("SELECT p FROM Prescription p WHERE p.prescriptionDate >= :startDate ORDER BY p.prescriptionDate DESC")
    Page<Prescription> findRecentPrescriptions(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.isDispensed = false AND p.status = 'ACTIVE'")
    Long countPendingPrescriptions();
    
    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.prescriptionDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Additional methods needed by PrescriptionService
    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByPatientIdOrderByDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT p FROM Prescription p WHERE p.doctor.id = :doctorId ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByDoctorIdOrderByDateDesc(@Param("doctorId") Long doctorId);
    
    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId AND p.status = 'ACTIVE' AND p.isDispensed = false ORDER BY p.prescriptionDate DESC")
    List<Prescription> findActiveByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId AND DATE(p.prescriptionDate) BETWEEN :startDate AND :endDate ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByPatientIdAndDateRange(@Param("patientId") Long patientId, 
                                                  @Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
    
    @Query("SELECT DISTINCT p FROM Prescription p JOIN p.prescriptionItems pi JOIN pi.medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :medicationName, '%'))")
    List<Prescription> findByMedicationName(@Param("medicationName") String medicationName);
}