package com.ingestion.common.laboratory;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Consolidated LabOrder entity that combines functionality from both
 * doctor and laboratory packages.
 */
@Entity
@Table(name = "lab_orders")
public class LabOrder extends BaseEntity {

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

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.ORDERED;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private OrderPriority priority = OrderPriority.ROUTINE;

    @Column(name = "clinical_notes", length = 1000)
    private String clinicalNotes;

    @Column(name = "special_instructions", length = 500)
    private String specialInstructions;

    @Column(name = "sample_collection_date")
    private LocalDateTime sampleCollectionDate;

    @Column(name = "result_date")
    private LocalDateTime resultDate;

    @Column(name = "is_billed")
    private Boolean isBilled = false;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private Doctor cancelledBy;

    @OneToMany(mappedBy = "labOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabOrderTest> labTests = new ArrayList<>();

    public enum OrderStatus {
        ORDERED, SAMPLE_COLLECTED, IN_PROCESS, COMPLETED, CANCELLED, REJECTED
    }

    public enum OrderPriority {
        ROUTINE, URGENT, STAT
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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderPriority getPriority() {
        return priority;
    }

    public void setPriority(OrderPriority priority) {
        this.priority = priority;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public LocalDateTime getSampleCollectionDate() {
        return sampleCollectionDate;
    }

    public void setSampleCollectionDate(LocalDateTime sampleCollectionDate) {
        this.sampleCollectionDate = sampleCollectionDate;
    }

    public LocalDateTime getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDateTime resultDate) {
        this.resultDate = resultDate;
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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public Doctor getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(Doctor cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public List<LabOrderTest> getLabTests() {
        return labTests;
    }

    public void setLabTests(List<LabOrderTest> labTests) {
        this.labTests = labTests;
    }

    // Helper methods
    public void addLabTest(LabOrderTest test) {
        labTests.add(test);
        test.setLabOrder(this);
    }

    public void removeLabTest(LabOrderTest test) {
        labTests.remove(test);
        test.setLabOrder(null);
    }

    public void markAsSampleCollected() {
        this.status = OrderStatus.SAMPLE_COLLECTED;
        this.sampleCollectionDate = LocalDateTime.now();
    }

    public void markAsInProcess() {
        this.status = OrderStatus.IN_PROCESS;
    }

    public void markAsCompleted() {
        this.status = OrderStatus.COMPLETED;
        this.resultDate = LocalDateTime.now();
    }

    public void markAsCancelled(Doctor cancelledBy, String reason) {
        this.status = OrderStatus.CANCELLED;
        this.cancelledBy = cancelledBy;
        this.cancellationReason = reason;
        this.cancellationDate = LocalDateTime.now();
    }

    public void markAsBilled() {
        this.isBilled = true;
    }

    public void markAsPaid() {
        this.isPaid = true;
    }
}