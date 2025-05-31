package com.ingestion.common.model.inpatient;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "department_transfers")
public class DepartmentTransfer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private InpatientAdmission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_department_id", nullable = false)
    private Department fromDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_department_id", nullable = false)
    private Department toDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_bed_id", nullable = false)
    private Bed fromBed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_bed_id", nullable = false)
    private Bed toBed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordered_by_doctor_id", nullable = false)
    private Doctor orderedBy;

    @NotNull(message = "Transfer date is required")
    @Column(name = "transfer_date", nullable = false)
    private LocalDateTime transferDate;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferStatus status = TransferStatus.PENDING;

    @PrePersist
    protected void onCreate() {
        if (transferDate == null) {
            transferDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // If transfer is completed, update bed statuses
        if (status == TransferStatus.COMPLETED) {
            fromBed.setStatus(Bed.BedStatus.CLEANING);
            toBed.setStatus(Bed.BedStatus.OCCUPIED);
        }
    }

    public enum TransferStatus {
        PENDING, COMPLETED, CANCELLED
    }

    // Getters and Setters
    public InpatientAdmission getAdmission() {
        return admission;
    }

    public void setAdmission(InpatientAdmission admission) {
        this.admission = admission;
    }

    public Department getFromDepartment() {
        return fromDepartment;
    }

    public void setFromDepartment(Department fromDepartment) {
        this.fromDepartment = fromDepartment;
    }

    public Department getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(Department toDepartment) {
        this.toDepartment = toDepartment;
    }

    public Bed getFromBed() {
        return fromBed;
    }

    public void setFromBed(Bed fromBed) {
        this.fromBed = fromBed;
    }

    public Bed getToBed() {
        return toBed;
    }

    public void setToBed(Bed toBed) {
        this.toBed = toBed;
    }

    public Doctor getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Doctor orderedBy) {
        this.orderedBy = orderedBy;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    // Helper method to complete transfer
    public void completeTransfer() {
        this.status = TransferStatus.COMPLETED;
        this.fromBed.setStatus(Bed.BedStatus.CLEANING);
        this.toBed.setStatus(Bed.BedStatus.OCCUPIED);
        
        // Update the admission's assigned bed
        this.admission.setAssignedBed(this.toBed);
    }
}