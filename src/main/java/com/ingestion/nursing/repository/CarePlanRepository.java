package com.ingestion.nursing.repository;

import com.ingestion.nursing.model.CarePlan;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarePlanRepository extends JpaRepository<CarePlan, Long> {
    
    List<CarePlan> findByPatientOrderByStartDateDesc(Patient patient);
    
    Page<CarePlan> findByPatientOrderByStartDateDesc(Patient patient, Pageable pageable);
    
    List<CarePlan> findByPatientAndStatusOrderByStartDateDesc(
            Patient patient, CarePlan.CarePlanStatus status);
    
    List<CarePlan> findByCreatedByOrderByCreatedDateDesc(User nurse);
    
    List<CarePlan> findByStatusAndStartDateBetweenOrderByStartDateDesc(
            CarePlan.CarePlanStatus status, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT cp FROM CarePlan cp WHERE cp.status = 'ACTIVE' AND cp.patient = :patient ORDER BY cp.startDate DESC")
    List<CarePlan> findActiveCarePlansForPatient(@Param("patient") Patient patient);
    
    @Query("SELECT cp FROM CarePlan cp WHERE cp.status = 'ACTIVE' AND " +
           "(cp.endDate IS NULL OR cp.endDate > :now) ORDER BY cp.startDate DESC")
    List<CarePlan> findCurrentActiveCarePlans(@Param("now") LocalDateTime now);
}