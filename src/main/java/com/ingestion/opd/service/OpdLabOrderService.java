package com.ingestion.opd.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdLabOrder;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OpdLabOrderService {

    OpdLabOrder saveLabOrder(OpdLabOrder labOrder);
    
    Optional<OpdLabOrder> findById(Long id);
    
    Optional<OpdLabOrder> findByOrderNumber(String orderNumber);
    
    List<OpdLabOrder> findAll();
    
    List<OpdLabOrder> findByOpdVisit(OpdVisit opdVisit);
    
    List<OpdLabOrder> findByDoctor(Doctor doctor);
    
    List<OpdLabOrder> findByPatient(Patient patient);
    
    List<OpdLabOrder> findByStatus(OpdLabOrder.LabOrderStatus status);
    
    List<OpdLabOrder> findByIsUrgent(Boolean isUrgent);
    
    List<OpdLabOrder> findByOrderDate(LocalDate orderDate);
    
    List<OpdLabOrder> findByOrderDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdLabOrder> findByStatusAndScheduledDateRange(OpdLabOrder.LabOrderStatus status, LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdLabOrder> findByPatientAndDateRange(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdLabOrder> findByStatuses(List<OpdLabOrder.LabOrderStatus> statuses);
    
    OpdLabOrder createLabOrder(OpdVisit opdVisit, Doctor doctor, String testName, String testCategory, String instructions, Boolean isUrgent);
    
    OpdLabOrder scheduleLabOrder(Long labOrderId, LocalDateTime scheduledDate);
    
    OpdLabOrder startProcessingLabOrder(Long labOrderId);
    
    OpdLabOrder completeLabOrder(Long labOrderId, String result, String resultNotes);
    
    OpdLabOrder cancelLabOrder(Long labOrderId);
    
    void deleteLabOrder(Long id);
}