package com.ingestion.billing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "discount_policies")
public class DiscountPolicy extends BaseEntity {

    @Column(name = "policy_name", nullable = false)
    private String policyName;

    @Column(name = "policy_code", nullable = false, unique = true)
    private String policyCode;

    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "discount_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "applicable_to")
    @Enumerated(EnumType.STRING)
    private ApplicableTo applicableTo;

    @Column(name = "minimum_bill_amount", precision = 10, scale = 2)
    private BigDecimal minimumBillAmount;

    @Column(name = "maximum_discount_amount", precision = 10, scale = 2)
    private BigDecimal maximumDiscountAmount;

    @Column(name = "requires_approval")
    private Boolean requiresApproval = false;

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

    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT
    }

    public enum ApplicableTo {
        ALL, OPD, IPD, LABORATORY, RADIOLOGY, PHARMACY, PROCEDURE, SURGERY, OTHER
    }

    @PrePersist
    protected void onCreate() {
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
    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public ApplicableTo getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(ApplicableTo applicableTo) {
        this.applicableTo = applicableTo;
    }

    public BigDecimal getMinimumBillAmount() {
        return minimumBillAmount;
    }

    public void setMinimumBillAmount(BigDecimal minimumBillAmount) {
        this.minimumBillAmount = minimumBillAmount;
    }

    public BigDecimal getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(BigDecimal maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    public Boolean getRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
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
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isCurrentlyEffective() {
        LocalDate today = LocalDate.now();
        
        boolean afterStartDate = effectiveFrom == null || !today.isBefore(effectiveFrom);
        boolean beforeEndDate = effectiveTo == null || !today.isAfter(effectiveTo);
        
        return isActive && afterStartDate && beforeEndDate;
    }

    public boolean isApplicable(BigDecimal billAmount) {
        if (!isCurrentlyEffective()) {
            return false;
        }
        
        if (minimumBillAmount != null && billAmount.compareTo(minimumBillAmount) < 0) {
            return false;
        }
        
        return true;
    }

    public BigDecimal calculateDiscountAmount(BigDecimal billAmount) {
        if (billAmount == null || discountValue == null || !isApplicable(billAmount)) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discountAmount;
        
        if (discountType == DiscountType.PERCENTAGE) {
            discountAmount = billAmount.multiply(discountValue).divide(new BigDecimal(100));
        } else {
            discountAmount = discountValue;
        }
        
        // Apply maximum discount limit if specified
        if (maximumDiscountAmount != null && discountAmount.compareTo(maximumDiscountAmount) > 0) {
            discountAmount = maximumDiscountAmount;
        }
        
        return discountAmount;
    }
}