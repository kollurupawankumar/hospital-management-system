package com.ingestion.nursing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Medication;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.pharmacy.model.PrescriptionItem;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medication_administrations")
public class MedicationAdministration extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_item_id")
    private PrescriptionItem prescriptionItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @Column(name = "route", nullable = false)
    private String route;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(name = "administered_time")
    private LocalDateTime administeredTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AdministrationStatus status = AdministrationStatus.SCHEDULED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administered_by")
    private User administeredBy;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "reason_not_given", length = 500)
    private String reasonNotGiven;

    @Column(name = "witness_required")
    private Boolean witnessRequired = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "witnessed_by")
    private User witnessedBy;

    @Column(name = "is_prn")
    private Boolean isPrn = false;

    @Column(name = "is_stat")
    private Boolean isStat = false;

    public enum AdministrationStatus {
        SCHEDULED, ADMINISTERED, MISSED, REFUSED, HELD, CANCELLED
    }

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public PrescriptionItem getPrescriptionItem() {
        return prescriptionItem;
    }

    public void setPrescriptionItem(PrescriptionItem prescriptionItem) {
        this.prescriptionItem = prescriptionItem;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getAdministeredTime() {
        return administeredTime;
    }

    public void setAdministeredTime(LocalDateTime administeredTime) {
        this.administeredTime = administeredTime;
    }

    public AdministrationStatus getStatus() {
        return status;
    }

    public void setStatus(AdministrationStatus status) {
        this.status = status;
    }

    public User getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(User administeredBy) {
        this.administeredBy = administeredBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReasonNotGiven() {
        return reasonNotGiven;
    }

    public void setReasonNotGiven(String reasonNotGiven) {
        this.reasonNotGiven = reasonNotGiven;
    }

    public Boolean getWitnessRequired() {
        return witnessRequired;
    }

    public void setWitnessRequired(Boolean witnessRequired) {
        this.witnessRequired = witnessRequired;
    }

    public User getWitnessedBy() {
        return witnessedBy;
    }

    public void setWitnessedBy(User witnessedBy) {
        this.witnessedBy = witnessedBy;
    }

    public Boolean getIsPrn() {
        return isPrn;
    }

    public void setIsPrn(Boolean prn) {
        isPrn = prn;
    }

    public Boolean getIsStat() {
        return isStat;
    }

    public void setIsStat(Boolean stat) {
        isStat = stat;
    }

    // Helper methods
    public void administer(User nurse, LocalDateTime time, String notes) {
        this.administeredBy = nurse;
        this.administeredTime = time;
        this.notes = notes;
        this.status = AdministrationStatus.ADMINISTERED;
    }

    public void markAsMissed(String reason) {
        this.status = AdministrationStatus.MISSED;
        this.reasonNotGiven = reason;
    }

    public void markAsRefused(String reason) {
        this.status = AdministrationStatus.REFUSED;
        this.reasonNotGiven = reason;
    }

    public void holdMedication(String reason) {
        this.status = AdministrationStatus.HELD;
        this.reasonNotGiven = reason;
    }

    public void cancelMedication(String reason) {
        this.status = AdministrationStatus.CANCELLED;
        this.reasonNotGiven = reason;
    }

    public void addWitness(User witness) {
        this.witnessedBy = witness;
    }

    public boolean isAdministered() {
        return this.status == AdministrationStatus.ADMINISTERED;
    }

    public boolean isPending() {
        return this.status == AdministrationStatus.SCHEDULED;
    }

    public boolean isDue(int minutesThreshold) {
        if (this.status != AdministrationStatus.SCHEDULED) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdTime = this.scheduledTime.minusMinutes(minutesThreshold);
        
        return now.isAfter(thresholdTime) || now.isEqual(thresholdTime);
    }

    public boolean isOverdue() {
        if (this.status != AdministrationStatus.SCHEDULED) {
            return false;
        }
        
        return LocalDateTime.now().isAfter(this.scheduledTime);
    }
}