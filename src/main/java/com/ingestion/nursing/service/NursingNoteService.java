package com.ingestion.nursing.service;

import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.NursingNote;
import com.ingestion.nursing.model.NursingTask;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NursingNoteService {
    
    NursingNote saveNursingNote(NursingNote nursingNote);
    
    Optional<NursingNote> findById(Long id);
    
    List<NursingNote> findByPatient(Patient patient);
    
    Page<NursingNote> findByPatient(Patient patient, Pageable pageable);
    
    List<NursingNote> findByPatientAndDateRange(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    
    List<NursingNote> findByPatientAndNoteType(Patient patient, NursingNote.NoteType noteType);
    
    List<NursingNote> findFlaggedNotes();
    
    List<NursingNote> findByCarePlan(CarePlan carePlan);
    
    List<NursingNote> findByNursingTask(NursingTask nursingTask);
    
    List<NursingNote> findByNoteTypeAndDateRange(NursingNote.NoteType noteType, LocalDateTime startDate, LocalDateTime endDate);
    
    NursingNote createNote(Patient patient, NursingNote.NoteType noteType, String noteContent, 
                         User recordedBy, CarePlan carePlan, NursingTask nursingTask);
    
    NursingNote flagNote(Long noteId, String reason);
    
    NursingNote unflagNote(Long noteId);
    
    void deleteNote(Long id);
}