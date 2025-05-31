package com.ingestion.patient.model;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lab_reports")

public class LabReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "lab_name")
    private String labName;

    @Column(name = "technician_name")
    private String technicianName;

    @Column(name = "doctor_referral")
    private String doctorReferral;

    @Column(name = "report_summary", length = 1000)
    private String reportSummary;

    @Column(name = "report_file_path")
    private String reportFilePath;

    @Column(name = "is_abnormal")
    private Boolean isAbnormal = false;

    @Column(name = "is_viewed_by_patient")
    private Boolean isViewedByPatient = false;

    @Column(name = "is_viewed_by_doctor")
    private Boolean isViewedByDoctor = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "labReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabTestResult> testResults = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (reportDate == null) {
            reportDate = LocalDate.now();
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getDoctorReferral() {
        return doctorReferral;
    }

    public void setDoctorReferral(String doctorReferral) {
        this.doctorReferral = doctorReferral;
    }

    public String getReportSummary() {
        return reportSummary;
    }

    public void setReportSummary(String reportSummary) {
        this.reportSummary = reportSummary;
    }

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean abnormal) {
        isAbnormal = abnormal;
    }

    public Boolean getIsViewedByPatient() {
        return isViewedByPatient;
    }

    public void setIsViewedByPatient(Boolean viewedByPatient) {
        isViewedByPatient = viewedByPatient;
    }

    public Boolean getIsViewedByDoctor() {
        return isViewedByDoctor;
    }

    public void setIsViewedByDoctor(Boolean viewedByDoctor) {
        isViewedByDoctor = viewedByDoctor;
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

    public List<LabTestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<LabTestResult> testResults) {
        this.testResults = testResults;
    }
}