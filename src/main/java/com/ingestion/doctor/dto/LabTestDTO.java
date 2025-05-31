package com.ingestion.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class LabTestDTO {
    
    private Long id;
    
    @NotBlank(message = "Test name is required")
    private String testName;
    
    private String testCode;
    
    private String testCategory;
    
    private String specialInstructions;
    
    public LabTestDTO() {
    }
    
    public LabTestDTO(Long id, String testName, String testCode, String testCategory, String specialInstructions) {
        this.id = id;
        this.testName = testName;
        this.testCode = testCode;
        this.testCategory = testCategory;
        this.specialInstructions = specialInstructions;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTestName() {
        return testName;
    }
    
    public void setTestName(String testName) {
        this.testName = testName;
    }
    
    public String getTestCode() {
        return testCode;
    }
    
    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }
    
    public String getTestCategory() {
        return testCategory;
    }
    
    public void setTestCategory(String testCategory) {
        this.testCategory = testCategory;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabTestDTO that = (LabTestDTO) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(testName, that.testName) &&
               Objects.equals(testCode, that.testCode) &&
               Objects.equals(testCategory, that.testCategory) &&
               Objects.equals(specialInstructions, that.specialInstructions);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, testName, testCode, testCategory, specialInstructions);
    }
    
    @Override
    public String toString() {
        return "LabTestDTO{" +
                "id=" + id +
                ", testName='" + testName + '\'' +
                ", testCode='" + testCode + '\'' +
                ", testCategory='" + testCategory + '\'' +
                ", specialInstructions='" + specialInstructions + '\'' +
                '}';
    }
}