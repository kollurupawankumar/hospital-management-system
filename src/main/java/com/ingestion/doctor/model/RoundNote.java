package com.ingestion.doctor.model;

import com.ingestion.common.model.inpatient.InpatientAdmission;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "round_notes")

public class RoundNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id")
    private InpatientAdmission admission;

    @Column(name = "round_date_time", nullable = false)
    private LocalDateTime roundDateTime;

    @Column(name = "subjective_notes", nullable = false, length = 1000)
    private String subjectiveNotes;

    @Column(name = "objective_notes", length = 1000)
    private String objectiveNotes;

    @Column(name = "vital_signs", length = 500)
    private String vitalSigns;

    @Column(name = "assessment", length = 1000)
    private String assessment;

    @Column(name = "plan", length = 1000)
    private String plan;

    @Column(name = "medication_changes", length = 1000)
    private String medicationChanges;

    @Column(name = "dietary_changes", length = 500)
    private String dietaryChanges;

    @Column(name = "activity_changes", length = 500)
    private String activityChanges;

    @Column(name = "additional_instructions", length = 1000)
    private String additionalInstructions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (roundDateTime == null) {
            roundDateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public InpatientAdmission getAdmission() {
        return admission;
    }

    public void setAdmission(InpatientAdmission admission) {
        this.admission = admission;
    }

    public LocalDateTime getRoundDateTime() {
        return roundDateTime;
    }

    public void setRoundDateTime(LocalDateTime roundDateTime) {
        this.roundDateTime = roundDateTime;
    }

    public String getSubjectiveNotes() {
        return subjectiveNotes;
    }

    public void setSubjectiveNotes(String subjectiveNotes) {
        this.subjectiveNotes = subjectiveNotes;
    }

    public String getObjectiveNotes() {
        return objectiveNotes;
    }

    public void setObjectiveNotes(String objectiveNotes) {
        this.objectiveNotes = objectiveNotes;
    }

    public String getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getMedicationChanges() {
        return medicationChanges;
    }

    public void setMedicationChanges(String medicationChanges) {
        this.medicationChanges = medicationChanges;
    }

    public String getDietaryChanges() {
        return dietaryChanges;
    }

    public void setDietaryChanges(String dietaryChanges) {
        this.dietaryChanges = dietaryChanges;
    }

    public String getActivityChanges() {
        return activityChanges;
    }

    public void setActivityChanges(String activityChanges) {
        this.activityChanges = activityChanges;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}