package com.ingestion.opd.repository;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdPrescription;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpdPrescriptionRepository extends JpaRepository<OpdPrescription, Long> {

    Optional<OpdPrescription> findByPrescriptionNumber(String prescriptionNumber);
    
    List<OpdPrescription> findByOpdVisit(OpdVisit opdVisit);
    
    List<OpdPrescription> findByDoctor(Doctor doctor);
    
    @Query("SELECT p FROM OpdPrescription p WHERE p.opdVisit.patient = :patient ORDER BY p.prescriptionDate DESC")
    List<OpdPrescription> findByPatient(@Param("patient") Patient patient);
    
    List<OpdPrescription> findByPrescriptionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT p FROM OpdPrescription p WHERE p.doctor = :doctor AND p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<OpdPrescription> findByDoctorAndDateRange(
            @Param("doctor") Doctor doctor,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM OpdPrescription p WHERE p.opdVisit.patient = :patient AND p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<OpdPrescription> findByPatientAndDateRange(
            @Param("patient") Patient patient,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}