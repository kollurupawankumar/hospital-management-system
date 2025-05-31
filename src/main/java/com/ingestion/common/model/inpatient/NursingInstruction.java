package com.ingestion.common.model.inpatient;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "nursing_instructions")
public class NursingInstruction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private InpatientAdmission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor orderedBy;

    @NotNull(message = "Instruction date is required")
    @Column(name = "instruction_date", nullable = false)
    private LocalDateTime instructionDate;

    @NotBlank(message = "Instruction is required")
    @Column(name = "instruction", nullable = false, length = 1000)
    private String instruction;

    @Enumerated(EnumType.STRING)
    @Column(name = "instruction_type", nullable = false)
    private InstructionType instructionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.NORMAL;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InstructionStatus status = InstructionStatus.ACTIVE;

    @Column(name = "notes", length = 500)
    private String notes;

    @PrePersist
    protected void onCreate() {
        if (instructionDate == null) {
            instructionDate = LocalDateTime.now();
        }
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }
    }

    public enum InstructionType {
        MEDICATION, MONITORING, ACTIVITY, DIET, WOUND_CARE, HYGIENE, RESPIRATORY, PAIN_MANAGEMENT, IV_THERAPY, OTHER
    }

    public enum Priority {
        LOW, NORMAL, HIGH, URGENT
    }

    public enum InstructionStatus {
        ACTIVE, COMPLETED, CANCELLED, PENDING
    }

    // Getters and Setters
    public InpatientAdmission getAdmission() {
        return admission;
    }

    public void setAdmission(InpatientAdmission admission) {
        this.admission = admission;
    }

    public Doctor getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Doctor orderedBy) {
        this.orderedBy = orderedBy;
    }

    public LocalDateTime getInstructionDate() {
        return instructionDate;
    }

    public void setInstructionDate(LocalDateTime instructionDate) {
        this.instructionDate = instructionDate;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(InstructionType instructionType) {
        this.instructionType = instructionType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public InstructionStatus getStatus() {
        return status;
    }

    public void setStatus(InstructionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}