package com.ingestion.radiology.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "radiology_orders")
public class RadiologyOrder extends BaseEntity {

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opd_visit_id")
    private OpdVisit opdVisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id", nullable = false)
    private RadiologyProcedure procedure;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "clinical_history", length = 1000)
    private String clinicalHistory;

    @Column(name = "clinical_diagnosis", length = 500)
    private String clinicalDiagnosis;

    @Column(name = "special_instructions", length = 1000)
    private String specialInstructions;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private OrderPriority priority = OrderPriority.ROUTINE;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.ORDERED;

    @Column(name = "is_billed")
    private Boolean isBilled = false;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @OneToOne(mappedBy = "radiologyOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private RadiologyReport report;

    public enum OrderPriority {
        ROUTINE, URGENT, STAT
    }

    public enum OrderStatus {
        ORDERED, SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, REJECTED
    }

    @PrePersist
    protected void onCreate() {
        if (orderNumber == null || orderNumber.isEmpty()) {
            // Generate order number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            orderNumber = "RAD-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public OpdVisit getOpdVisit() {
        return opdVisit;
    }

    public void setOpdVisit(OpdVisit opdVisit) {
        this.opdVisit = opdVisit;
    }

    public RadiologyProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(RadiologyProcedure procedure) {
        this.procedure = procedure;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public String getClinicalHistory() {
        return clinicalHistory;
    }

    public void setClinicalHistory(String clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
    }

    public String getClinicalDiagnosis() {
        return clinicalDiagnosis;
    }

    public void setClinicalDiagnosis(String clinicalDiagnosis) {
        this.clinicalDiagnosis = clinicalDiagnosis;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public OrderPriority getPriority() {
        return priority;
    }

    public void setPriority(OrderPriority priority) {
        this.priority = priority;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Boolean getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(Boolean billed) {
        isBilled = billed;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean paid) {
        isPaid = paid;
    }

    public RadiologyReport getReport() {
        return report;
    }

    public void setReport(RadiologyReport report) {
        this.report = report;
    }

    // Helper methods
    public void schedule(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
        this.status = OrderStatus.SCHEDULED;
    }

    public void startProcedure() {
        this.status = OrderStatus.IN_PROGRESS;
    }

    public void completeProcedure() {
        this.completedDate = LocalDateTime.now();
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public void reject() {
        this.status = OrderStatus.REJECTED;
    }

    public void markAsBilled() {
        this.isBilled = true;
    }

    public void markAsPaid() {
        this.isPaid = true;
    }
}