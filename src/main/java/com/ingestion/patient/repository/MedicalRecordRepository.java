package com.ingestion.patient.repository;

import com.ingestion.patient.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    
    List<MedicalRecord> findByPatientId(Long patientId);
    
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId ORDER BY mr.recordDate DESC")
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId AND mr.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId AND mr.recordType = :recordType")
    List<MedicalRecord> findByPatientIdAndRecordType(
            @Param("patientId") Long patientId,
            @Param("recordType") String recordType);
}