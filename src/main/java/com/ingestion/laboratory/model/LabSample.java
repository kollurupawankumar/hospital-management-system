package com.ingestion.laboratory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.security.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_samples")
public class LabSample extends BaseEntity {

    @Column(name = "sample_id", nullable = false, unique = true)
    private String sampleId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @Column(name = "sample_type")
    private String sampleType;

    @Column(name = "container_type")
    private String containerType;

    @Column(name = "volume")
    private String volume;

    @Column(name = "collection_date")
    private LocalDateTime collectionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collected_by")
    private User collectedBy;

    @Column(name = "collection_notes", length = 500)
    private String collectionNotes;

    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by")
    private User receivedBy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SampleStatus status = SampleStatus.PENDING;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "storage_location")
    private String storageLocation;

    public enum SampleStatus {
        PENDING, COLLECTED, RECEIVED, IN_PROCESS, PROCESSED, REJECTED
    }

    @PrePersist
    protected void onCreate() {
        if (sampleId == null || sampleId.isEmpty()) {
            // Generate sample ID
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            sampleId = "S-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
    }

    // Getters and Setters
    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public LabOrder getLabOrder() {
        return labOrder;
    }

    public void setLabOrder(LabOrder labOrder) {
        this.labOrder = labOrder;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public LocalDateTime getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDateTime collectionDate) {
        this.collectionDate = collectionDate;
    }

    public User getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(User collectedBy) {
        this.collectedBy = collectedBy;
    }

    public String getCollectionNotes() {
        return collectionNotes;
    }

    public void setCollectionNotes(String collectionNotes) {
        this.collectionNotes = collectionNotes;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public User getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(User receivedBy) {
        this.receivedBy = receivedBy;
    }

    public SampleStatus getStatus() {
        return status;
    }

    public void setStatus(SampleStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    // Helper methods
    public void markAsCollected(User collectedBy, LocalDateTime collectionDate, String notes) {
        this.collectedBy = collectedBy;
        this.collectionDate = collectionDate;
        this.collectionNotes = notes;
        this.status = SampleStatus.COLLECTED;
        
        // Update the lab order status
        if (this.labOrder != null) {
            this.labOrder.markAsSampleCollected();
        }
    }

    public void markAsReceived(User receivedBy, LocalDateTime receivedDate) {
        this.receivedBy = receivedBy;
        this.receivedDate = receivedDate;
        this.status = SampleStatus.RECEIVED;
    }

    public void markAsInProcess() {
        this.status = SampleStatus.IN_PROCESS;
        
        // Update the lab order status
        if (this.labOrder != null) {
            this.labOrder.markAsInProcess();
        }
    }

    public void markAsProcessed() {
        this.status = SampleStatus.PROCESSED;
    }

    public void markAsRejected(String rejectionReason) {
        this.status = SampleStatus.REJECTED;
        this.rejectionReason = rejectionReason;
        
        // Update the lab order status
        // Note: The common LabOrder class might not have this method, so we're removing it
        // If needed, this should be handled at the service layer
    }
}