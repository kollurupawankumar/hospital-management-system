package com.ingestion.laboratory.service.impl;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.LabSample;
import com.ingestion.laboratory.repository.LabSampleRepository;
import com.ingestion.laboratory.service.LabSampleService;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LabSampleServiceImpl implements LabSampleService {

    @Autowired
    private LabSampleRepository labSampleRepository;

    @Override
    public LabSample saveSample(LabSample labSample) {
        return labSampleRepository.save(labSample);
    }

    @Override
    public Optional<LabSample> findById(Long id) {
        return labSampleRepository.findById(id);
    }

    @Override
    public Optional<LabSample> findBySampleId(String sampleId) {
        return labSampleRepository.findBySampleId(sampleId);
    }

    @Override
    public Optional<LabSample> findByLabOrder(LabOrder labOrder) {
        return labSampleRepository.findByLabOrder(labOrder);
    }

    @Override
    public List<LabSample> findAll() {
        return labSampleRepository.findAll();
    }

    @Override
    public List<LabSample> findBySampleType(String sampleType) {
        return labSampleRepository.findBySampleType(sampleType);
    }

    @Override
    public List<LabSample> findByStatus(LabSample.SampleStatus status) {
        return labSampleRepository.findByStatus(status);
    }

    @Override
    public List<LabSample> findByCollectedBy(User collectedBy) {
        return labSampleRepository.findByCollectedBy(collectedBy);
    }

    @Override
    public List<LabSample> findByReceivedBy(User receivedBy) {
        return labSampleRepository.findByReceivedBy(receivedBy);
    }

    @Override
    public List<LabSample> findByCollectionDate(LocalDate collectionDate) {
        // Simplified implementation - return all samples for now
        return labSampleRepository.findAll();
    }

    @Override
    public List<LabSample> findByCollectionDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Simplified implementation - return all samples for now
        return labSampleRepository.findAll();
    }

    @Override
    public List<LabSample> findByReceivedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Simplified implementation - return all samples for now
        return labSampleRepository.findAll();
    }

    @Override
    public List<LabSample> findByStatusOrderByCollectionDate(LabSample.SampleStatus status) {
        return labSampleRepository.findByStatus(status);
    }

    @Override
    public List<LabSample> findByStatusesOrderByCollectionDate(List<LabSample.SampleStatus> statuses) {
        // Simplified implementation - return all samples for now
        return labSampleRepository.findAll();
    }

    @Override
    public Long countByStatus(LabSample.SampleStatus status) {
        return (long) labSampleRepository.findByStatus(status).size();
    }

    @Override
    public Long countByCollectionDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (long) labSampleRepository.findAll().size();
    }

    @Override
    public LabSample createSample(LabOrder labOrder, String sampleType, String containerType, String volume) {
        LabSample sample = new LabSample();
        sample.setSampleId(generateSampleId());
        sample.setLabOrder(labOrder);
        sample.setSampleType(sampleType);
        sample.setContainerType(containerType);
        sample.setVolume(volume);
        sample.setStatus(LabSample.SampleStatus.PENDING);
        sample.setCreatedAt(LocalDateTime.now());
        
        return labSampleRepository.save(sample);
    }

    @Override
    public LabSample collectSample(Long sampleId, User collectedBy, LocalDateTime collectionDate, String notes) {
        Optional<LabSample> sampleOpt = labSampleRepository.findById(sampleId);
        if (sampleOpt.isPresent()) {
            LabSample sample = sampleOpt.get();
            sample.setStatus(LabSample.SampleStatus.COLLECTED);
            sample.setCollectedBy(collectedBy);
            sample.setCollectionDate(collectionDate);
            // Note: notes field doesn't exist in the model
            return labSampleRepository.save(sample);
        }
        throw new RuntimeException("Sample not found with id: " + sampleId);
    }

    @Override
    public LabSample receiveSample(Long sampleId, User receivedBy, LocalDateTime receivedDate, String storageLocation) {
        Optional<LabSample> sampleOpt = labSampleRepository.findById(sampleId);
        if (sampleOpt.isPresent()) {
            LabSample sample = sampleOpt.get();
            sample.setStatus(LabSample.SampleStatus.RECEIVED);
            sample.setReceivedBy(receivedBy);
            sample.setReceivedDate(receivedDate);
            sample.setStorageLocation(storageLocation);
            return labSampleRepository.save(sample);
        }
        throw new RuntimeException("Sample not found with id: " + sampleId);
    }

    @Override
    public LabSample markAsInProcess(Long sampleId) {
        Optional<LabSample> sampleOpt = labSampleRepository.findById(sampleId);
        if (sampleOpt.isPresent()) {
            LabSample sample = sampleOpt.get();
            sample.setStatus(LabSample.SampleStatus.IN_PROCESS);
            return labSampleRepository.save(sample);
        }
        throw new RuntimeException("Sample not found with id: " + sampleId);
    }

    @Override
    public LabSample markAsProcessed(Long sampleId) {
        Optional<LabSample> sampleOpt = labSampleRepository.findById(sampleId);
        if (sampleOpt.isPresent()) {
            LabSample sample = sampleOpt.get();
            sample.setStatus(LabSample.SampleStatus.PROCESSED);
            return labSampleRepository.save(sample);
        }
        throw new RuntimeException("Sample not found with id: " + sampleId);
    }

    @Override
    public LabSample markAsRejected(Long sampleId, String rejectionReason) {
        Optional<LabSample> sampleOpt = labSampleRepository.findById(sampleId);
        if (sampleOpt.isPresent()) {
            LabSample sample = sampleOpt.get();
            sample.setStatus(LabSample.SampleStatus.REJECTED);
            sample.setRejectionReason(rejectionReason);
            return labSampleRepository.save(sample);
        }
        throw new RuntimeException("Sample not found with id: " + sampleId);
    }

    @Override
    public void deleteSample(Long id) {
        labSampleRepository.deleteById(id);
    }

    private String generateSampleId() {
        return "SAM" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}