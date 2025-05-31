package com.ingestion.patient.repository;

import com.ingestion.patient.model.TelemedicineSession;
import com.ingestion.patient.model.TelemedicineSession.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelemedicineSessionRepository extends JpaRepository<TelemedicineSession, Long> {
    
    List<TelemedicineSession> findByPatientId(Long patientId);
    
    List<TelemedicineSession> findByDoctorId(Long doctorId);
    
    Optional<TelemedicineSession> findByAppointmentId(Long appointmentId);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.patient.id = :patientId ORDER BY ts.sessionDate DESC")
    List<TelemedicineSession> findByPatientIdOrderByDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.doctor.id = :doctorId ORDER BY ts.sessionDate DESC")
    List<TelemedicineSession> findByDoctorIdOrderByDateDesc(@Param("doctorId") Long doctorId);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.patient.id = :patientId AND ts.sessionDate >= :now ORDER BY ts.sessionDate ASC")
    List<TelemedicineSession> findUpcomingSessionsByPatientId(
            @Param("patientId") Long patientId,
            @Param("now") LocalDateTime now);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.doctor.id = :doctorId AND ts.sessionDate >= :now ORDER BY ts.sessionDate ASC")
    List<TelemedicineSession> findUpcomingSessionsByDoctorId(
            @Param("doctorId") Long doctorId,
            @Param("now") LocalDateTime now);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.status = :status ORDER BY ts.sessionDate ASC")
    List<TelemedicineSession> findByStatus(@Param("status") SessionStatus status);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.sessionDate BETWEEN :startDate AND :endDate ORDER BY ts.sessionDate ASC")
    List<TelemedicineSession> findSessionsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.sessionDate < :now AND ts.status = 'SCHEDULED'")
    List<TelemedicineSession> findMissedSessions(@Param("now") LocalDateTime now);
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.isRecorded = true ORDER BY ts.sessionDate DESC")
    List<TelemedicineSession> findRecordedSessions();
    
    @Query("SELECT ts FROM TelemedicineSession ts WHERE ts.patient.id = :patientId AND ts.isRecorded = true ORDER BY ts.sessionDate DESC")
    List<TelemedicineSession> findRecordedSessionsByPatientId(@Param("patientId") Long patientId);
}