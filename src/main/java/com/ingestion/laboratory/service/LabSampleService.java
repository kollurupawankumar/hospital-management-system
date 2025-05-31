package com.ingestion.laboratory.service;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.LabSample;
import com.ingestion.security.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LabSampleService {

    LabSample saveSample(LabSample labSample);
    
    Optional<LabSample> findById(Long id);
    
    Optional<LabSample> findBySampleId(String sampleId);
    
    Optional<LabSample> findByLabOrder(LabOrder labOrder);
    
    List<LabSample> findAll();
    
    List<LabSample> findBySampleType(String sampleType);
    
    List<LabSample> findByStatus(LabSample.SampleStatus status);
    
    List<LabSample> findByCollectedBy(User collectedBy);
    
    List<LabSample> findByReceivedBy(User receivedBy);
    
    List<LabSample> findByCollectionDate(LocalDate collectionDate);
    
    List<LabSample> findByCollectionDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabSample> findByReceivedDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabSample> findByStatusOrderByCollectionDate(LabSample.SampleStatus status);
    
    List<LabSample> findByStatusesOrderByCollectionDate(List<LabSample.SampleStatus> statuses);
    
    Long countByStatus(LabSample.SampleStatus status);
    
    Long countByCollectionDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    LabSample createSample(LabOrder labOrder, String sampleType, String containerType, String volume);
    
    LabSample collectSample(Long sampleId, User collectedBy, LocalDateTime collectionDate, String notes);
    
    LabSample receiveSample(Long sampleId, User receivedBy, LocalDateTime receivedDate, String storageLocation);
    
    LabSample markAsInProcess(Long sampleId);
    
    LabSample markAsProcessed(Long sampleId);
    
    LabSample markAsRejected(Long sampleId, String rejectionReason);
    
    void deleteSample(Long id);
}