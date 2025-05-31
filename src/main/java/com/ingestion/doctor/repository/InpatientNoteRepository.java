package com.ingestion.doctor.repository;

import com.ingestion.doctor.model.InpatientNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InpatientNoteRepository extends JpaRepository<InpatientNote, Long> {
    
    List<InpatientNote> findByPatientIdOrderByNoteDateTimeDesc(Long patientId);
    
    List<InpatientNote> findByDoctorIdOrderByNoteDateTimeDesc(Long doctorId);
    
    List<InpatientNote> findByAdmissionIdOrderByNoteDateTimeDesc(Long admissionId);
    
    @Query("SELECT n FROM InpatientNote n WHERE n.patient.id = :patientId AND n.noteType = :noteType ORDER BY n.noteDateTime DESC")
    List<InpatientNote> findByPatientIdAndNoteType(
            @Param("patientId") Long patientId,
            @Param("noteType") String noteType);
    
    @Query("SELECT n FROM InpatientNote n WHERE n.patient.id = :patientId AND n.noteDateTime BETWEEN :startDateTime AND :endDateTime ORDER BY n.noteDateTime DESC")
    List<InpatientNote> findByPatientIdAndDateTimeRange(
            @Param("patientId") Long patientId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
    
    @Query("SELECT n FROM InpatientNote n WHERE n.doctor.id = :doctorId AND n.noteDateTime BETWEEN :startDateTime AND :endDateTime ORDER BY n.noteDateTime DESC")
    List<InpatientNote> findByDoctorIdAndDateTimeRange(
            @Param("doctorId") Long doctorId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
    
    @Query("SELECT n FROM InpatientNote n WHERE n.patient.id = :patientId AND (LOWER(n.noteContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(n.assessments) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(n.plans) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<InpatientNote> searchNotesByPatientId(
            @Param("patientId") Long patientId,
            @Param("searchTerm") String searchTerm);
}