package com.ingestion.common.model.inpatient;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "beds")
public class Bed extends BaseEntity {

    @NotBlank(message = "Bed number is required")
    @Column(name = "bed_number", nullable = false)
    private String bedNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bed_type", nullable = false)
    private BedType bedType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BedStatus status = BedStatus.AVAILABLE;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @NotNull(message = "Daily rate is required")
    @Column(name = "daily_rate", nullable = false)
    private Double dailyRate;

    @Column(name = "features", length = 500)
    private String features;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    public enum BedType {
        STANDARD, ELECTRIC, BARIATRIC, PEDIATRIC, ICU, CCU, STRETCHER, BIRTHING, HOMECARE, OTHER
    }

    public enum BedStatus {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE, CLEANING
    }

    // Getters and Setters
    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    public BedStatus getStatus() {
        return status;
    }

    public void setStatus(BedStatus status) {
        this.status = status;
        if (this.ward != null) {
            this.ward.updateAvailableBeds();
        }
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }
}