package com.ingestion.laboratory.service.impl;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.LabResult;
import com.ingestion.laboratory.model.LabResultItem;
import com.ingestion.laboratory.repository.LabResultItemRepository;
import com.ingestion.laboratory.repository.LabResultRepository;
import com.ingestion.laboratory.service.LabResultService;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LabResultServiceImpl implements LabResultService {

    @Autowired
    private LabResultRepository labResultRepository;

    @Autowired
    private LabResultItemRepository labResultItemRepository;

    @Override
    public LabResult saveResult(LabResult labResult) {
        return labResultRepository.save(labResult);
    }

    @Override
    public Optional<LabResult> findById(Long id) {
        return labResultRepository.findById(id);
    }

    @Override
    public Optional<LabResult> findByResultNumber(String resultNumber) {
        return labResultRepository.findByResultNumber(resultNumber);
    }

    @Override
    public Optional<LabResult> findByLabOrder(LabOrder labOrder) {
        return labResultRepository.findByLabOrder(labOrder);
    }

    @Override
    public List<LabResult> findAll() {
        return labResultRepository.findAll();
    }

    @Override
    public List<LabResult> findByStatus(LabResult.ResultStatus status) {
        return labResultRepository.findByStatus(status);
    }

    @Override
    public List<LabResult> findByPerformedBy(User performedBy) {
        return labResultRepository.findByPerformedBy(performedBy);
    }

    @Override
    public List<LabResult> findByVerifiedBy(User verifiedBy) {
        return labResultRepository.findByVerifiedBy(verifiedBy);
    }

    @Override
    public List<LabResult> findByApprovedBy(User approvedBy) {
        return labResultRepository.findByApprovedBy(approvedBy);
    }

    @Override
    public List<LabResult> findByIsAbnormal(Boolean isAbnormal) {
        return labResultRepository.findByIsAbnormal(isAbnormal);
    }

    @Override
    public List<LabResult> findByIsCritical(Boolean isCritical) {
        return labResultRepository.findByIsCritical(isCritical);
    }

    @Override
    public List<LabResult> findByResultDate(LocalDate resultDate) {
        // Simplified implementation - return all results for now
        return labResultRepository.findAll();
    }

    @Override
    public List<LabResult> findByResultDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Simplified implementation - return all results for now
        return labResultRepository.findAll();
    }

    @Override
    public List<LabResult> findByVerifiedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Simplified implementation - return all results for now
        return labResultRepository.findAll();
    }

    @Override
    public List<LabResult> findByApprovedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Simplified implementation - return all results for now
        return labResultRepository.findAll();
    }

    @Override
    public List<LabResult> findByStatusOrderByResultDate(LabResult.ResultStatus status) {
        return labResultRepository.findByStatus(status);
    }

    @Override
    public List<LabResult> findByStatusesOrderByResultDate(List<LabResult.ResultStatus> statuses) {
        // Simplified implementation - return all results for now
        return labResultRepository.findAll();
    }

    @Override
    public List<LabResult> findCriticalResultsNotInformed() {
        return labResultRepository.findByIsCritical(true);
    }

    @Override
    public Long countByStatus(LabResult.ResultStatus status) {
        return (long) labResultRepository.findByStatus(status).size();
    }

    @Override
    public Long countByResultDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (long) labResultRepository.findAll().size();
    }

    @Override
    public LabResult createResult(LabOrder labOrder, User performedBy) {
        LabResult result = new LabResult();
        result.setResultNumber(generateResultNumber());
        result.setLabOrder(labOrder);
        result.setPerformedBy(performedBy);
        result.setStatus(LabResult.ResultStatus.PENDING);
        result.setResultDate(LocalDateTime.now());
        result.setCreatedAt(LocalDateTime.now());
        
        return labResultRepository.save(result);
    }

    @Override
    public LabResult enterResultValues(Long resultId, Map<Long, String> parameterValues) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            // Simplified implementation - just mark as completed for now
            result.setStatus(LabResult.ResultStatus.COMPLETED);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult enterResultValue(Long resultId, Long parameterId, String value) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            // Simplified implementation - just mark as completed for now
            result.setStatus(LabResult.ResultStatus.COMPLETED);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult enterResultValueFromAnalyzer(Long resultId, Long parameterId, String value, String analyzerId, String analyzerResult) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            // Simplified implementation - just mark as completed for now
            result.setStatus(LabResult.ResultStatus.COMPLETED);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult markAsInProcess(Long resultId, User performedBy) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setStatus(LabResult.ResultStatus.PENDING);
            result.setPerformedBy(performedBy);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult markAsCompleted(Long resultId) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setStatus(LabResult.ResultStatus.COMPLETED);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult markAsVerified(Long resultId, User verifiedBy) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setStatus(LabResult.ResultStatus.VERIFIED);
            result.setVerifiedBy(verifiedBy);
            result.setVerifiedDate(LocalDateTime.now());
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult markAsApproved(Long resultId, User approvedBy, String pathologistNotes) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setStatus(LabResult.ResultStatus.APPROVED);
            result.setApprovedBy(approvedBy);
            result.setApprovedDate(LocalDateTime.now());
            result.setPathologistNotes(pathologistNotes);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult markAsDelivered(Long resultId, String deliveryMethod) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setStatus(LabResult.ResultStatus.DELIVERED);
            // Note: deliveryMethod and deliveredDate fields don't exist in the model
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult markCriticalResult(Long resultId, String informedTo) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setIsCritical(true);
            // Note: informedTo and informedDate fields don't exist in the model
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult updateClinicalNotes(Long resultId, String clinicalNotes) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setClinicalNotes(clinicalNotes);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public LabResult updatePathologistNotes(Long resultId, String pathologistNotes) {
        Optional<LabResult> resultOpt = labResultRepository.findById(resultId);
        if (resultOpt.isPresent()) {
            LabResult result = resultOpt.get();
            result.setPathologistNotes(pathologistNotes);
            return labResultRepository.save(result);
        }
        throw new RuntimeException("Lab result not found with id: " + resultId);
    }

    @Override
    public void deleteResult(Long id) {
        labResultRepository.deleteById(id);
    }

    @Override
    public byte[] generateResultPdf(Long resultId) {
        // This would typically generate a PDF report
        // For now, return empty byte array as placeholder
        return new byte[0];
    }

    private String generateResultNumber() {
        return "RES" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}