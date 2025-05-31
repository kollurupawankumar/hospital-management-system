package com.ingestion.laboratory.repository;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.LabResult;
import com.ingestion.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {

    Optional<LabResult> findByResultNumber(String resultNumber);
    
    Optional<LabResult> findByLabOrder(LabOrder labOrder);
    
    List<LabResult> findByStatus(LabResult.ResultStatus status);
    
    List<LabResult> findByPerformedBy(User performedBy);
    
    List<LabResult> findByVerifiedBy(User verifiedBy);
    
    List<LabResult> findByApprovedBy(User approvedBy);
    
    List<LabResult> findByIsAbnormal(Boolean isAbnormal);
    
    List<LabResult> findByIsCritical(Boolean isCritical);
    
    List<LabResult> findByResultDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabResult> findByVerifiedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabResult> findByApprovedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT r FROM LabResult r WHERE r.status = :status ORDER BY r.resultDate")
    List<LabResult> findByStatusOrderByResultDate(@Param("status") LabResult.ResultStatus status);
    
    @Query("SELECT r FROM LabResult r WHERE r.status IN :statuses ORDER BY r.resultDate")
    List<LabResult> findByStatusesOrderByResultDate(@Param("statuses") List<LabResult.ResultStatus> statuses);
    
    @Query("SELECT r FROM LabResult r WHERE r.isCritical = true AND r.criticalInformedDate IS NULL")
    List<LabResult> findCriticalResultsNotInformed();
    
    @Query("SELECT COUNT(r) FROM LabResult r WHERE r.status = :status")
    Long countByStatus(@Param("status") LabResult.ResultStatus status);
    
    @Query("SELECT COUNT(r) FROM LabResult r WHERE r.resultDate BETWEEN :startDate AND :endDate")
    Long countByResultDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}