package com.ingestion.laboratory.service.impl;

import com.ingestion.laboratory.model.LabAnalyzer;
import com.ingestion.laboratory.model.LabTest;
import com.ingestion.laboratory.repository.LabAnalyzerRepository;
import com.ingestion.laboratory.service.LabAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LabAnalyzerServiceImpl implements LabAnalyzerService {

    @Autowired
    private LabAnalyzerRepository labAnalyzerRepository;

    @Override
    public LabAnalyzer saveAnalyzer(LabAnalyzer labAnalyzer) {
        return labAnalyzerRepository.save(labAnalyzer);
    }

    @Override
    public Optional<LabAnalyzer> findById(Long id) {
        return labAnalyzerRepository.findById(id);
    }

    @Override
    public Optional<LabAnalyzer> findByAnalyzerId(String analyzerId) {
        return labAnalyzerRepository.findByAnalyzerId(analyzerId);
    }

    @Override
    public List<LabAnalyzer> findAll() {
        return labAnalyzerRepository.findAll();
    }

    @Override
    public List<LabAnalyzer> findByAnalyzerName(String analyzerName) {
        return labAnalyzerRepository.findByAnalyzerNameContainingIgnoreCase(analyzerName);
    }

    @Override
    public List<LabAnalyzer> findByManufacturer(String manufacturer) {
        return labAnalyzerRepository.findByManufacturerContainingIgnoreCase(manufacturer);
    }

    @Override
    public List<LabAnalyzer> findByStatus(LabAnalyzer.AnalyzerStatus status) {
        return labAnalyzerRepository.findByStatus(status);
    }

    @Override
    public List<LabAnalyzer> findByLocation(String location) {
        return labAnalyzerRepository.findByLocation(location);
    }

    @Override
    public List<LabAnalyzer> findAllActiveAnalyzers() {
        return labAnalyzerRepository.findAllActiveAnalyzers();
    }

    @Override
    public List<LabAnalyzer> findActiveAnalyzersSupportingTest(LabTest test) {
        return labAnalyzerRepository.findActiveAnalyzersSupportingTest(test);
    }

    @Override
    public List<LabAnalyzer> findAnalyzersNeedingMaintenance() {
        LocalDateTime now = LocalDateTime.now();
        return labAnalyzerRepository.findAnalyzersNeedingMaintenance(now);
    }

    @Override
    public List<LabAnalyzer> findAnalyzersNeedingCalibration() {
        LocalDateTime now = LocalDateTime.now();
        return labAnalyzerRepository.findAnalyzersNeedingCalibration(now);
    }

    @Override
    public LabAnalyzer createAnalyzer(LabAnalyzer analyzer, List<LabTest> supportedTests) {
        analyzer.setCreatedAt(LocalDateTime.now());
        // Note: supportedTests would be handled through a many-to-many relationship in the LabAnalyzer entity
        // For now, just save the analyzer
        return labAnalyzerRepository.save(analyzer);
    }

    @Override
    public LabAnalyzer updateAnalyzer(Long id, LabAnalyzer analyzer) {
        Optional<LabAnalyzer> existingOpt = labAnalyzerRepository.findById(id);
        if (existingOpt.isPresent()) {
            LabAnalyzer existing = existingOpt.get();
            existing.setAnalyzerName(analyzer.getAnalyzerName());
            existing.setManufacturer(analyzer.getManufacturer());
            existing.setModel(analyzer.getModel());
            existing.setSerialNumber(analyzer.getSerialNumber());
            existing.setLocation(analyzer.getLocation());
            existing.setStatus(analyzer.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(existing);
        }
        throw new RuntimeException("Analyzer not found with id: " + id);
    }

    @Override
    public LabAnalyzer addSupportedTest(Long analyzerId, LabTest test) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            // Note: This would typically add the test to a supportedTests collection in the analyzer
            // For now, just return the analyzer
            return analyzer;
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer removeSupportedTest(Long analyzerId, Long testId) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            // Note: This would typically remove the test from a supportedTests collection in the analyzer
            // For now, just return the analyzer
            return analyzer;
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer markAsMaintenance(Long analyzerId) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            analyzer.setStatus(LabAnalyzer.AnalyzerStatus.MAINTENANCE);
            analyzer.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(analyzer);
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer markAsCalibration(Long analyzerId) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            analyzer.setStatus(LabAnalyzer.AnalyzerStatus.CALIBRATION);
            analyzer.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(analyzer);
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer markAsActive(Long analyzerId) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            analyzer.setStatus(LabAnalyzer.AnalyzerStatus.ACTIVE);
            analyzer.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(analyzer);
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer markAsInactive(Long analyzerId) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            analyzer.setStatus(LabAnalyzer.AnalyzerStatus.INACTIVE);
            analyzer.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(analyzer);
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer markAsOutOfOrder(Long analyzerId) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            analyzer.setStatus(LabAnalyzer.AnalyzerStatus.OUT_OF_ORDER);
            analyzer.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(analyzer);
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer performMaintenance(Long analyzerId, LocalDateTime maintenanceDate, LocalDateTime nextMaintenanceDate) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            analyzer.setLastMaintenanceDate(maintenanceDate);
            analyzer.setNextMaintenanceDate(nextMaintenanceDate);
            analyzer.setStatus(LabAnalyzer.AnalyzerStatus.ACTIVE);
            analyzer.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(analyzer);
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public LabAnalyzer performCalibration(Long analyzerId, LocalDateTime calibrationDate, LocalDateTime nextCalibrationDate) {
        Optional<LabAnalyzer> analyzerOpt = labAnalyzerRepository.findById(analyzerId);
        if (analyzerOpt.isPresent()) {
            LabAnalyzer analyzer = analyzerOpt.get();
            analyzer.setLastCalibrationDate(calibrationDate);
            analyzer.setNextCalibrationDate(nextCalibrationDate);
            analyzer.setStatus(LabAnalyzer.AnalyzerStatus.ACTIVE);
            analyzer.setUpdatedAt(LocalDateTime.now());
            return labAnalyzerRepository.save(analyzer);
        }
        throw new RuntimeException("Analyzer not found with id: " + analyzerId);
    }

    @Override
    public void deleteAnalyzer(Long id) {
        labAnalyzerRepository.deleteById(id);
    }

    @Override
    public String sendTestToAnalyzer(Long analyzerId, Long labOrderId, Long testId) {
        // This would typically send test data to the analyzer via HL7 or other protocol
        // For now, return a placeholder transaction ID
        return "TXN" + System.currentTimeMillis();
    }

    @Override
    public void receiveResultFromAnalyzer(String analyzerId, String sampleId, String testCode, String parameterName, String result) {
        // This would typically process results received from the analyzer
        // For now, this is a placeholder implementation
        // In a real implementation, this would:
        // 1. Find the corresponding lab order/sample
        // 2. Create or update lab result items
        // 3. Mark results as received from analyzer
    }
}