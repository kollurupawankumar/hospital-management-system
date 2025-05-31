package com.ingestion.nursing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nursing_notes")
public class NursingNote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "note_type")
    @Enumerated(EnumType.STRING)
    private NoteType noteType;

    @Column(name = "note_content", length = 2000, nullable = false)
    private String noteContent;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    @Column(name = "is_flagged")
    private Boolean isFlagged = false;

    @Column(name = "flag_reason", length = 500)
    private String flagReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_plan_id")
    private CarePlan carePlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nursing_task_id")
    private NursingTask nursingTask;

    public enum NoteType {
        ASSESSMENT, INTERVENTION, MEDICATION, OBSERVATION, SHIFT_HANDOVER, CARE_PLAN, OTHER
    }

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public User getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(User recordedBy) {
        this.recordedBy = recordedBy;
    }

    public Boolean getIsFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(Boolean flagged) {
        isFlagged = flagged;
    }

    public String getFlagReason() {
        return flagReason;
    }

    public void setFlagReason(String flagReason) {
        this.flagReason = flagReason;
    }

    public CarePlan getCarePlan() {
        return carePlan;
    }

    public void setCarePlan(CarePlan carePlan) {
        this.carePlan = carePlan;
    }

    public NursingTask getNursingTask() {
        return nursingTask;
    }

    public void setNursingTask(NursingTask nursingTask) {
        this.nursingTask = nursingTask;
    }

    // Helper methods
    public void flagNote(String reason) {
        this.isFlagged = true;
        this.flagReason = reason;
    }

    public void unflagNote() {
        this.isFlagged = false;
        this.flagReason = null;
    }
}