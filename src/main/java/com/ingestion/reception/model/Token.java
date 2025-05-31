package com.ingestion.reception.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.patient.model.Patient;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(name = "token_number", nullable = false)
    private String tokenNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @Column(name = "token_date", nullable = false)
    private LocalDate tokenDate;
    
    @Column(name = "issue_time", nullable = false)
    private LocalDateTime issueTime;
    
    @Column(name = "expected_service_time")
    private LocalDateTime expectedServiceTime;
    
    @Column(name = "actual_service_time")
    private LocalDateTime actualServiceTime;
    
    @Column(name = "completion_time")
    private LocalDateTime completionTime;
    
    @Column(name = "token_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    
    @Column(name = "token_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus = TokenStatus.WAITING;
    
    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenPriority priority = TokenPriority.NORMAL;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "counter_number")
    private String counterNumber;
    
    @Column(name = "notes")
    private String notes;
    
    public enum TokenType {
        WALK_IN,
        APPOINTMENT,
        EMERGENCY,
        FOLLOW_UP,
        LAB_TEST,
        PHARMACY
    }
    
    public enum TokenStatus {
        WAITING,
        CALLED,
        IN_PROGRESS,
        COMPLETED,
        NO_SHOW,
        CANCELLED,
        TRANSFERRED
    }
    
    public enum TokenPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
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

    public LocalDate getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(LocalDate tokenDate) {
        this.tokenDate = tokenDate;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public LocalDateTime getExpectedServiceTime() {
        return expectedServiceTime;
    }

    public void setExpectedServiceTime(LocalDateTime expectedServiceTime) {
        this.expectedServiceTime = expectedServiceTime;
    }

    public LocalDateTime getActualServiceTime() {
        return actualServiceTime;
    }

    public void setActualServiceTime(LocalDateTime actualServiceTime) {
        this.actualServiceTime = actualServiceTime;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public TokenStatus getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public TokenPriority getPriority() {
        return priority;
    }

    public void setPriority(TokenPriority priority) {
        this.priority = priority;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(String counterNumber) {
        this.counterNumber = counterNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}