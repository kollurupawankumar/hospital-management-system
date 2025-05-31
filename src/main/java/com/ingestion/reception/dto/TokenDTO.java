package com.ingestion.reception.dto;

import com.ingestion.reception.model.Token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TokenDTO {
    
    private Long id;
    
    private String tokenNumber;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private String patientName;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    private String doctorName;
    
    private LocalDate tokenDate;
    
    private LocalDateTime issueTime;
    
    private LocalDateTime expectedServiceTime;
    
    private LocalDateTime actualServiceTime;
    
    private LocalDateTime completionTime;
    
    @NotNull(message = "Token type is required")
    private Token.TokenType tokenType;
    
    private Token.TokenStatus tokenStatus = Token.TokenStatus.WAITING;
    
    private Token.TokenPriority priority = Token.TokenPriority.NORMAL;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    private String counterNumber;
    
    private String notes;
    
    // Additional fields for display
    private String estimatedWaitTime;
    private Integer queuePosition;
    private String statusDisplay;
    private String priorityDisplay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public Token.TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(Token.TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Token.TokenStatus getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(Token.TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public Token.TokenPriority getPriority() {
        return priority;
    }

    public void setPriority(Token.TokenPriority priority) {
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

    public String getEstimatedWaitTime() {
        return estimatedWaitTime;
    }

    public void setEstimatedWaitTime(String estimatedWaitTime) {
        this.estimatedWaitTime = estimatedWaitTime;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getPriorityDisplay() {
        return priorityDisplay;
    }

    public void setPriorityDisplay(String priorityDisplay) {
        this.priorityDisplay = priorityDisplay;
    }
}