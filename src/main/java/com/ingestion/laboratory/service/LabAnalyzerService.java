package com.ingestion.laboratory.service;

import com.ingestion.laboratory.model.LabAnalyzer;
import com.ingestion.laboratory.model.LabTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LabAnalyzerService {

    LabAnalyzer saveAnalyzer(LabAnalyzer labAnalyzer);
    
    Optional<LabAnalyzer> findById(Long id);
    
    Optional<LabAnalyzer> findByAnalyzerId(String analyzerId);
    
    List<LabAnalyzer> findAll();
    
    List<LabAnalyzer> findByAnalyzerName(String analyzerName);
    
    List<LabAnalyzer> findByManufacturer(String manufacturer);
    
    List<LabAnalyzer> findByStatus(LabAnalyzer.AnalyzerStatus status);
    
    List<LabAnalyzer> findByLocation(String location);
    
    List<LabAnalyzer> findAllActiveAnalyzers();
    
    List<LabAnalyzer> findActiveAnalyzersSupportingTest(LabTest test);
    
    List<LabAnalyzer> findAnalyzersNeedingMaintenance();
    
    List<LabAnalyzer> findAnalyzersNeedingCalibration();
    
    LabAnalyzer createAnalyzer(LabAnalyzer analyzer, List<LabTest> supportedTests);
    
    LabAnalyzer updateAnalyzer(Long id, LabAnalyzer analyzer);
    
    LabAnalyzer addSupportedTest(Long analyzerId, LabTest test);
    
    LabAnalyzer removeSupportedTest(Long analyzerId, Long testId);
    
    LabAnalyzer markAsMaintenance(Long analyzerId);
    
    LabAnalyzer markAsCalibration(Long analyzerId);
    
    LabAnalyzer markAsActive(Long analyzerId);
    
    LabAnalyzer markAsInactive(Long analyzerId);
    
    LabAnalyzer markAsOutOfOrder(Long analyzerId);
    
    LabAnalyzer performMaintenance(Long analyzerId, LocalDateTime maintenanceDate, LocalDateTime nextMaintenanceDate);
    
    LabAnalyzer performCalibration(Long analyzerId, LocalDateTime calibrationDate, LocalDateTime nextCalibrationDate);
    
    void deleteAnalyzer(Long id);
    
    // Methods for analyzer integration
    String sendTestToAnalyzer(Long analyzerId, Long labOrderId, Long testId);
    
    void receiveResultFromAnalyzer(String analyzerId, String sampleId, String testCode, String parameterName, String result);
}