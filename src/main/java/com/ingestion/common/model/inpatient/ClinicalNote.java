package com.ingestion.common.model.inpatient;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_notes")
public class ClinicalNote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private InpatientAdmission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Note date is required")
    @Column(name = "note_date", nullable = false)
    private LocalDateTime noteDate;

    @NotBlank(message = "Note content is required")
    @Column(name = "note_content", nullable = false, length = 2000)
    private String noteContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_type", nullable = false)
    private NoteType noteType;

    @PrePersist
    protected void onCreate() {
        if (noteDate == null) {
            noteDate = LocalDateTime.now();
        }
    }

    public enum NoteType {
        PROGRESS_NOTE, ADMISSION_NOTE, DISCHARGE_NOTE, CONSULTATION_NOTE, PROCEDURE_NOTE, SURGICAL_NOTE, NURSING_NOTE, OTHER
    }

    // Getters and Setters
    public InpatientAdmission getAdmission() {
        return admission;
    }

    public void setAdmission(InpatientAdmission admission) {
        this.admission = admission;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(LocalDateTime noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }
}