package com.ingestion.doctor.model;

import com.ingestion.common.model.inpatient.InpatientAdmission;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "discharge_summaries")

public class DischargeSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id")
    private InpatientAdmission admission;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Column(name = "discharge_date", nullable = false)
    private LocalDate dischargeDate;

    @Column(name = "admission_diagnosis", nullable = false, length = 1000)
    private String admissionDiagnosis;

    @Column(name = "discharge_diagnosis", nullable = false, length = 1000)
    private String dischargeDiagnosis;

    @Column(name = "brief_hospital_course", length = 2000)
    private String briefHospitalCourse;

    @Column(name = "procedures_performed", length = 1000)
    private String proceduresPerformed;

    @Column(name = "significant_findings", length = 1000)
    private String significantFindings;

    @Column(name = "condition_at_discharge", length = 500)
    private String conditionAtDischarge;

    @Column(name = "discharge_medications", length = 1000)
    private String dischargeMedications;

    @Column(name = "dietary_instructions", length = 500)
    private String dietaryInstructions;

    @Column(name = "activity_instructions", length = 500)
    private String activityInstructions;

    @Column(name = "follow_up_instructions", length = 1000)
    private String followUpInstructions;

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
        if (dischargeDate == null) {
            dischargeDate = LocalDate.now();
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