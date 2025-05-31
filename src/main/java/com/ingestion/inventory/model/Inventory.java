package com.ingestion.inventory.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "quantity_available")
    private Integer quantityAvailable = 0;

    @Column(name = "quantity_reserved")
    private Integer quantityReserved = 0;

    @Column(name = "manufacturing_date")
    private LocalDateTime manufacturingDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "rack_number")
    private String rackNumber;

    @Column(name = "shelf_number")
    private String shelfNumber;

    @Column(name = "bin_number")
    private String binNumber;

    @Column(name = "last_stock_update")
    private LocalDateTime lastStockUpdate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        lastStockUpdate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastStockUpdate = LocalDateTime.now();
    }

    // Getters and Setters
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Integer getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(Integer quantityReserved) {
        this.quantityReserved = quantityReserved;
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

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getRackNumber() {
        return rackNumber;
    }

    public void setRackNumber(String rackNumber) {
        this.rackNumber = rackNumber;
    }

    public String getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getBinNumber() {
        return binNumber;
    }

    public void setBinNumber(String binNumber) {
        this.binNumber = binNumber;
    }

    public LocalDateTime getLastStockUpdate() {
        return lastStockUpdate;
    }

    public void setLastStockUpdate(LocalDateTime lastStockUpdate) {
        this.lastStockUpdate = lastStockUpdate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    // Helper methods
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public Integer getAvailableQuantity() {
        return quantityAvailable - quantityReserved;
    }

    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon(int daysThreshold) {
        if (expiryDate == null) {
            return false;
        }
        LocalDateTime thresholdDate = LocalDateTime.now().plusDays(daysThreshold);
        return expiryDate.isBefore(thresholdDate) && !isExpired();
    }

    public boolean isLowStock() {
        if (item == null || item.getReorderLevel() == null) {
            return false;
        }
        return getAvailableQuantity() <= item.getReorderLevel();
    }

    public String getLocation() {
        StringBuilder location = new StringBuilder();
        
        if (rackNumber != null && !rackNumber.isEmpty()) {
            location.append("Rack: ").append(rackNumber);
        }
        
        if (shelfNumber != null && !shelfNumber.isEmpty()) {
            if (location.length() > 0) {
                location.append(", ");
            }
            location.append("Shelf: ").append(shelfNumber);
        }
        
        if (binNumber != null && !binNumber.isEmpty()) {
            if (location.length() > 0) {
                location.append(", ");
            }
            location.append("Bin: ").append(binNumber);
        }
        
        return location.toString();
    }

    public void addStock(Integer quantity) {
        if (quantity != null && quantity > 0) {
            this.quantityAvailable += quantity;
            this.lastStockUpdate = LocalDateTime.now();
        }
    }

    public void removeStock(Integer quantity) {
        if (quantity != null && quantity > 0) {
            if (quantity <= this.getAvailableQuantity()) {
                this.quantityAvailable -= quantity;
                this.lastStockUpdate = LocalDateTime.now();
            } else {
                throw new IllegalArgumentException("Cannot remove more stock than available");
            }
        }
    }

    public void reserveStock(Integer quantity) {
        if (quantity != null && quantity > 0) {
            if (quantity <= this.getAvailableQuantity()) {
                this.quantityReserved += quantity;
                this.lastStockUpdate = LocalDateTime.now();
            } else {
                throw new IllegalArgumentException("Cannot reserve more stock than available");
            }
        }
    }

    public void releaseReservedStock(Integer quantity) {
        if (quantity != null && quantity > 0) {
            if (quantity <= this.quantityReserved) {
                this.quantityReserved -= quantity;
                this.lastStockUpdate = LocalDateTime.now();
            } else {
                throw new IllegalArgumentException("Cannot release more reserved stock than exists");
            }
        }
    }

    public void consumeReservedStock(Integer quantity) {
        if (quantity != null && quantity > 0) {
            if (quantity <= this.quantityReserved) {
                this.quantityReserved -= quantity;
                this.quantityAvailable -= quantity;
                this.lastStockUpdate = LocalDateTime.now();
            } else {
                throw new IllegalArgumentException("Cannot consume more reserved stock than exists");
            }
        }
    }
}