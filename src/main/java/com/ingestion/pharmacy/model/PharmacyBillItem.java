package com.ingestion.pharmacy.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pharmacy_bill_items")
public class PharmacyBillItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_bill_id", nullable = false)
    private PharmacyBill pharmacyBill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_item_id")
    private PrescriptionItem prescriptionItem;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

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

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "expiry_date")
    private java.time.LocalDate expiryDate;

    @Column(name = "notes", length = 500)
    private String notes;

    // Getters and Setters
    public PharmacyBill getPharmacyBill() {
        return pharmacyBill;
    }

    public void setPharmacyBill(PharmacyBill pharmacyBill) {
        this.pharmacyBill = pharmacyBill;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public PrescriptionItem getPrescriptionItem() {
        return prescriptionItem;
    }

    public void setPrescriptionItem(PrescriptionItem prescriptionItem) {
        this.prescriptionItem = prescriptionItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculateDiscountAmount();
        calculateTotalPrice();
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        calculateTotalPrice();
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
        calculateTaxAmount();
        calculateTotalPrice();
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper methods
    private void calculateDiscountAmount() {
        if (unitPrice != null && quantity != null && discountPercentage != null) {
            BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity));
            discountAmount = totalPrice.multiply(discountPercentage).divide(new BigDecimal(100));
        }
    }

    private void calculateTaxAmount() {
        if (unitPrice != null && quantity != null && taxPercentage != null) {
            BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity));
            
            // Apply discount if available
            if (discountAmount != null) {
                totalPrice = totalPrice.subtract(discountAmount);
            }
            
            taxAmount = totalPrice.multiply(taxPercentage).divide(new BigDecimal(100));
        }
    }

    private void calculateTotalPrice() {
        if (unitPrice != null && quantity != null) {
            BigDecimal total = unitPrice.multiply(new BigDecimal(quantity));
            
            // Apply discount if available
            if (discountAmount != null) {
                total = total.subtract(discountAmount);
            }
            
            // Apply tax if available
            if (taxAmount != null) {
                total = total.add(taxAmount);
            }
            
            totalPrice = total;
        }
    }
}