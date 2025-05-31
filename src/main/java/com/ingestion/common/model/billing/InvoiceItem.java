package com.ingestion.common.model.billing;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

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

    @Column(name = "is_insurance_covered")
    private Boolean isInsuranceCovered = false;

    @Column(name = "insurance_coverage_percentage", precision = 5, scale = 2)
    private BigDecimal insuranceCoveragePercentage;

    @Column(name = "insurance_covered_amount", precision = 10, scale = 2)
    private BigDecimal insuranceCoveredAmount;

    @Column(name = "patient_payable_amount", precision = 10, scale = 2)
    private BigDecimal patientPayableAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ServiceType {
        CONSULTATION, PROCEDURE, LABORATORY, RADIOLOGY, MEDICATION, ROOM_CHARGE, SURGERY, EQUIPMENT, SUPPLIES, OTHER
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateTotalAmount();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotalAmount();
    }

    // Getters and Setters
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalAmount();
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

    public Boolean getIsInsuranceCovered() {
        return isInsuranceCovered;
    }

    public void setIsInsuranceCovered(Boolean insuranceCovered) {
        isInsuranceCovered = insuranceCovered;
        calculateInsuranceCoverage();
    }

    public BigDecimal getInsuranceCoveragePercentage() {
        return insuranceCoveragePercentage;
    }

    public void setInsuranceCoveragePercentage(BigDecimal insuranceCoveragePercentage) {
        this.insuranceCoveragePercentage = insuranceCoveragePercentage;
        calculateInsuranceCoverage();
    }

    public BigDecimal getInsuranceCoveredAmount() {
        return insuranceCoveredAmount;
    }

    public void setInsuranceCoveredAmount(BigDecimal insuranceCoveredAmount) {
        this.insuranceCoveredAmount = insuranceCoveredAmount;
        calculatePatientPayableAmount();
    }

    public BigDecimal getPatientPayableAmount() {
        return patientPayableAmount;
    }

    public void setPatientPayableAmount(BigDecimal patientPayableAmount) {
        this.patientPayableAmount = patientPayableAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    private void calculateDiscountAmount() {
        if (unitPrice != null && quantity != null && discountPercentage != null) {
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(quantity));
            discountAmount = subtotal.multiply(discountPercentage).divide(new BigDecimal(100));
        }
    }

    private void calculateTaxAmount() {
        if (unitPrice != null && quantity != null && taxPercentage != null) {
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(quantity));
            
            // Apply discount if available
            if (discountAmount != null) {
                subtotal = subtotal.subtract(discountAmount);
            }
            
            taxAmount = subtotal.multiply(taxPercentage).divide(new BigDecimal(100));
        }
    }

    private void calculateTotalAmount() {
        if (unitPrice != null && quantity != null) {
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(quantity));
            
            // Apply discount if available
            if (discountAmount != null) {
                subtotal = subtotal.subtract(discountAmount);
            }
            
            // Apply tax if available
            if (taxAmount != null) {
                subtotal = subtotal.add(taxAmount);
            }
            
            totalAmount = subtotal;
            calculateInsuranceCoverage();
        }
    }

    private void calculateInsuranceCoverage() {
        if (isInsuranceCovered && totalAmount != null && insuranceCoveragePercentage != null) {
            insuranceCoveredAmount = totalAmount.multiply(insuranceCoveragePercentage).divide(new BigDecimal(100));
            calculatePatientPayableAmount();
        } else if (!isInsuranceCovered) {
            insuranceCoveredAmount = BigDecimal.ZERO;
            calculatePatientPayableAmount();
        }
    }

    private void calculatePatientPayableAmount() {
        if (totalAmount != null) {
            if (insuranceCoveredAmount != null) {
                patientPayableAmount = totalAmount.subtract(insuranceCoveredAmount);
            } else {
                patientPayableAmount = totalAmount;
            }
        }
    }
}