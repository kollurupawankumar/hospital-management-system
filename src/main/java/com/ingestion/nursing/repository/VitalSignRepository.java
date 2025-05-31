package com.ingestion.nursing.repository;

import com.ingestion.common.model.inpatient.VitalSign;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {
    
    List<VitalSign> findByPatientOrderByRecordedAtDesc(Patient patient);
    
    Page<VitalSign> findByPatientOrderByRecordedAtDesc(Patient patient, Pageable pageable);
    
    List<VitalSign> findByPatientAndRecordedAtBetweenOrderByRecordedAtDesc(
            Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    
    // TODO: Update this query once we add isAbnormal field to the common VitalSign class
    @Query("SELECT v FROM VitalSign v WHERE v.patient = :patient ORDER BY v.recordedAt DESC")
    List<VitalSign> findByIsAbnormalTrueAndPatientOrderByRecordedAtDesc(@Param("patient") Patient patient);
    
    @Query("SELECT v FROM VitalSign v WHERE v.patient = :patient AND v.recordedAt = " +
           "(SELECT MAX(v2.recordedAt) FROM VitalSign v2 WHERE v2.patient = :patient)")
    VitalSign findLatestByPatient(@Param("patient") Patient patient);
    
    // TODO: Update this query once we add isAbnormal field to the common VitalSign class
    @Query("SELECT v FROM VitalSign v WHERE v.recordedAt >= :since ORDER BY v.recordedAt DESC")
    List<VitalSign> findRecentAbnormalVitals(@Param("since") LocalDateTime since);
}