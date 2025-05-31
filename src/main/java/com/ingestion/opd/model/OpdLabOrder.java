package com.ingestion.opd.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "opd_lab_orders")
public class OpdLabOrder extends BaseEntity {

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opd_visit_id", nullable = false)
    private OpdVisit opdVisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_category")
    private String testCategory;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "result")
    private String result;

    @Column(name = "result_notes", length = 1000)
    private String resultNotes;

    @Column(name = "instructions", length = 1000)
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LabOrderStatus status = LabOrderStatus.ORDERED;

    @Column(name = "is_urgent")
    private Boolean isUrgent = false;

    public enum LabOrderStatus {
        ORDERED, SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (orderNumber == null || orderNumber.isEmpty()) {
            // Generate order number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            orderNumber = "LAB-" + datePrefix + "-" + System.nanoTime() % 10000;
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

    public OpdVisit getOpdVisit() {
        return opdVisit;
    }

    public void setOpdVisit(OpdVisit opdVisit) {
        this.opdVisit = opdVisit;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestCategory() {
        return testCategory;
    }

    public void setTestCategory(String testCategory) {
        this.testCategory = testCategory;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNotes() {
        return resultNotes;
    }

    public void setResultNotes(String resultNotes) {
        this.resultNotes = resultNotes;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public LabOrderStatus getStatus() {
        return status;
    }

    public void setStatus(LabOrderStatus status) {
        this.status = status;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    // Helper methods
    public void schedule(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
        this.status = LabOrderStatus.SCHEDULED;
    }

    public void startProcessing() {
        this.status = LabOrderStatus.IN_PROGRESS;
    }

    public void complete(String result, String resultNotes) {
        this.result = result;
        this.resultNotes = resultNotes;
        this.completedDate = LocalDateTime.now();
        this.status = LabOrderStatus.COMPLETED;
    }

    public void cancel() {
        this.status = LabOrderStatus.CANCELLED;
    }
}