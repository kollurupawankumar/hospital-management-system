package com.ingestion.common.model.inventory;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.inventory.model.Store;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_adjustments")
public class StockAdjustment extends BaseEntity {

    @Column(name = "adjustment_number", nullable = false, unique = true)
    private String adjustmentNumber;

    @Column(name = "adjustment_date", nullable = false)
    private LocalDateTime adjustmentDate;

    @Column(name = "adjustment_type")
    @Enumerated(EnumType.STRING)
    private AdjustmentType adjustmentType;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AdjustmentStatus status = AdjustmentStatus.PENDING;

    @Column(name = "module_type")
    @Enumerated(EnumType.STRING)
    private ModuleType moduleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store; // For inventory module

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user")
    private User createdByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @OneToMany(mappedBy = "stockAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockAdjustmentItem> items = new ArrayList<>();

    public enum AdjustmentType {
        ADDITION, REDUCTION, SUBTRACTION, DAMAGE, EXPIRY, LOSS, THEFT, RETURN, TRANSFER, OTHER
    }

    public enum AdjustmentStatus {
        PENDING, APPROVED, COMPLETED, REJECTED, CANCELLED
    }

    public enum ModuleType {
        PHARMACY, INVENTORY
    }

    @PrePersist
    protected void onCreate() {
        if (adjustmentNumber == null || adjustmentNumber.isEmpty()) {
            // Generate adjustment number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            adjustmentNumber = "ADJ-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
        if (adjustmentDate == null) {
            adjustmentDate = LocalDateTime.now();
        }
        
        // Set the string version of creator for BaseEntity
        if (createdByUser != null) {
            setCreatedBy(createdByUser.getUsername());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Set the string version of last modified by for BaseEntity
        if (createdByUser != null) {
            setUpdatedBy(createdByUser.getUsername());
        }
    }

    // Getters and Setters
    public String getAdjustmentNumber() {
        return adjustmentNumber;
    }

    public void setAdjustmentNumber(String adjustmentNumber) {
        this.adjustmentNumber = adjustmentNumber;
    }

    public LocalDateTime getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(LocalDateTime adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public AdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(AdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
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

    public AdjustmentStatus getStatus() {
        return status;
    }

    public void setStatus(AdjustmentStatus status) {
        this.status = status;
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(ModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
        // Also set the string version for BaseEntity
        setCreatedBy(createdByUser != null ? createdByUser.getUsername() : null);
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
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

    public List<StockAdjustmentItem> getItems() {
        return items;
    }

    public void setItems(List<StockAdjustmentItem> items) {
        this.items = items;
    }

    // Helper methods
    public void addItem(StockAdjustmentItem item) {
        items.add(item);
        item.setStockAdjustment(this);
    }

    public void removeItem(StockAdjustmentItem item) {
        items.remove(item);
        item.setStockAdjustment(null);
    }

    public void approve(User approver) {
        if (this.status == AdjustmentStatus.PENDING) {
            this.status = AdjustmentStatus.APPROVED;
            this.approvedDate = LocalDateTime.now();
            this.approvedBy = approver;
        } else {
            throw new IllegalStateException("Stock adjustment must be in PENDING status to approve");
        }
    }

    public void complete() {
        if (this.status == AdjustmentStatus.APPROVED) {
            this.status = AdjustmentStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Stock adjustment must be in APPROVED status to complete");
        }
    }

    public void reject() {
        this.status = AdjustmentStatus.REJECTED;
    }

    public void cancel(User cancelledBy, String reason) {
        if (this.status != AdjustmentStatus.COMPLETED) {
            this.isCancelled = true;
            this.status = AdjustmentStatus.CANCELLED;
            this.cancelledBy = cancelledBy;
            this.cancellationReason = reason;
            this.cancellationDate = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Cannot cancel a completed stock adjustment");
        }
    }

    public boolean isPending() {
        return this.status == AdjustmentStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == AdjustmentStatus.APPROVED;
    }

    public boolean isCompleted() {
        return this.status == AdjustmentStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return this.status == AdjustmentStatus.CANCELLED;
    }

    public boolean isAddition() {
        return this.adjustmentType == AdjustmentType.ADDITION;
    }

    public boolean isReduction() {
        return this.adjustmentType == AdjustmentType.REDUCTION || 
               this.adjustmentType == AdjustmentType.SUBTRACTION ||
               this.adjustmentType == AdjustmentType.DAMAGE || 
               this.adjustmentType == AdjustmentType.EXPIRY || 
               this.adjustmentType == AdjustmentType.LOSS || 
               this.adjustmentType == AdjustmentType.THEFT;
    }
}