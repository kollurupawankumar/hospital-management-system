package com.ingestion.reception.dto;

import com.ingestion.reception.model.VisitorPass;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;


public class VisitorPassDTO {
    
    private Long id;
    
    private String passNumber;
    
    @NotBlank(message = "Visitor name is required")
    private String visitorName;
    
    @NotBlank(message = "Visitor phone is required")
    private String visitorPhone;
    
    private String visitorIdType;
    
    private String visitorIdNumber;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private String patientName;
    
    private String relationshipToPatient;
    
    private LocalDateTime issueTime;
    
    private LocalDateTime expiryTime;
    
    private LocalDateTime checkInTime;
    
    private LocalDateTime checkOutTime;
    
    @NotBlank(message = "Issued by is required")
    private String issuedBy;
    
    private VisitorPass.PassStatus passStatus = VisitorPass.PassStatus.ACTIVE;
    
    private String wardNumber;
    
    private String roomNumber;
    
    @NotBlank(message = "Purpose is required")
    private String purpose;
    
    private String notes;
    
    // Additional fields for display
    private String statusDisplay;
    private String validityPeriod;
    private boolean isExpired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassNumber() {
        return passNumber;
    }

    public void setPassNumber(String passNumber) {
        this.passNumber = passNumber;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorIdType() {
        return visitorIdType;
    }

    public void setVisitorIdType(String visitorIdType) {
        this.visitorIdType = visitorIdType;
    }

    public String getVisitorIdNumber() {
        return visitorIdNumber;
    }

    public void setVisitorIdNumber(String visitorIdNumber) {
        this.visitorIdNumber = visitorIdNumber;
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

    public String getRelationshipToPatient() {
        return relationshipToPatient;
    }

    public void setRelationshipToPatient(String relationshipToPatient) {
        this.relationshipToPatient = relationshipToPatient;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public VisitorPass.PassStatus getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(VisitorPass.PassStatus passStatus) {
        this.passStatus = passStatus;
    }

    public String getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}