package com.ingestion.inventory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.common.model.purchasing.PurchaseOrderItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "goods_receipt_items")
public class GoodsReceiptItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id", nullable = false)
    private GoodsReceipt goodsReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_item_id")
    private PurchaseOrderItem purchaseOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "received_quantity", nullable = false)
    private Integer receivedQuantity;

    @Column(name = "accepted_quantity", nullable = false)
    private Integer acceptedQuantity;

    @Column(name = "rejected_quantity")
    private Integer rejectedQuantity = 0;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "manufacturing_date")
    private LocalDateTime manufacturingDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "is_added_to_inventory")
    private Boolean isAddedToInventory = false;

    // Getters and Setters
    public GoodsReceipt getGoodsReceipt() {
        return goodsReceipt;
    }

    public void setGoodsReceipt(GoodsReceipt goodsReceipt) {
        this.goodsReceipt = goodsReceipt;
    }

    public PurchaseOrderItem getPurchaseOrderItem() {
        return purchaseOrderItem;
    }

    public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        this.purchaseOrderItem = purchaseOrderItem;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
        calculateTotalAmount();
    }

    public Integer getAcceptedQuantity() {
        return acceptedQuantity;
    }

    public void setAcceptedQuantity(Integer acceptedQuantity) {
        this.acceptedQuantity = acceptedQuantity;
        calculateTotalAmount();
    }

    public Integer getRejectedQuantity() {
        return rejectedQuantity;
    }

    public void setRejectedQuantity(Integer rejectedQuantity) {
        this.rejectedQuantity = rejectedQuantity;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDateTime getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDateTime manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalAmount();
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculateDiscountAmount();
        calculateTotalAmount();
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        calculateTotalAmount();
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
        calculateTaxAmount();
        calculateTotalAmount();
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        calculateTotalAmount();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsAddedToInventory() {
        return isAddedToInventory;
    }

    public void setIsAddedToInventory(Boolean addedToInventory) {
        isAddedToInventory = addedToInventory;
    }

    // Helper methods
    private void calculateDiscountAmount() {
        if (unitPrice != null && acceptedQuantity != null && discountPercentage != null) {
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(acceptedQuantity));
            discountAmount = subtotal.multiply(discountPercentage).divide(new BigDecimal(100));
        }
    }

    private void calculateTaxAmount() {
        if (unitPrice != null && acceptedQuantity != null && taxPercentage != null) {
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(acceptedQuantity));
            
            // Apply discount if available
            if (discountAmount != null) {
                subtotal = subtotal.subtract(discountAmount);
            }
            
            taxAmount = subtotal.multiply(taxPercentage).divide(new BigDecimal(100));
        }
    }

    private void calculateTotalAmount() {
        if (unitPrice != null && acceptedQuantity != null) {
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(acceptedQuantity));
            
            // Apply discount if available
            if (discountAmount != null) {
                subtotal = subtotal.subtract(discountAmount);
            }
            
            // Apply tax if available
            if (taxAmount != null) {
                subtotal = subtotal.add(taxAmount);
            }
            
            totalAmount = subtotal;
        }
    }

    public void rejectItems(Integer quantity, String reason) {
        if (quantity != null && quantity > 0 && quantity <= receivedQuantity) {
            rejectedQuantity += quantity;
            acceptedQuantity = receivedQuantity - rejectedQuantity;
            rejectionReason = reason;
            calculateTotalAmount();
        } else {
            throw new IllegalArgumentException("Invalid rejection quantity");
        }
    }

    public void markAsAddedToInventory() {
        isAddedToInventory = true;
    }

    public boolean isFullyRejected() {
        return rejectedQuantity.equals(receivedQuantity);
    }

    public boolean isPartiallyRejected() {
        return rejectedQuantity > 0 && rejectedQuantity < receivedQuantity;
    }
}