package com.ingestion.inventory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
public class Store extends BaseEntity {

    @Column(name = "store_code", nullable = false, unique = true)
    private String storeCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "store_type")
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;

    @OneToMany(mappedBy = "store")
    private List<Inventory> inventoryItems = new ArrayList<>();

    public enum StoreType {
        MAIN_STORE, PHARMACY, LABORATORY, RADIOLOGY, WARD, OT, EMERGENCY, OTHER
    }

    @PrePersist
    protected void onCreate() {
        if (storeCode == null || storeCode.isEmpty()) {
            // Generate store code
            String prefix = "STR";
            storeCode = prefix + "-" + System.nanoTime() % 10000;
        }
        
        // Set the string version of creator for BaseEntity
        if (creator != null) {
            setCreatedBy(creator.getUsername());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Set the string version of last modified by for BaseEntity
        if (lastModifiedBy != null) {
            setUpdatedBy(lastModifiedBy.getUsername());
        }
    }

    // Getters and Setters
    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public StoreType getStoreType() {
        return storeType;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        // Also set the string version for BaseEntity
        setCreatedBy(creator != null ? creator.getUsername() : null);
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        // Also set the string version for BaseEntity
        setUpdatedBy(lastModifiedBy != null ? lastModifiedBy.getUsername() : null);
    }

    public List<Inventory> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<Inventory> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    // Helper methods
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void addInventoryItem(Inventory inventoryItem) {
        inventoryItems.add(inventoryItem);
        inventoryItem.setStore(this);
    }

    public void removeInventoryItem(Inventory inventoryItem) {
        inventoryItems.remove(inventoryItem);
        inventoryItem.setStore(null);
    }

    public Integer getTotalItemCount() {
        return inventoryItems.size();
    }

    public List<Inventory> getLowStockItems() {
        List<Inventory> lowStockItems = new ArrayList<>();
        
        for (Inventory inventory : inventoryItems) {
            if (inventory.isLowStock()) {
                lowStockItems.add(inventory);
            }
        }
        
        return lowStockItems;
    }

    public List<Inventory> getExpiringItems(int daysThreshold) {
        List<Inventory> expiringItems = new ArrayList<>();
        
        for (Inventory inventory : inventoryItems) {
            if (inventory.isExpiringSoon(daysThreshold)) {
                expiringItems.add(inventory);
            }
        }
        
        return expiringItems;
    }

    public List<Inventory> getExpiredItems() {
        List<Inventory> expiredItems = new ArrayList<>();
        
        for (Inventory inventory : inventoryItems) {
            if (inventory.isExpired()) {
                expiredItems.add(inventory);
            }
        }
        
        return expiredItems;
    }
}