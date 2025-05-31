package com.ingestion.doctor.repository;

import com.ingestion.doctor.model.DischargeSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DischargeSummaryRepository extends JpaRepository<DischargeSummary, Long> {
    
    List<DischargeSummary> findByPatientIdOrderByDischargeDateDesc(Long patientId);
    
    List<DischargeSummary> findByDoctorIdOrderByDischargeDateDesc(Long doctorId);
    
    Optional<DischargeSummary> findByAdmissionId(Long admissionId);
    
    @Query("SELECT ds FROM DischargeSummary ds WHERE ds.patient.id = :patientId AND ds.dischargeDate BETWEEN :startDate AND :endDate ORDER BY ds.dischargeDate DESC")
    List<DischargeSummary> findByPatientIdAndDischargeDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ds FROM DischargeSummary ds WHERE ds.doctor.id = :doctorId AND ds.dischargeDate BETWEEN :startDate AND :endDate ORDER BY ds.dischargeDate DESC")
    List<DischargeSummary> findByDoctorIdAndDischargeDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ds FROM DischargeSummary ds WHERE ds.patient.id = :patientId AND (LOWER(ds.dischargeDiagnosis) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(ds.briefHospitalCourse) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(ds.proceduresPerformed) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<DischargeSummary> searchDischargeSummariesByPatientId(
            @Param("patientId") Long patientId,
            @Param("searchTerm") String searchTerm);
}