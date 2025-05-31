package com.ingestion.laboratory.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "test_parameters")
public class TestParameter extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTest labTest;

    @Column(name = "parameter_name", nullable = false)
    private String parameterName;

    @Column(name = "unit")
    private String unit;

    @Column(name = "reference_range")
    private String referenceRange;

    @Column(name = "male_reference_range")
    private String maleReferenceRange;

    @Column(name = "female_reference_range")
    private String femaleReferenceRange;

    @Column(name = "child_reference_range")
    private String childReferenceRange;

    @Column(name = "critical_low")
    private String criticalLow;

    @Column(name = "critical_high")
    private String criticalHigh;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Getters and Setters
    public LabTest getLabTest() {
        return labTest;
    }

    public void setLabTest(LabTest labTest) {
        this.labTest = labTest;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getMaleReferenceRange() {
        return maleReferenceRange;
    }

    public void setMaleReferenceRange(String maleReferenceRange) {
        this.maleReferenceRange = maleReferenceRange;
    }

    public String getFemaleReferenceRange() {
        return femaleReferenceRange;
    }

    public void setFemaleReferenceRange(String femaleReferenceRange) {
        this.femaleReferenceRange = femaleReferenceRange;
    }

    public String getChildReferenceRange() {
        return childReferenceRange;
    }

    public void setChildReferenceRange(String childReferenceRange) {
        this.childReferenceRange = childReferenceRange;
    }

    public String getCriticalLow() {
        return criticalLow;
    }

    public void setCriticalLow(String criticalLow) {
        this.criticalLow = criticalLow;
    }

    public String getCriticalHigh() {
        return criticalHigh;
    }

    public void setCriticalHigh(String criticalHigh) {
        this.criticalHigh = criticalHigh;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}