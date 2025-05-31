package com.ingestion.patient.dto;

import com.ingestion.patient.model.MedicalRecord;
import com.ingestion.patient.model.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;


public class MedicalRecordDTO {
    
    private Long id;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private String patientName;
    
    private LocalDateTime recordDate;
    
    @NotBlank(message = "Record type is required")
    private String recordType;
    
    private String diagnosis;
    
    private String treatment;
    
    private String prescription;
    
    private String notes;
    
    private String doctorName;
    
    private String vitalSigns;
    
    private String labResults;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public String getLabResults() {
        return labResults;
    }

    public void setLabResults(String labResults) {
        this.labResults = labResults;
    }

    // Convert Entity to DTO
    public static MedicalRecordDTO fromEntity(MedicalRecord medicalRecord) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(medicalRecord.getId());
        
        if (medicalRecord.getPatient() != null) {
            dto.setPatientId(medicalRecord.getPatient().getId());
            dto.setPatientName(medicalRecord.getPatient().getFirstName() + " " + 
                    medicalRecord.getPatient().getLastName());
        }
        
        dto.setRecordDate(medicalRecord.getRecordDate());
        dto.setRecordType(medicalRecord.getRecordType());
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setTreatment(medicalRecord.getTreatment());
        dto.setPrescription(medicalRecord.getPrescription());
        dto.setNotes(medicalRecord.getNotes());
        dto.setDoctorName(medicalRecord.getDoctorName());
        dto.setVitalSigns(medicalRecord.getVitalSigns());
        dto.setLabResults(medicalRecord.getLabResults());
        
        return dto;
    }
    
    // Convert DTO to Entity
    public MedicalRecord toEntity(Patient patient) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(this.id);
        medicalRecord.setPatient(patient);
        medicalRecord.setRecordDate(this.recordDate != null ? this.recordDate : LocalDateTime.now());
        medicalRecord.setRecordType(this.recordType);
        medicalRecord.setDiagnosis(this.diagnosis);
        medicalRecord.setTreatment(this.treatment);
        medicalRecord.setPrescription(this.prescription);
        medicalRecord.setNotes(this.notes);
        medicalRecord.setDoctorName(this.doctorName);
        medicalRecord.setVitalSigns(this.vitalSigns);
        medicalRecord.setLabResults(this.labResults);
        
        return medicalRecord;
    }
}