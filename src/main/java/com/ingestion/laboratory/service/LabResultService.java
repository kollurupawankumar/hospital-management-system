package com.ingestion.laboratory.service;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.*;
import com.ingestion.security.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LabResultService {

    LabResult saveResult(LabResult labResult);
    
    Optional<LabResult> findById(Long id);
    
    Optional<LabResult> findByResultNumber(String resultNumber);
    
    Optional<LabResult> findByLabOrder(LabOrder labOrder);
    
    List<LabResult> findAll();
    
    List<LabResult> findByStatus(LabResult.ResultStatus status);
    
    List<LabResult> findByPerformedBy(User performedBy);
    
    List<LabResult> findByVerifiedBy(User verifiedBy);
    
    List<LabResult> findByApprovedBy(User approvedBy);
    
    List<LabResult> findByIsAbnormal(Boolean isAbnormal);
    
    List<LabResult> findByIsCritical(Boolean isCritical);
    
    List<LabResult> findByResultDate(LocalDate resultDate);
    
    List<LabResult> findByResultDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabResult> findByVerifiedDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabResult> findByApprovedDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LabResult> findByStatusOrderByResultDate(LabResult.ResultStatus status);
    
    List<LabResult> findByStatusesOrderByResultDate(List<LabResult.ResultStatus> statuses);
    
    List<LabResult> findCriticalResultsNotInformed();
    
    Long countByStatus(LabResult.ResultStatus status);
    
    Long countByResultDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    LabResult createResult(LabOrder labOrder, User performedBy);
    
    LabResult enterResultValues(Long resultId, Map<Long, String> parameterValues);
    
    LabResult enterResultValue(Long resultId, Long parameterId, String value);
    
    LabResult enterResultValueFromAnalyzer(Long resultId, Long parameterId, String value, String analyzerId, String analyzerResult);
    
    LabResult markAsInProcess(Long resultId, User performedBy);
    
    LabResult markAsCompleted(Long resultId);
    
    LabResult markAsVerified(Long resultId, User verifiedBy);
    
    LabResult markAsApproved(Long resultId, User approvedBy, String pathologistNotes);
    
    LabResult markAsDelivered(Long resultId, String deliveryMethod);
    
    LabResult markCriticalResult(Long resultId, String informedTo);
    
    LabResult updateClinicalNotes(Long resultId, String clinicalNotes);
    
    LabResult updatePathologistNotes(Long resultId, String pathologistNotes);
    
    void deleteResult(Long id);
    
    byte[] generateResultPdf(Long resultId);
}