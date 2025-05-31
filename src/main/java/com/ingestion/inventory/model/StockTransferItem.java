package com.ingestion.inventory.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "stock_transfer_items")
public class StockTransferItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_transfer_id", nullable = false)
    private StockTransfer stockTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "received_quantity")
    private Integer receivedQuantity = 0;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "is_processed")
    private Boolean isProcessed = false;

    // Getters and Setters
    public StockTransfer getStockTransfer() {
        return stockTransfer;
    }

    public void setStockTransfer(StockTransfer stockTransfer) {
        this.stockTransfer = stockTransfer;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean processed) {
        isProcessed = processed;
    }

    // Helper methods
    public void receiveQuantity(Integer quantity) {
        if (quantity != null && quantity > 0) {
            if (receivedQuantity + quantity <= this.quantity) {
                receivedQuantity += quantity;
            } else {
                throw new IllegalArgumentException("Cannot receive more than transferred quantity");
            }
        }
    }

    public void markAsProcessed() {
        isProcessed = true;
    }

    public boolean isFullyReceived() {
        return receivedQuantity >= quantity;
    }

    public boolean isPartiallyReceived() {
        return receivedQuantity > 0 && receivedQuantity < quantity;
    }

    public Integer getPendingQuantity() {
        return quantity - receivedQuantity;
    }
}