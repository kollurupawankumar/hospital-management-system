package com.ingestion.opd.repository;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpdVisitRepository extends JpaRepository<OpdVisit, Long> {

    Optional<OpdVisit> findByTokenNumber(String tokenNumber);
    
    List<OpdVisit> findByPatient(Patient patient);
    
    List<OpdVisit> findByDoctor(Doctor doctor);
    
    List<OpdVisit> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdVisit> findByVisitStatus(OpdVisit.VisitStatus status);
    
    List<OpdVisit> findByDoctorAndVisitDateBetween(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);
    
    List<OpdVisit> findByPatientAndVisitDateBetween(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT v FROM OpdVisit v WHERE v.doctor = :doctor AND v.visitDate >= :today AND v.visitStatus IN :statuses ORDER BY v.visitDate")
    List<OpdVisit> findTodayVisitsByDoctor(@Param("doctor") Doctor doctor, @Param("today") LocalDateTime today, @Param("statuses") List<OpdVisit.VisitStatus> statuses);
    
    @Query("SELECT v FROM OpdVisit v WHERE v.visitDate >= :startDate AND v.visitDate <= :endDate AND v.visitStatus IN :statuses")
    Page<OpdVisit> findVisitsByDateRangeAndStatus(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statuses") List<OpdVisit.VisitStatus> statuses,
            Pageable pageable);
    
    @Query("SELECT v FROM OpdVisit v WHERE v.doctor = :doctor AND v.visitDate >= :startDate AND v.visitDate <= :endDate AND v.visitStatus IN :statuses")
    Page<OpdVisit> findVisitsByDoctorAndDateRangeAndStatus(
            @Param("doctor") Doctor doctor,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statuses") List<OpdVisit.VisitStatus> statuses,
            Pageable pageable);
    
    @Query("SELECT COUNT(v) FROM OpdVisit v WHERE v.doctor = :doctor AND DATE(v.visitDate) = DATE(:date) AND v.visitStatus IN :statuses")
    Long countVisitsByDoctorAndDate(@Param("doctor") Doctor doctor, @Param("date") LocalDateTime date, @Param("statuses") List<OpdVisit.VisitStatus> statuses);
    
    @Query("SELECT v FROM OpdVisit v WHERE v.patient = :patient ORDER BY v.visitDate DESC")
    Page<OpdVisit> findPatientVisitHistory(@Param("patient") Patient patient, Pageable pageable);
}