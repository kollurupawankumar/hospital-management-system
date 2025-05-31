package com.ingestion.inventory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_transfers")
public class StockTransfer extends BaseEntity {

    @Column(name = "transfer_number", nullable = false, unique = true)
    private String transferNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_store_id", nullable = false)
    private Store sourceStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_store_id", nullable = false)
    private Store destinationStore;

    @Column(name = "transfer_date", nullable = false)
    private LocalDateTime transferDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransferStatus status = TransferStatus.PENDING;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private User completedBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;

    @OneToMany(mappedBy = "stockTransfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockTransferItem> items = new ArrayList<>();

    public enum TransferStatus {
        PENDING, APPROVED, IN_TRANSIT, COMPLETED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (transferNumber == null || transferNumber.isEmpty()) {
            // Generate transfer number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            transferNumber = "TRF-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
        if (transferDate == null) {
            transferDate = LocalDateTime.now();
        }
        createdDate = LocalDateTime.now();
        lastModifiedDate = LocalDateTime.now();
        
        // Set the string version of creator for BaseEntity
        if (creator != null) {
            setCreatedBy(creator.getUsername());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public Store getSourceStore() {
        return sourceStore;
    }

    public void setSourceStore(Store sourceStore) {
        this.sourceStore = sourceStore;
    }

    public Store getDestinationStore() {
        return destinationStore;
    }

    public void setDestinationStore(Store destinationStore) {
        this.destinationStore = destinationStore;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean cancelled) {
        isCancelled = cancelled;
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

    public User getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(User cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public User getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(User completedBy) {
        this.completedBy = completedBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        // Also set the string version for BaseEntity
        setCreatedBy(creator != null ? creator.getUsername() : null);
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public List<StockTransferItem> getItems() {
        return items;
    }

    public void setItems(List<StockTransferItem> items) {
        this.items = items;
    }

    // Helper methods
    public void addItem(StockTransferItem item) {
        items.add(item);
        item.setStockTransfer(this);
    }

    public void removeItem(StockTransferItem item) {
        items.remove(item);
        item.setStockTransfer(null);
    }

    public void approve(User approver) {
        if (this.status == TransferStatus.PENDING) {
            this.status = TransferStatus.APPROVED;
            this.approvalDate = LocalDateTime.now();
            this.approvedBy = approver;
        } else {
            throw new IllegalStateException("Stock transfer must be in PENDING status to approve");
        }
    }

    public void markInTransit() {
        if (this.status == TransferStatus.APPROVED) {
            this.status = TransferStatus.IN_TRANSIT;
        } else {
            throw new IllegalStateException("Stock transfer must be in APPROVED status to mark as in transit");
        }
    }

    public void complete(User completedBy) {
        if (this.status == TransferStatus.IN_TRANSIT || this.status == TransferStatus.APPROVED) {
            this.status = TransferStatus.COMPLETED;
            this.completionDate = LocalDateTime.now();
            this.completedBy = completedBy;
        } else {
            throw new IllegalStateException("Stock transfer must be in IN_TRANSIT or APPROVED status to complete");
        }
    }

    public void cancel(User cancelledBy, String reason) {
        if (this.status != TransferStatus.COMPLETED) {
            this.isCancelled = true;
            this.status = TransferStatus.CANCELLED;
            this.cancelledBy = cancelledBy;
            this.cancellationReason = reason;
            this.cancellationDate = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Cannot cancel a completed stock transfer");
        }
    }

    public boolean isPending() {
        return this.status == TransferStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == TransferStatus.APPROVED;
    }

    public boolean isInTransit() {
        return this.status == TransferStatus.IN_TRANSIT;
    }

    public boolean isCompleted() {
        return this.status == TransferStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return this.status == TransferStatus.CANCELLED;
    }
}