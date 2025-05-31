package com.ingestion.laboratory.repository;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.LabSample;
import com.ingestion.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabSampleRepository extends JpaRepository<LabSample, Long> {

    Optional<LabSample> findBySampleId(String sampleId);
    
    Optional<LabSample> findByLabOrder(LabOrder labOrder);
    
    List<LabSample> findBySampleType(String sampleType);
    
    List<LabSample> findByStatus(LabSample.SampleStatus status);
    
    List<LabSample> findByCollectedBy(User collectedBy);
    
    List<LabSample> findByReceivedBy(User receivedBy);
    
    List<LabSample> findByCollectionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabSample> findByReceivedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT s FROM LabSample s WHERE s.status = :status ORDER BY s.collectionDate")
    List<LabSample> findByStatusOrderByCollectionDate(@Param("status") LabSample.SampleStatus status);
    
    @Query("SELECT s FROM LabSample s WHERE s.status IN :statuses ORDER BY s.collectionDate")
    List<LabSample> findByStatusesOrderByCollectionDate(@Param("statuses") List<LabSample.SampleStatus> statuses);
    
    @Query("SELECT COUNT(s) FROM LabSample s WHERE s.status = :status")
    Long countByStatus(@Param("status") LabSample.SampleStatus status);
    
    @Query("SELECT COUNT(s) FROM LabSample s WHERE s.collectionDate BETWEEN :startDate AND :endDate")
    Long countByCollectionDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}