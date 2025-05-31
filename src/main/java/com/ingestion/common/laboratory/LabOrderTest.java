package com.ingestion.common.laboratory;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.laboratory.model.LabTest;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a test ordered as part of a lab order.
 */
@Entity
@Table(name = "lab_order_tests")
public class LabOrderTest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTest labTest;

    @Column(name = "test_price", precision = 10, scale = 2)
    private BigDecimal testPrice;

    @Column(name = "result_value", length = 1000)
    private String resultValue;

    @Column(name = "reference_range", length = 500)
    private String referenceRange;

    @Column(name = "result_notes", length = 1000)
    private String resultNotes;

    @Column(name = "result_date")
    private LocalDateTime resultDate;

    @Column(name = "is_abnormal")
    private Boolean isAbnormal = false;

    @Column(name = "is_critical")
    private Boolean isCritical = false;

    // Getters and Setters
    public LabOrder getLabOrder() {
        return labOrder;
    }

    public void setLabOrder(LabOrder labOrder) {
        this.labOrder = labOrder;
    }

    public LabTest getLabTest() {
        return labTest;
    }

    public void setLabTest(LabTest labTest) {
        this.labTest = labTest;
        if (labTest != null) {
            this.testPrice = labTest.getPrice();
            this.referenceRange = labTest.getReferenceRange();
        }
    }

    public BigDecimal getTestPrice() {
        return testPrice;
    }

    public void setTestPrice(BigDecimal testPrice) {
        this.testPrice = testPrice;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getResultNotes() {
        return resultNotes;
    }

    public void setResultNotes(String resultNotes) {
        this.resultNotes = resultNotes;
    }

    public LocalDateTime getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDateTime resultDate) {
        this.resultDate = resultDate;
    }

    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean abnormal) {
        isAbnormal = abnormal;
    }

    public Boolean getIsCritical() {
        return isCritical;
    }

    public void setIsCritical(Boolean critical) {
        isCritical = critical;
    }

    // Helper methods
    public void markAsAbnormal() {
        this.isAbnormal = true;
    }

    public void markAsCritical() {
        this.isAbnormal = true;
        this.isCritical = true;
    }

    public void addResult(String value, String notes, boolean isAbnormal, boolean isCritical) {
        this.resultValue = value;
        this.resultNotes = notes;
        this.isAbnormal = isAbnormal;
        this.isCritical = isCritical;
        this.resultDate = LocalDateTime.now();
    }
}