package com.ingestion.laboratory.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "lab_result_items")
public class LabResultItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_result_id", nullable = false)
    private LabResult labResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_parameter_id", nullable = false)
    private TestParameter testParameter;

    @Column(name = "result_value")
    private String resultValue;

    @Column(name = "unit")
    private String unit;

    @Column(name = "reference_range")
    private String referenceRange;

    @Column(name = "is_abnormal")
    private Boolean isAbnormal = false;

    @Column(name = "is_critical")
    private Boolean isCritical = false;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "analyzer_id")
    private String analyzerId;

    @Column(name = "analyzer_result")
    private String analyzerResult;

    // Getters and Setters
    public LabResult getLabResult() {
        return labResult;
    }

    public void setLabResult(LabResult labResult) {
        this.labResult = labResult;
    }

    public TestParameter getTestParameter() {
        return testParameter;
    }

    public void setTestParameter(TestParameter testParameter) {
        this.testParameter = testParameter;
        if (testParameter != null) {
            this.unit = testParameter.getUnit();
            this.referenceRange = testParameter.getReferenceRange();
        }
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
        checkAbnormalAndCriticalFlags();
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAnalyzerId() {
        return analyzerId;
    }

    public void setAnalyzerId(String analyzerId) {
        this.analyzerId = analyzerId;
    }

    public String getAnalyzerResult() {
        return analyzerResult;
    }

    public void setAnalyzerResult(String analyzerResult) {
        this.analyzerResult = analyzerResult;
    }

    // Helper methods
    private void checkAbnormalAndCriticalFlags() {
        if (resultValue == null || testParameter == null) {
            return;
        }

        try {
            // For numeric results, check if they are outside the reference range
            double value = Double.parseDouble(resultValue);
            
            // Check for critical values
            if (testParameter.getCriticalLow() != null && !testParameter.getCriticalLow().isEmpty()) {
                double criticalLow = Double.parseDouble(testParameter.getCriticalLow());
                if (value < criticalLow) {
                    this.isCritical = true;
                    this.isAbnormal = true;
                    return;
                }
            }
            
            if (testParameter.getCriticalHigh() != null && !testParameter.getCriticalHigh().isEmpty()) {
                double criticalHigh = Double.parseDouble(testParameter.getCriticalHigh());
                if (value > criticalHigh) {
                    this.isCritical = true;
                    this.isAbnormal = true;
                    return;
                }
            }
            
            // Check for abnormal values
            if (referenceRange != null && !referenceRange.isEmpty()) {
                // Parse reference range (e.g., "10-20")
                String[] range = referenceRange.split("-");
                if (range.length == 2) {
                    double low = Double.parseDouble(range[0]);
                    double high = Double.parseDouble(range[1]);
                    
                    if (value < low || value > high) {
                        this.isAbnormal = true;
                    } else {
                        this.isAbnormal = false;
                    }
                }
            }
        } catch (NumberFormatException e) {
            // For non-numeric results, we can't automatically determine if they are abnormal
            // This would need to be set manually
        }
    }
}