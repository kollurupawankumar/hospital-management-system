package com.ingestion.patient.repository;

import com.ingestion.patient.model.LabReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LabReportRepository extends JpaRepository<LabReport, Long> {
    
    List<LabReport> findByPatientId(Long patientId);
    
    @Query("SELECT lr FROM LabReport lr WHERE lr.patient.id = :patientId ORDER BY lr.reportDate DESC")
    List<LabReport> findByPatientIdOrderByDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT lr FROM LabReport lr WHERE lr.patient.id = :patientId AND lr.reportType = :reportType ORDER BY lr.reportDate DESC")
    List<LabReport> findByPatientIdAndReportType(
            @Param("patientId") Long patientId,
            @Param("reportType") String reportType);
    
    @Query("SELECT lr FROM LabReport lr WHERE lr.patient.id = :patientId AND lr.reportDate BETWEEN :startDate AND :endDate ORDER BY lr.reportDate DESC")
    List<LabReport> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT lr FROM LabReport lr WHERE lr.patient.id = :patientId AND lr.isAbnormal = true ORDER BY lr.reportDate DESC")
    List<LabReport> findAbnormalByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT lr FROM LabReport lr WHERE lr.patient.id = :patientId AND lr.isViewedByPatient = false ORDER BY lr.reportDate DESC")
    List<LabReport> findUnviewedByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT lr FROM LabReport lr WHERE lr.isViewedByDoctor = false ORDER BY lr.reportDate DESC")
    List<LabReport> findAllUnviewedByDoctor();
    
    @Query("SELECT DISTINCT lr.reportType FROM LabReport lr ORDER BY lr.reportType")
    List<String> findAllReportTypes();
}