package com.ingestion.laboratory.repository;

import com.ingestion.laboratory.model.LabAnalyzer;
import com.ingestion.laboratory.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabAnalyzerRepository extends JpaRepository<LabAnalyzer, Long> {

    Optional<LabAnalyzer> findByAnalyzerId(String analyzerId);
    
    List<LabAnalyzer> findByAnalyzerNameContainingIgnoreCase(String analyzerName);
    
    List<LabAnalyzer> findByManufacturerContainingIgnoreCase(String manufacturer);
    
    List<LabAnalyzer> findByStatus(LabAnalyzer.AnalyzerStatus status);
    
    List<LabAnalyzer> findByLocation(String location);
    
    @Query("SELECT a FROM LabAnalyzer a WHERE a.status = 'ACTIVE' ORDER BY a.analyzerName")
    List<LabAnalyzer> findAllActiveAnalyzers();
    
    @Query("SELECT a FROM LabAnalyzer a WHERE :test MEMBER OF a.supportedTests AND a.status = 'ACTIVE'")
    List<LabAnalyzer> findActiveAnalyzersSupportingTest(@Param("test") LabTest test);
    
    @Query("SELECT a FROM LabAnalyzer a WHERE a.nextMaintenanceDate <= :date AND a.status = 'ACTIVE'")
    List<LabAnalyzer> findAnalyzersNeedingMaintenance(@Param("date") LocalDateTime date);
    
    @Query("SELECT a FROM LabAnalyzer a WHERE a.nextCalibrationDate <= :date AND a.status = 'ACTIVE'")
    List<LabAnalyzer> findAnalyzersNeedingCalibration(@Param("date") LocalDateTime date);
}