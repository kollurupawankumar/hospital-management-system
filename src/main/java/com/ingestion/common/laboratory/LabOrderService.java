package com.ingestion.common.laboratory;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.laboratory.model.LabTest;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Consolidated service interface for lab order operations.
 * Combines functionality from both doctor and laboratory packages.
 */
public interface LabOrderService {
    
    // Basic CRUD operations
    List<LabOrder> findAll();
    
    Optional<LabOrder> findById(Long id);
    
    Optional<LabOrder> findByOrderNumber(String orderNumber);
    
    LabOrder save(LabOrder labOrder);
    
    void deleteById(Long id);
    
    // Creation methods
    LabOrder createOrder(
            Patient patient,
            Doctor doctor,
            OpdVisit opdVisit,
            LabOrder.OrderPriority priority,
            String clinicalNotes,
            String specialInstructions,
            List<LabTest> tests);
    
    // Test management
    LabOrderTest addTestToOrder(Long orderId, LabTest test);
    
    void removeTestFromOrder(Long orderId, Long testId);
    
    // Status management
    LabOrder updateOrderStatus(Long orderId, LabOrder.OrderStatus status);
    
    LabOrder markAsSampleCollected(Long orderId);
    
    LabOrder markAsInProcess(Long orderId);
    
    LabOrder markAsCompleted(Long orderId);
    
    LabOrder cancelOrder(Long orderId, Doctor cancelledBy, String reason);
    
    LabOrder markAsCancelled(Long orderId);
    
    LabOrder markAsRejected(Long orderId);
    
    void deleteOrder(Long orderId);
    
    // Billing operations
    LabOrder markAsBilled(Long orderId);
    
    LabOrder markAsPaid(Long orderId);
    
    // Search and filtering
    List<LabOrder> findByPatient(Patient patient);
    
    List<LabOrder> findByPatientId(Long patientId);
    
    List<LabOrder> findByDoctor(Doctor doctor);
    
    List<LabOrder> findByDoctorId(Long doctorId);
    
    List<LabOrder> findByOpdVisit(OpdVisit opdVisit);
    
    List<LabOrder> findByStatus(LabOrder.OrderStatus status);
    
    List<LabOrder> findByPriority(LabOrder.OrderPriority priority);
    
    List<LabOrder> findByOrderDate(LocalDate date);
    
    List<LabOrder> findByOrderDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabOrder> findByStatusesOrderByPriorityAndOrderDate(List<LabOrder.OrderStatus> statuses);
    
    Page<LabOrder> findByPatientPaginated(Patient patient, Pageable pageable);
    
    List<LabOrder> findByTestName(String testName);
    
    // Reporting and statistics
    Long countByStatus(LabOrder.OrderStatus status);
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}