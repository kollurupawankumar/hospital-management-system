package com.ingestion.opd.repository;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdLabOrder;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpdLabOrderRepository extends JpaRepository<OpdLabOrder, Long> {

    Optional<OpdLabOrder> findByOrderNumber(String orderNumber);
    
    List<OpdLabOrder> findByOpdVisit(OpdVisit opdVisit);
    
    List<OpdLabOrder> findByDoctor(Doctor doctor);
    
    List<OpdLabOrder> findByStatus(OpdLabOrder.LabOrderStatus status);
    
    List<OpdLabOrder> findByIsUrgent(Boolean isUrgent);
    
    @Query("SELECT l FROM OpdLabOrder l WHERE l.opdVisit.patient = :patient ORDER BY l.orderDate DESC")
    List<OpdLabOrder> findByPatient(@Param("patient") Patient patient);
    
    List<OpdLabOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT l FROM OpdLabOrder l WHERE l.status = :status AND l.scheduledDate BETWEEN :startDate AND :endDate")
    List<OpdLabOrder> findByStatusAndScheduledDateBetween(
            @Param("status") OpdLabOrder.LabOrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT l FROM OpdLabOrder l WHERE l.opdVisit.patient = :patient AND l.orderDate BETWEEN :startDate AND :endDate")
    List<OpdLabOrder> findByPatientAndDateRange(
            @Param("patient") Patient patient,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT l FROM OpdLabOrder l WHERE l.status IN :statuses ORDER BY l.orderDate")
    List<OpdLabOrder> findByStatuses(@Param("statuses") List<OpdLabOrder.LabOrderStatus> statuses);
}