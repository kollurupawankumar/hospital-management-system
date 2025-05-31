package com.ingestion.reception.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "visitor_passes")
public class VisitorPass extends BaseEntity {

    @Column(name = "pass_number", nullable = false, unique = true)
    private String passNumber;
    
    @Column(name = "visitor_name", nullable = false)
    private String visitorName;
    
    @Column(name = "visitor_phone", nullable = false)
    private String visitorPhone;
    
    @Column(name = "visitor_id_type")
    private String visitorIdType;
    
    @Column(name = "visitor_id_number")
    private String visitorIdNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @Column(name = "relationship_to_patient")
    private String relationshipToPatient;
    
    @Column(name = "issue_time", nullable = false)
    private LocalDateTime issueTime;
    
    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;
    
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;
    
    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;
    
    @Column(name = "issued_by", nullable = false)
    private String issuedBy;
    
    @Column(name = "pass_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PassStatus passStatus = PassStatus.ACTIVE;
    
    @Column(name = "ward_number")
    private String wardNumber;
    
    @Column(name = "room_number")
    private String roomNumber;
    
    @Column(name = "purpose", nullable = false)
    private String purpose;
    
    @Column(name = "notes")
    private String notes;
    
    public enum PassStatus {
        ACTIVE,
        CHECKED_IN,
        CHECKED_OUT,
        EXPIRED,
        CANCELLED
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    public PassStatus getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(PassStatus passStatus) {
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
}