package com.ingestion.doctor.dto;

import jakarta.validation.constraints.NotBlank;


public class PrescriptionItemDTO {
    
    private Long id;
    
    @NotBlank(message = "Medication name is required")
    private String medicationName;
    
    private String dosage;
    
    private String frequency;
    
    private String duration;
    
    private String instructions;
    
    private Boolean isBeforeMeal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Boolean getBeforeMeal() {
        return isBeforeMeal;
    }

    public void setBeforeMeal(Boolean beforeMeal) {
        isBeforeMeal = beforeMeal;
    }
}