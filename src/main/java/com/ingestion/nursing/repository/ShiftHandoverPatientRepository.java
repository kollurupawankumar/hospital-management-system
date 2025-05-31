package com.ingestion.nursing.repository;

import com.ingestion.nursing.model.ShiftHandover;
import com.ingestion.nursing.model.ShiftHandoverPatient;
import com.ingestion.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftHandoverPatientRepository extends JpaRepository<ShiftHandoverPatient, Long> {
    
    List<ShiftHandoverPatient> findByShiftHandover(ShiftHandover handover);
    
    List<ShiftHandoverPatient> findByPatientOrderByShiftHandover_HandoverDateDesc(Patient patient);
    
    List<ShiftHandoverPatient> findByIsCriticalTrueAndShiftHandover_HandoverDateBetweenOrderByShiftHandover_HandoverDateDesc(
            LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT shp FROM ShiftHandoverPatient shp WHERE shp.patient = :patient AND shp.shiftHandover.handoverDate = " +
           "(SELECT MAX(sh.handoverDate) FROM ShiftHandover sh JOIN sh.patients p WHERE p.patient = :patient)")
    ShiftHandoverPatient findLatestHandoverForPatient(@Param("patient") Patient patient);
    
    @Query("SELECT shp FROM ShiftHandoverPatient shp WHERE shp.isCritical = true AND shp.shiftHandover.handoverDate >= :since " +
           "ORDER BY shp.shiftHandover.handoverDate DESC")
    List<ShiftHandoverPatient> findRecentCriticalPatients(@Param("since") LocalDateTime since);
}