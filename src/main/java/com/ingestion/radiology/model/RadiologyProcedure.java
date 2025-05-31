package com.ingestion.radiology.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "radiology_procedures")
public class RadiologyProcedure extends BaseEntity {

    @Column(name = "procedure_code", nullable = false, unique = true)
    private String procedureCode;

    @Column(name = "procedure_name", nullable = false)
    private String procedureName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "modality", nullable = false)
    @Enumerated(EnumType.STRING)
    private Modality modality;

    @Column(name = "body_part")
    private String bodyPart;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "preparation_instructions", length = 1000)
    private String preparationInstructions;

    @Column(name = "contrast_required")
    private Boolean contrastRequired = false;

    @Column(name = "contrast_type")
    private String contrastType;

    @Column(name = "radiation_dose")
    private String radiationDose;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public enum Modality {
        XRAY, CT, MRI, ULTRASOUND, MAMMOGRAPHY, FLUOROSCOPY, DEXA, PET, NUCLEAR_MEDICINE
    }

    // Getters and Setters
    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getPreparationInstructions() {
        return preparationInstructions;
    }

    public void setPreparationInstructions(String preparationInstructions) {
        this.preparationInstructions = preparationInstructions;
    }

    public Boolean getContrastRequired() {
        return contrastRequired;
    }

    public void setContrastRequired(Boolean contrastRequired) {
        this.contrastRequired = contrastRequired;
    }

    public String getContrastType() {
        return contrastType;
    }

    public void setContrastType(String contrastType) {
        this.contrastType = contrastType;
    }

    public String getRadiationDose() {
        return radiationDose;
    }

    public void setRadiationDose(String radiationDose) {
        this.radiationDose = radiationDose;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}