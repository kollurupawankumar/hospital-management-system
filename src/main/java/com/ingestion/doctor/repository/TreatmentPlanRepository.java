package com.ingestion.doctor.repository;

import com.ingestion.doctor.model.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
    
    List<TreatmentPlan> findByPatientIdOrderByStartDateDesc(Long patientId);
    
    List<TreatmentPlan> findByDoctorIdOrderByStartDateDesc(Long doctorId);
    
    @Query("SELECT tp FROM TreatmentPlan tp WHERE tp.patient.id = :patientId AND tp.startDate <= :currentDate AND (tp.endDate IS NULL OR tp.endDate >= :currentDate) ORDER BY tp.startDate DESC")
    List<TreatmentPlan> findByPatientIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            @Param("patientId") Long patientId,
            @Param("currentDate") LocalDate currentDate,
            @Param("currentDate") LocalDate currentDate2);
    
    @Query("SELECT tp FROM TreatmentPlan tp WHERE tp.diagnosis.id = :diagnosisId ORDER BY tp.startDate DESC")
    List<TreatmentPlan> findByDiagnosisId(@Param("diagnosisId") Long diagnosisId);
    
    @Query("SELECT tp FROM TreatmentPlan tp WHERE tp.patient.id = :patientId AND tp.startDate BETWEEN :startDate AND :endDate ORDER BY tp.startDate DESC")
    List<TreatmentPlan> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT tp FROM TreatmentPlan tp WHERE tp.doctor.id = :doctorId AND tp.startDate BETWEEN :startDate AND :endDate ORDER BY tp.startDate DESC")
    List<TreatmentPlan> findByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT tp FROM TreatmentPlan tp WHERE tp.patient.id = :patientId AND LOWER(tp.planName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(tp.planDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<TreatmentPlan> searchTreatmentPlansByPatientId(
            @Param("patientId") Long patientId,
            @Param("searchTerm") String searchTerm);
}