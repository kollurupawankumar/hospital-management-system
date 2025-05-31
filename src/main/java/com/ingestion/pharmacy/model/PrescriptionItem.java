package com.ingestion.pharmacy.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prescription_items")
public class PrescriptionItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "duration")
    private String duration;

    @Column(name = "route")
    @Enumerated(EnumType.STRING)
    private AdministrationRoute route;

    @Column(name = "instructions", length = 500)
    private String instructions;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_dispensed")
    private Boolean isDispensed = false;

    @Column(name = "dispensed_quantity")
    private Integer dispensedQuantity = 0;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "notes", length = 500)
    private String notes;

    public enum AdministrationRoute {
        ORAL, INTRAVENOUS, INTRAMUSCULAR, SUBCUTANEOUS, TOPICAL, INHALATION, RECTAL, SUBLINGUAL, NASAL, OPHTHALMIC, OTIC, OTHER
    }

    // Getters and Setters
    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public AdministrationRoute getRoute() {
        return route;
    }

    public void setRoute(AdministrationRoute route) {
        this.route = route;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public Boolean getIsDispensed() {
        return isDispensed;
    }

    public void setIsDispensed(Boolean dispensed) {
        isDispensed = dispensed;
    }

    public Integer getDispensedQuantity() {
        return dispensedQuantity;
    }

    public void setDispensedQuantity(Integer dispensedQuantity) {
        this.dispensedQuantity = dispensedQuantity;
        updateDispensedStatus();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper methods
    private void calculateTotalPrice() {
        if (unitPrice != null && quantity != null) {
            totalPrice = unitPrice.multiply(new BigDecimal(quantity));
        }
    }

    private void updateDispensedStatus() {
        if (dispensedQuantity >= quantity) {
            isDispensed = true;
        } else {
            isDispensed = false;
        }
    }

    public void dispense(int quantity, String batchNumber) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        
        if (this.dispensedQuantity + quantity > this.quantity) {
            throw new IllegalArgumentException("Cannot dispense more than prescribed quantity");
        }
        
        this.dispensedQuantity += quantity;
        this.batchNumber = batchNumber;
        
        updateDispensedStatus();
    }

    public boolean isFullyDispensed() {
        return dispensedQuantity.equals(quantity);
    }

    public boolean isPartiallyDispensed() {
        return dispensedQuantity > 0 && dispensedQuantity < quantity;
    }

    public Integer getRemainingQuantity() {
        return quantity - dispensedQuantity;
    }
}