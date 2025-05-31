package com.ingestion.laboratory.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lab_tests")
public class LabTest extends BaseEntity {

    @Column(name = "test_code", nullable = false, unique = true)
    private String testCode;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "department")
    private String department;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "sample_type")
    private String sampleType;

    @Column(name = "sample_volume")
    private String sampleVolume;

    @Column(name = "container_type")
    private String containerType;
    
    @Column(name = "reference_range", length = 500)
    private String referenceRange;

    @Column(name = "processing_time")
    private String processingTime;

    @Column(name = "fasting_required")
    private Boolean fastingRequired = false;

    @Column(name = "special_instructions", length = 1000)
    private String specialInstructions;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "labTest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestParameter> parameters = new ArrayList<>();

    // Getters and Setters
    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSampleVolume() {
        return sampleVolume;
    }

    public void setSampleVolume(String sampleVolume) {
        this.sampleVolume = sampleVolume;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }
    
    public String getReferenceRange() {
        return referenceRange;
    }
    
    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public Boolean getFastingRequired() {
        return fastingRequired;
    }

    public void setFastingRequired(Boolean fastingRequired) {
        this.fastingRequired = fastingRequired;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public List<TestParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TestParameter> parameters) {
        this.parameters = parameters;
    }

    // Helper methods
    public void addParameter(TestParameter parameter) {
        parameters.add(parameter);
        parameter.setLabTest(this);
    }

    public void removeParameter(TestParameter parameter) {
        parameters.remove(parameter);
        parameter.setLabTest(null);
    }
}