package com.ingestion.opd.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opd_prescriptions")
public class OpdPrescription extends BaseEntity {

    @Column(name = "prescription_number", nullable = false, unique = true)
    private String prescriptionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opd_visit_id", nullable = false)
    private OpdVisit opdVisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @Column(name = "diagnosis", length = 1000)
    private String diagnosis;

    @Column(name = "notes", length = 2000)
    private String notes;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpdMedication> medications = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (prescriptionNumber == null || prescriptionNumber.isEmpty()) {
            // Generate prescription number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            prescriptionNumber = "RX-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
        if (prescriptionDate == null) {
            prescriptionDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public String getPrescriptionNumber() {
        return prescriptionNumber;
    }

    public void setPrescriptionNumber(String prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }

    public OpdVisit getOpdVisit() {
        return opdVisit;
    }

    public void setOpdVisit(OpdVisit opdVisit) {
        this.opdVisit = opdVisit;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDateTime prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OpdMedication> getMedications() {
        return medications;
    }

    public void setMedications(List<OpdMedication> medications) {
        this.medications = medications;
    }

    // Helper methods
    public void addMedication(OpdMedication medication) {
        medications.add(medication);
        medication.setPrescription(this);
    }

    public void removeMedication(OpdMedication medication) {
        medications.remove(medication);
        medication.setPrescription(null);
    }
}