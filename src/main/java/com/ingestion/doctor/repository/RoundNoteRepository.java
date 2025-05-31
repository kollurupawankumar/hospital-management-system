package com.ingestion.doctor.repository;

import com.ingestion.doctor.model.RoundNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoundNoteRepository extends JpaRepository<RoundNote, Long> {
    
    List<RoundNote> findByPatientIdOrderByRoundDateTimeDesc(Long patientId);
    
    List<RoundNote> findByDoctorIdOrderByRoundDateTimeDesc(Long doctorId);
    
    List<RoundNote> findByAdmissionIdOrderByRoundDateTimeDesc(Long admissionId);
    
    @Query("SELECT r FROM RoundNote r WHERE r.patient.id = :patientId AND r.roundDateTime BETWEEN :startDateTime AND :endDateTime ORDER BY r.roundDateTime DESC")
    List<RoundNote> findByPatientIdAndDateTimeRange(
            @Param("patientId") Long patientId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
    
    @Query("SELECT r FROM RoundNote r WHERE r.doctor.id = :doctorId AND r.roundDateTime BETWEEN :startDateTime AND :endDateTime ORDER BY r.roundDateTime DESC")
    List<RoundNote> findByDoctorIdAndDateTimeRange(
            @Param("doctorId") Long doctorId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
    
    @Query("SELECT r FROM RoundNote r WHERE r.patient.id = :patientId AND (LOWER(r.subjectiveNotes) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(r.objectiveNotes) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(r.assessment) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(r.plan) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<RoundNote> searchRoundNotesByPatientId(
            @Param("patientId") Long patientId,
            @Param("searchTerm") String searchTerm);
}