package com.ingestion.nursing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.common.model.inpatient.Department;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shift_handovers")
public class ShiftHandover extends BaseEntity {

    @Column(name = "handover_date", nullable = false)
    private LocalDateTime handoverDate;

    @Column(name = "shift_type")
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outgoing_nurse_id", nullable = false)
    private User outgoingNurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incoming_nurse_id", nullable = false)
    private User incomingNurse;

    @Column(name = "general_notes", length = 2000)
    private String generalNotes;

    @Column(name = "pending_tasks", length = 2000)
    private String pendingTasks;

    @Column(name = "critical_patients", length = 2000)
    private String criticalPatients;

    @Column(name = "medication_issues", length = 2000)
    private String medicationIssues;

    @Column(name = "equipment_issues", length = 2000)
    private String equipmentIssues;

    @Column(name = "staffing_issues", length = 2000)
    private String staffingIssues;

    @Column(name = "is_acknowledged")
    private Boolean isAcknowledged = false;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @OneToMany(mappedBy = "shiftHandover", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftHandoverPatient> patients = new ArrayList<>();

    public enum ShiftType {
        MORNING, AFTERNOON, NIGHT
    }

    // Getters and Setters
    public LocalDateTime getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(LocalDateTime handoverDate) {
        this.handoverDate = handoverDate;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public User getOutgoingNurse() {
        return outgoingNurse;
    }

    public void setOutgoingNurse(User outgoingNurse) {
        this.outgoingNurse = outgoingNurse;
    }

    public User getIncomingNurse() {
        return incomingNurse;
    }

    public void setIncomingNurse(User incomingNurse) {
        this.incomingNurse = incomingNurse;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public String getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(String pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public String getCriticalPatients() {
        return criticalPatients;
    }

    public void setCriticalPatients(String criticalPatients) {
        this.criticalPatients = criticalPatients;
    }

    public String getMedicationIssues() {
        return medicationIssues;
    }

    public void setMedicationIssues(String medicationIssues) {
        this.medicationIssues = medicationIssues;
    }

    public String getEquipmentIssues() {
        return equipmentIssues;
    }

    public void setEquipmentIssues(String equipmentIssues) {
        this.equipmentIssues = equipmentIssues;
    }

    public String getStaffingIssues() {
        return staffingIssues;
    }

    public void setStaffingIssues(String staffingIssues) {
        this.staffingIssues = staffingIssues;
    }

    public Boolean getIsAcknowledged() {
        return isAcknowledged;
    }

    public void setIsAcknowledged(Boolean acknowledged) {
        isAcknowledged = acknowledged;
    }

    public LocalDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public List<ShiftHandoverPatient> getPatients() {
        return patients;
    }

    public void setPatients(List<ShiftHandoverPatient> patients) {
        this.patients = patients;
    }

    // Helper methods
    public void addPatient(ShiftHandoverPatient patient) {
        patients.add(patient);
        patient.setShiftHandover(this);
    }

    public void removePatient(ShiftHandoverPatient patient) {
        patients.remove(patient);
        patient.setShiftHandover(null);
    }

    public void acknowledge() {
        this.isAcknowledged = true;
        this.acknowledgedAt = LocalDateTime.now();
    }
}