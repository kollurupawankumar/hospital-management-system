package com.ingestion.common.laboratory;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
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

/**
 * Consolidated repository for LabOrder entities.
 * Combines functionality from both doctor and laboratory packages.
 */
@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    
    // Basic queries
    Optional<LabOrder> findByOrderNumber(String orderNumber);
    
    List<LabOrder> findByPatient(Patient patient);
    
    List<LabOrder> findByDoctor(Doctor doctor);
    
    List<LabOrder> findByOpdVisit(OpdVisit opdVisit);
    
    List<LabOrder> findByStatus(LabOrder.OrderStatus status);
    
    List<LabOrder> findByPriority(LabOrder.OrderPriority priority);
    
    List<LabOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabOrder> findByIsBilled(Boolean isBilled);
    
    List<LabOrder> findByIsPaid(Boolean isPaid);
    
    // Patient-specific queries
    List<LabOrder> findByPatientIdOrderByOrderDateDesc(Long patientId);
    
    @Query("SELECT o FROM LabOrder o WHERE o.patient.id = :patientId AND o.status = :status")
    List<LabOrder> findByPatientIdAndStatus(@Param("patientId") Long patientId, @Param("status") LabOrder.OrderStatus status);
    
    @Query("SELECT o FROM LabOrder o WHERE o.patient = :patient ORDER BY o.orderDate DESC")
    Page<LabOrder> findByPatientOrderByOrderDateDesc(@Param("patient") Patient patient, Pageable pageable);
    
    @Query("SELECT o FROM LabOrder o WHERE o.patient.id = :patientId AND o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<LabOrder> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    // Doctor-specific queries
    List<LabOrder> findByDoctorIdOrderByOrderDateDesc(Long doctorId);
    
    @Query("SELECT o FROM LabOrder o WHERE o.doctor.id = :doctorId AND o.status = :status")
    List<LabOrder> findByDoctorIdAndStatus(@Param("doctorId") Long doctorId, @Param("status") LabOrder.OrderStatus status);
    
    @Query("SELECT o FROM LabOrder o WHERE o.doctor = :doctor AND o.orderDate BETWEEN :startDate AND :endDate")
    List<LabOrder> findByDoctorAndDateRange(
            @Param("doctor") Doctor doctor,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM LabOrder o WHERE o.doctor.id = :doctorId AND o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<LabOrder> findByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    // Status and priority queries
    @Query("SELECT o FROM LabOrder o WHERE o.status = :status AND o.priority = :priority ORDER BY o.orderDate")
    List<LabOrder> findByStatusAndPriorityOrderByOrderDate(
            @Param("status") LabOrder.OrderStatus status,
            @Param("priority") LabOrder.OrderPriority priority);
    
    @Query("SELECT o FROM LabOrder o WHERE o.status IN :statuses ORDER BY " +
           "CASE o.priority " +
           "WHEN 'STAT' THEN 0 " +
           "WHEN 'URGENT' THEN 1 " +
           "ELSE 2 END, " +
           "o.orderDate")
    List<LabOrder> findByStatusesOrderByPriorityAndOrderDate(@Param("statuses") List<LabOrder.OrderStatus> statuses);
    
    // Test-related queries
    @Query("SELECT o FROM LabOrder o JOIN o.labTests t WHERE t.labTest.testName LIKE %:testName%")
    List<LabOrder> findByTestName(@Param("testName") String testName);
    
    @Query("SELECT o FROM LabOrder o JOIN o.labTests t WHERE o.patient.id = :patientId AND t.labTest.testName LIKE %:testName%")
    List<LabOrder> findByPatientIdAndTestName(
            @Param("patientId") Long patientId,
            @Param("testName") String testName);
    
    // Counting queries
    @Query("SELECT COUNT(o) FROM LabOrder o WHERE o.status = :status")
    Long countByStatus(@Param("status") LabOrder.OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM LabOrder o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}