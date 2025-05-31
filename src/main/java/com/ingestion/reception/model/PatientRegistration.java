package com.ingestion.reception.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_registrations")
public class PatientRegistration extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;
    
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    
    @Column(name = "registered_by", nullable = false)
    private String registeredBy;
    
    @Column(name = "registration_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;
    
    @Column(name = "registration_notes")
    private String registrationNotes;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    public enum RegistrationType {
        NEW_PATIENT,
        RETURNING_PATIENT,
        EMERGENCY,
        REFERRAL
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public String getRegistrationNotes() {
        return registrationNotes;
    }

    public void setRegistrationNotes(String registrationNotes) {
        this.registrationNotes = registrationNotes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}