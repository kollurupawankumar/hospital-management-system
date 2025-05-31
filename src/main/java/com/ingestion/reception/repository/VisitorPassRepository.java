package com.ingestion.reception.repository;

import com.ingestion.reception.model.VisitorPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorPassRepository extends JpaRepository<VisitorPass, Long> {
    
    Optional<VisitorPass> findByPassNumber(String passNumber);
    
    List<VisitorPass> findByPatientId(Long patientId);
    
    List<VisitorPass> findByVisitorNameContainingIgnoreCase(String visitorName);
    
    List<VisitorPass> findByVisitorPhoneContaining(String visitorPhone);
    
    @Query("SELECT v FROM VisitorPass v WHERE v.issueTime BETWEEN :startTime AND :endTime ORDER BY v.issueTime DESC")
    List<VisitorPass> findByIssueDateTimeBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT v FROM VisitorPass v WHERE v.passStatus = :status AND v.issueTime BETWEEN :startTime AND :endTime ORDER BY v.issueTime DESC")
    List<VisitorPass> findByStatusAndIssueDateTimeBetween(
            @Param("status") VisitorPass.PassStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT v FROM VisitorPass v WHERE v.passStatus IN ('ACTIVE', 'CHECKED_IN') AND v.patient.id = :patientId ORDER BY v.issueTime DESC")
    List<VisitorPass> findActiveVisitorsByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT v FROM VisitorPass v WHERE v.passStatus = 'ACTIVE' AND v.expiryTime < :currentTime")
    List<VisitorPass> findExpiredPasses(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(v) FROM VisitorPass v WHERE v.passStatus IN ('ACTIVE', 'CHECKED_IN') AND v.wardNumber = :wardNumber")
    long countActiveVisitorsByWard(@Param("wardNumber") String wardNumber);
    
    @Query("SELECT MAX(v.passNumber) FROM VisitorPass v WHERE v.passNumber LIKE CONCAT(:prefix, '%')")
    String findLastPassNumberWithPrefix(@Param("prefix") String prefix);
}