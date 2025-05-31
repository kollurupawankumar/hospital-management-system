package com.ingestion.nursing.repository;

import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.NursingNote;
import com.ingestion.nursing.model.NursingTask;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NursingNoteRepository extends JpaRepository<NursingNote, Long> {
    
    List<NursingNote> findByPatientOrderByRecordedAtDesc(Patient patient);
    
    Page<NursingNote> findByPatientOrderByRecordedAtDesc(Patient patient, Pageable pageable);
    
    List<NursingNote> findByPatientAndRecordedAtBetweenOrderByRecordedAtDesc(
            Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    
    List<NursingNote> findByPatientAndNoteTypeOrderByRecordedAtDesc(
            Patient patient, NursingNote.NoteType noteType);
    
    List<NursingNote> findByIsFlaggedTrueOrderByRecordedAtDesc();
    
    List<NursingNote> findByCarePlanOrderByRecordedAtDesc(CarePlan carePlan);
    
    List<NursingNote> findByNursingTaskOrderByRecordedAtDesc(NursingTask nursingTask);
    
    List<NursingNote> findByNoteTypeAndRecordedAtBetweenOrderByRecordedAtDesc(
            NursingNote.NoteType noteType, LocalDateTime startDate, LocalDateTime endDate);
}