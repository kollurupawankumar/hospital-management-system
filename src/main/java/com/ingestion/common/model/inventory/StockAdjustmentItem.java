package com.ingestion.common.model.inventory;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stock_adjustment_items")
public class StockAdjustmentItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_adjustment_id", nullable = false)
    private StockAdjustment stockAdjustment;

    @Column(name = "item_id", nullable = false)
    private Long itemId; // Reference to Medicine or Item

    @Column(name = "item_type")
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "expiry_date")
    private java.time.LocalDate expiryDate;

    @Column(name = "current_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentQuantity;

    @Column(name = "adjusted_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal adjustedQuantity;

    @Column(name = "difference_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal differenceQuantity;

    @Column(name = "unit_cost", precision = 10, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "notes", length = 500)
    private String notes;

    public enum ItemType {
        MEDICINE, INVENTORY_ITEM
    }

    @PrePersist
    @PreUpdate
    protected void calculateDifference() {
        if (currentQuantity != null && adjustedQuantity != null) {
            differenceQuantity = adjustedQuantity.subtract(currentQuantity);
        }
        if (differenceQuantity != null && unitCost != null) {
            totalCost = differenceQuantity.multiply(unitCost);
        }
    }

    // Getters and Setters
    public StockAdjustment getStockAdjustment() {
        return stockAdjustment;
    }

    public void setStockAdjustment(StockAdjustment stockAdjustment) {
        this.stockAdjustment = stockAdjustment;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public java.time.LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(java.time.LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(BigDecimal currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public BigDecimal getAdjustedQuantity() {
        return adjustedQuantity;
    }

    public void setAdjustedQuantity(BigDecimal adjustedQuantity) {
        this.adjustedQuantity = adjustedQuantity;
    }

    public BigDecimal getDifferenceQuantity() {
        return differenceQuantity;
    }

    public void setDifferenceQuantity(BigDecimal differenceQuantity) {
        this.differenceQuantity = differenceQuantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
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

    // Helper methods
    public boolean isIncrease() {
        return differenceQuantity != null && differenceQuantity.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDecrease() {
        return differenceQuantity != null && differenceQuantity.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getAbsoluteDifference() {
        return differenceQuantity != null ? differenceQuantity.abs() : BigDecimal.ZERO;
    }
}