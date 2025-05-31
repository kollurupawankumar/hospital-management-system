package com.ingestion.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;


public class RoundNoteDTO {
    
    private Long id;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    private String patientName;
    
    private String doctorName;
    
    private LocalDateTime roundDateTime;
    
    @NotBlank(message = "Subjective notes are required")
    private String subjectiveNotes;
    
    private String objectiveNotes;
    
    private String vitalSigns;
    
    private String assessment;
    
    private String plan;
    
    private String medicationChanges;
    
    private String dietaryChanges;
    
    private String activityChanges;
    
    private String additionalInstructions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
}