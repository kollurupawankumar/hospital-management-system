package com.ingestion.inventory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
public class Asset extends BaseEntity {

    @Column(name = "asset_code", nullable = false, unique = true)
    private String assetCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ItemCategory category;

    @Column(name = "asset_type")
    @Enumerated(EnumType.STRING)
    private AssetType assetType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AssetStatus status = AssetStatus.AVAILABLE;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "current_value", precision = 10, scale = 2)
    private BigDecimal currentValue;

    @Column(name = "depreciation_rate", precision = 5, scale = 2)
    private BigDecimal depreciationRate;

    @Column(name = "last_depreciation_date")
    private LocalDate lastDepreciationDate;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "warranty_expiry_date")
    private LocalDate warrantyExpiryDate;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(name = "assigned_date")
    private LocalDateTime assignedDate;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @Column(name = "maintenance_interval_days")
    private Integer maintenanceIntervalDays;

    @Column(name = "notes", length = 1000)
    private String notes;

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

    public enum AssetType {
        EQUIPMENT, FURNITURE, VEHICLE, BUILDING, IT_HARDWARE, SOFTWARE, MEDICAL_DEVICE, OTHER
    }

    public enum AssetStatus {
        AVAILABLE, IN_USE, UNDER_MAINTENANCE, DAMAGED, DISPOSED, LOST
    }

    @PrePersist
    protected void onCreate() {
        if (assetCode == null || assetCode.isEmpty()) {
            // Generate asset code
            String prefix = "AST";
            assetCode = prefix + "-" + System.nanoTime() % 10000;
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
    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public AssetStatus getStatus() {
        return status;
    }

    public void setStatus(AssetStatus status) {
        this.status = status;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public LocalDate getLastDepreciationDate() {
        return lastDepreciationDate;
    }

    public void setLastDepreciationDate(LocalDate lastDepreciationDate) {
        this.lastDepreciationDate = lastDepreciationDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getWarrantyExpiryDate() {
        return warrantyExpiryDate;
    }

    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
        this.warrantyExpiryDate = warrantyExpiryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public Integer getMaintenanceIntervalDays() {
        return maintenanceIntervalDays;
    }

    public void setMaintenanceIntervalDays(Integer maintenanceIntervalDays) {
        this.maintenanceIntervalDays = maintenanceIntervalDays;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    // Helper methods
    public void assignTo(User user) {
        this.assignedTo = user;
        this.assignedDate = LocalDateTime.now();
        this.status = AssetStatus.IN_USE;
    }

    public void unassign() {
        this.assignedTo = null;
        this.assignedDate = null;
        this.status = AssetStatus.AVAILABLE;
    }

    public void markForMaintenance() {
        this.status = AssetStatus.UNDER_MAINTENANCE;
    }

    public void markAsDamaged() {
        this.status = AssetStatus.DAMAGED;
    }

    public void markAsDisposed() {
        this.status = AssetStatus.DISPOSED;
    }

    public void markAsLost() {
        this.status = AssetStatus.LOST;
    }

    public void markAsAvailable() {
        this.status = AssetStatus.AVAILABLE;
    }

    public void recordMaintenance() {
        this.lastMaintenanceDate = LocalDate.now();
        
        if (maintenanceIntervalDays != null) {
            this.nextMaintenanceDate = LocalDate.now().plusDays(maintenanceIntervalDays);
        }
        
        this.status = AssetStatus.AVAILABLE;
    }

    public void calculateDepreciation() {
        if (purchasePrice != null && depreciationRate != null && purchaseDate != null) {
            LocalDate today = LocalDate.now();
            
            // If last depreciation date is null, use purchase date
            LocalDate lastDepDate = lastDepreciationDate != null ? lastDepreciationDate : purchaseDate;
            
            // Calculate years since last depreciation
            long yearsSinceLastDepreciation = java.time.temporal.ChronoUnit.YEARS.between(lastDepDate, today);
            
            if (yearsSinceLastDepreciation > 0) {
                BigDecimal depreciationFactor = BigDecimal.ONE.subtract(
                        depreciationRate.divide(new BigDecimal(100))
                ).pow((int) yearsSinceLastDepreciation);
                
                // Current value is the starting value times the depreciation factor
                BigDecimal startingValue = currentValue != null ? currentValue : purchasePrice;
                currentValue = startingValue.multiply(depreciationFactor);
                
                // Update last depreciation date
                lastDepreciationDate = today;
            }
        }
    }

    public boolean isUnderWarranty() {
        if (warrantyExpiryDate == null) {
            return false;
        }
        return LocalDate.now().isBefore(warrantyExpiryDate) || LocalDate.now().isEqual(warrantyExpiryDate);
    }

    public boolean isMaintenanceDue() {
        if (nextMaintenanceDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(nextMaintenanceDate) || LocalDate.now().isEqual(nextMaintenanceDate);
    }

    public boolean isMaintenanceDueSoon(int daysThreshold) {
        if (nextMaintenanceDate == null) {
            return false;
        }
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        return nextMaintenanceDate.isBefore(thresholdDate) && !isMaintenanceDue();
    }
}