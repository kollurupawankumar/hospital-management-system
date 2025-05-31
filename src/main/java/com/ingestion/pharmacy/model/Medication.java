package com.ingestion.pharmacy.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medications")
public class Medication extends BaseEntity {

    @Column(name = "medication_code", nullable = false, unique = true)
    private String medicationCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "dosage_form")
    @Enumerated(EnumType.STRING)
    private DosageForm dosageForm;

    @Column(name = "strength")
    private String strength;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private MedicationCategory category;

    @Column(name = "is_controlled_substance")
    private Boolean isControlledSubstance = false;

    @Column(name = "controlled_substance_class")
    private String controlledSubstanceClass;

    @Column(name = "requires_prescription")
    private Boolean requiresPrescription = true;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "current_stock")
    private Integer currentStock = 0;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "storage_instructions", length = 500)
    private String storageInstructions;

    @Column(name = "administration_instructions", length = 1000)
    private String administrationInstructions;

    @Column(name = "side_effects", length = 1000)
    private String sideEffects;

    @Column(name = "contraindications", length = 1000)
    private String contraindications;

    @Column(name = "interactions", length = 1000)
    private String interactions;

    @Column(name = "notes", length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    public enum DosageForm {
        TABLET, CAPSULE, LIQUID, INJECTION, CREAM, OINTMENT, GEL, PATCH, INHALER, DROPS, SUPPOSITORY, POWDER, OTHER
    }

    public enum MedicationCategory {
        ANALGESIC, ANTIBIOTIC, ANTIHISTAMINE, ANTIHYPERTENSIVE, ANTIDEPRESSANT, ANTIDIABETIC, 
        ANTIINFLAMMATORY, ANTIVIRAL, CARDIOVASCULAR, GASTROINTESTINAL, HORMONE, IMMUNOSUPPRESSANT, 
        RESPIRATORY, VITAMIN, OTHER
    }

    @PrePersist
    protected void onCreate() {
        if (medicationCode == null || medicationCode.isEmpty()) {
            // Generate medication code
            String prefix = "MED";
            medicationCode = prefix + "-" + System.nanoTime() % 10000;
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
    public String getMedicationCode() {
        return medicationCode;
    }

    public void setMedicationCode(String medicationCode) {
        this.medicationCode = medicationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DosageForm getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(DosageForm dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public MedicationCategory getCategory() {
        return category;
    }

    public void setCategory(MedicationCategory category) {
        this.category = category;
    }

    public Boolean getIsControlledSubstance() {
        return isControlledSubstance;
    }

    public void setIsControlledSubstance(Boolean controlledSubstance) {
        isControlledSubstance = controlledSubstance;
    }

    public String getControlledSubstanceClass() {
        return controlledSubstanceClass;
    }

    public void setControlledSubstanceClass(String controlledSubstanceClass) {
        this.controlledSubstanceClass = controlledSubstanceClass;
    }

    public Boolean getRequiresPrescription() {
        return requiresPrescription;
    }

    public void setRequiresPrescription(Boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
    }

    public String getAdministrationInstructions() {
        return administrationInstructions;
    }

    public void setAdministrationInstructions(String administrationInstructions) {
        this.administrationInstructions = administrationInstructions;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getInteractions() {
        return interactions;
    }

    public void setInteractions(String interactions) {
        this.interactions = interactions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        // Also set the string version for BaseEntity
        setCreatedBy(creator != null ? creator.getUsername() : null);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // Helper methods
    public void incrementStock(int quantity) {
        this.currentStock += quantity;
    }

    public void decrementStock(int quantity) {
        if (this.currentStock >= quantity) {
            this.currentStock -= quantity;
        } else {
            throw new IllegalArgumentException("Cannot decrement stock below zero");
        }
    }

    public boolean isLowStock() {
        return reorderLevel != null && currentStock <= reorderLevel;
    }

    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon(int daysThreshold) {
        if (expiryDate == null) {
            return false;
        }
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        return expiryDate.isBefore(thresholdDate) && !isExpired();
    }
}