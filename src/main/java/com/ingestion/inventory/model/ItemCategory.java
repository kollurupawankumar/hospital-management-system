package com.ingestion.inventory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_categories")
public class ItemCategory extends BaseEntity {

    @Column(name = "category_code", nullable = false, unique = true)
    private String categoryCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private ItemCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<ItemCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;

    @PrePersist
    protected void onCreate() {
        if (categoryCode == null || categoryCode.isEmpty()) {
            // Generate category code
            String prefix = "CAT";
            categoryCode = prefix + "-" + System.nanoTime() % 10000;
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
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
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

    public ItemCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ItemCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<ItemCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<ItemCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
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

    // Helper methods
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void addSubCategory(ItemCategory subCategory) {
        subCategories.add(subCategory);
        subCategory.setParentCategory(this);
    }

    public void removeSubCategory(ItemCategory subCategory) {
        subCategories.remove(subCategory);
        subCategory.setParentCategory(null);
    }

    public void addItem(Item item) {
        items.add(item);
        item.setCategory(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setCategory(null);
    }

    public boolean hasSubCategories() {
        return !subCategories.isEmpty();
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public boolean isRootCategory() {
        return parentCategory == null;
    }

    public String getFullCategoryPath() {
        if (isRootCategory()) {
            return name;
        } else {
            return parentCategory.getFullCategoryPath() + " > " + name;
        }
    }
}