package com.ingestion.radiology.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "radiology_schedules")
public class RadiologySchedule extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiology_order_id", nullable = false)
    private RadiologyOrder radiologyOrder;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RadiologyRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User technician;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiologist_id")
    private User radiologist;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status = ScheduleStatus.SCHEDULED;

    @Column(name = "patient_arrived")
    private Boolean patientArrived = false;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "procedure_start_time")
    private LocalDateTime procedureStartTime;

    @Column(name = "procedure_end_time")
    private LocalDateTime procedureEndTime;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    public enum ScheduleStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    }

    // Getters and Setters
    public RadiologyOrder getRadiologyOrder() {
        return radiologyOrder;
    }

    public void setRadiologyOrder(RadiologyOrder radiologyOrder) {
        this.radiologyOrder = radiologyOrder;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public RadiologyRoom getRoom() {
        return room;
    }

    public void setRoom(RadiologyRoom room) {
        this.room = room;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }

    public User getRadiologist() {
        return radiologist;
    }

    public void setRadiologist(User radiologist) {
        this.radiologist = radiologist;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public Boolean getPatientArrived() {
        return patientArrived;
    }

    public void setPatientArrived(Boolean patientArrived) {
        this.patientArrived = patientArrived;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getProcedureStartTime() {
        return procedureStartTime;
    }

    public void setProcedureStartTime(LocalDateTime procedureStartTime) {
        this.procedureStartTime = procedureStartTime;
    }

    public LocalDateTime getProcedureEndTime() {
        return procedureEndTime;
    }

    public void setProcedureEndTime(LocalDateTime procedureEndTime) {
        this.procedureEndTime = procedureEndTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    // Helper methods
    public void markPatientArrived() {
        this.patientArrived = true;
        this.arrivalTime = LocalDateTime.now();
    }

    public void startProcedure() {
        this.status = ScheduleStatus.IN_PROGRESS;
        this.procedureStartTime = LocalDateTime.now();
        
        // Update the order status
        if (this.radiologyOrder != null) {
            this.radiologyOrder.startProcedure();
        }
    }

    public void completeProcedure() {
        this.status = ScheduleStatus.COMPLETED;
        this.procedureEndTime = LocalDateTime.now();
    }

    public void cancel(String reason) {
        this.status = ScheduleStatus.CANCELLED;
        this.cancellationReason = reason;
        
        // Update the order status
        if (this.radiologyOrder != null) {
            this.radiologyOrder.cancel();
        }
    }

    public void markNoShow() {
        this.status = ScheduleStatus.NO_SHOW;
        
        // Update the order status
        if (this.radiologyOrder != null) {
            this.radiologyOrder.cancel();
        }
    }
}