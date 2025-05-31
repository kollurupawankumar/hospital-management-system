package com.ingestion.doctor.repository;

import com.ingestion.doctor.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    
    List<Diagnosis> findByPatientIdOrderByDiagnosisDateDesc(Long patientId);
    
    List<Diagnosis> findByDoctorIdOrderByDiagnosisDateDesc(Long doctorId);
    
    List<Diagnosis> findByPatientIdAndIsChronicTrue(Long patientId);
    
    List<Diagnosis> findByPatientIdAndIsPrimaryTrue(Long patientId);
    
    @Query("SELECT d FROM Diagnosis d WHERE d.patient.id = :patientId AND d.diagnosisDate BETWEEN :startDate AND :endDate ORDER BY d.diagnosisDate DESC")
    List<Diagnosis> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT d FROM Diagnosis d WHERE d.doctor.id = :doctorId AND d.diagnosisDate BETWEEN :startDate AND :endDate ORDER BY d.diagnosisDate DESC")
    List<Diagnosis> findByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT d FROM Diagnosis d WHERE d.patient.id = :patientId AND LOWER(d.diagnosisName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(d.diagnosisCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Diagnosis> searchDiagnosesByPatientId(
            @Param("patientId") Long patientId,
            @Param("searchTerm") String searchTerm);
    
    @Query("SELECT DISTINCT d.diagnosisType FROM Diagnosis d WHERE d.diagnosisType IS NOT NULL ORDER BY d.diagnosisType")
    List<String> findAllDiagnosisTypes();
}