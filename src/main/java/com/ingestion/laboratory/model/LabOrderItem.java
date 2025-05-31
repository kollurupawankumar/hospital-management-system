package com.ingestion.laboratory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.common.laboratory.LabOrder;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lab_order_items")
public class LabOrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTest labTest;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ItemStatus status = ItemStatus.PENDING;

    public enum ItemStatus {
        PENDING, IN_PROCESS, COMPLETED, CANCELLED
    }

    @PrePersist
    @PreUpdate
    protected void calculateFinalPrice() {
        if (price != null) {
            if (discount != null) {
                finalPrice = price.subtract(discount);
            } else {
                finalPrice = price;
            }
        }
    }

    // Getters and Setters
    public LabOrder getLabOrder() {
        return labOrder;
    }

    public void setLabOrder(LabOrder labOrder) {
        this.labOrder = labOrder;
    }

    public LabTest getLabTest() {
        return labTest;
    }

    public void setLabTest(LabTest labTest) {
        this.labTest = labTest;
        if (labTest != null) {
            this.price = labTest.getPrice();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    // Helper methods
    public void markAsInProcess() {
        this.status = ItemStatus.IN_PROCESS;
    }

    public void markAsCompleted() {
        this.status = ItemStatus.COMPLETED;
    }

    public void markAsCancelled() {
        this.status = ItemStatus.CANCELLED;
    }
}