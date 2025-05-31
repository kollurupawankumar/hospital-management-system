package com.ingestion.nursing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;

@Entity
@Table(name = "shift_handover_patients")
public class ShiftHandoverPatient extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_handover_id", nullable = false)
    private ShiftHandover shiftHandover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "current_status", length = 500)
    private String currentStatus;

    @Column(name = "care_plan_updates", length = 1000)
    private String carePlanUpdates;

    @Column(name = "medication_updates", length = 1000)
    private String medicationUpdates;

    @Column(name = "vital_signs_updates", length = 500)
    private String vitalSignsUpdates;

    @Column(name = "pending_tasks", length = 1000)
    private String pendingTasks;

    @Column(name = "special_instructions", length = 1000)
    private String specialInstructions;

    @Column(name = "is_critical")
    private Boolean isCritical = false;

    // Getters and Setters
    public ShiftHandover getShiftHandover() {
        return shiftHandover;
    }

    public void setShiftHandover(ShiftHandover shiftHandover) {
        this.shiftHandover = shiftHandover;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCarePlanUpdates() {
        return carePlanUpdates;
    }

    public void setCarePlanUpdates(String carePlanUpdates) {
        this.carePlanUpdates = carePlanUpdates;
    }

    public String getMedicationUpdates() {
        return medicationUpdates;
    }

    public void setMedicationUpdates(String medicationUpdates) {
        this.medicationUpdates = medicationUpdates;
    }

    public String getVitalSignsUpdates() {
        return vitalSignsUpdates;
    }

    public void setVitalSignsUpdates(String vitalSignsUpdates) {
        this.vitalSignsUpdates = vitalSignsUpdates;
    }

    public String getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(String pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Boolean getIsCritical() {
        return isCritical;
    }

    public void setIsCritical(Boolean critical) {
        isCritical = critical;
    }

    // Helper methods
    public void markAsCritical() {
        this.isCritical = true;
    }

    public void markAsNonCritical() {
        this.isCritical = false;
    }
}