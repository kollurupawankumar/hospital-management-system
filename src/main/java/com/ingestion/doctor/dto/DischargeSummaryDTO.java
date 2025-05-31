package com.ingestion.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DischargeSummaryDTO {
    
    private Long id;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    private String patientName;
    
    private String doctorName;
    
    private LocalDate admissionDate;
    
    private LocalDate dischargeDate;
    
    @NotBlank(message = "Admission diagnosis is required")
    private String admissionDiagnosis;
    
    @NotBlank(message = "Discharge diagnosis is required")
    private String dischargeDiagnosis;
    
    private String briefHospitalCourse;
    
    private String proceduresPerformed;
    
    private String significantFindings;
    
    private String conditionAtDischarge;
    
    private String dischargeMedications;
    
    private String dietaryInstructions;
    
    private String activityInstructions;
    
    private String followUpInstructions;
    
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

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getAdmissionDiagnosis() {
        return admissionDiagnosis;
    }

    public void setAdmissionDiagnosis(String admissionDiagnosis) {
        this.admissionDiagnosis = admissionDiagnosis;
    }

    public String getDischargeDiagnosis() {
        return dischargeDiagnosis;
    }

    public void setDischargeDiagnosis(String dischargeDiagnosis) {
        this.dischargeDiagnosis = dischargeDiagnosis;
    }

    public String getBriefHospitalCourse() {
        return briefHospitalCourse;
    }

    public void setBriefHospitalCourse(String briefHospitalCourse) {
        this.briefHospitalCourse = briefHospitalCourse;
    }

    public String getProceduresPerformed() {
        return proceduresPerformed;
    }

    public void setProceduresPerformed(String proceduresPerformed) {
        this.proceduresPerformed = proceduresPerformed;
    }

    public String getSignificantFindings() {
        return significantFindings;
    }

    public void setSignificantFindings(String significantFindings) {
        this.significantFindings = significantFindings;
    }

    public String getConditionAtDischarge() {
        return conditionAtDischarge;
    }

    public void setConditionAtDischarge(String conditionAtDischarge) {
        this.conditionAtDischarge = conditionAtDischarge;
    }

    public String getDischargeMedications() {
        return dischargeMedications;
    }

    public void setDischargeMedications(String dischargeMedications) {
        this.dischargeMedications = dischargeMedications;
    }

    public String getDietaryInstructions() {
        return dietaryInstructions;
    }

    public void setDietaryInstructions(String dietaryInstructions) {
        this.dietaryInstructions = dietaryInstructions;
    }

    public String getActivityInstructions() {
        return activityInstructions;
    }

    public void setActivityInstructions(String activityInstructions) {
        this.activityInstructions = activityInstructions;
    }

    public String getFollowUpInstructions() {
        return followUpInstructions;
    }

    public void setFollowUpInstructions(String followUpInstructions) {
        this.followUpInstructions = followUpInstructions;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }
}