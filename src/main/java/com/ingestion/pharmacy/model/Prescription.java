package com.ingestion.pharmacy.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription extends BaseEntity {

    @Column(name = "prescription_number", nullable = false, unique = true)
    private String prescriptionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opd_visit_id")
    private OpdVisit opdVisit;

    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @Column(name = "diagnosis", length = 500)
    private String diagnosis;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;

    @Column(name = "is_dispensed")
    private Boolean isDispensed = false;

    @Column(name = "dispensed_date")
    private LocalDateTime dispensedDate;

    @Column(name = "dispensed_by")
    private String dispensedBy;

    @Column(name = "is_billed")
    private Boolean isBilled = false;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionItem> prescriptionItems = new ArrayList<>();

    public enum PrescriptionStatus {
        ACTIVE, COMPLETED, CANCELLED, EXPIRED
    }

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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public OpdVisit getOpdVisit() {
        return opdVisit;
    }

    public void setOpdVisit(OpdVisit opdVisit) {
        this.opdVisit = opdVisit;
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

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public Boolean getIsDispensed() {
        return isDispensed;
    }

    public void setIsDispensed(Boolean dispensed) {
        isDispensed = dispensed;
    }

    public LocalDateTime getDispensedDate() {
        return dispensedDate;
    }

    public void setDispensedDate(LocalDateTime dispensedDate) {
        this.dispensedDate = dispensedDate;
    }

    public String getDispensedBy() {
        return dispensedBy;
    }

    public void setDispensedBy(String dispensedBy) {
        this.dispensedBy = dispensedBy;
    }

    public Boolean getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(Boolean billed) {
        isBilled = billed;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean paid) {
        isPaid = paid;
    }

    public List<PrescriptionItem> getPrescriptionItems() {
        return prescriptionItems;
    }

    public void setPrescriptionItems(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
    }

    // Helper methods
    public void addPrescriptionItem(PrescriptionItem item) {
        prescriptionItems.add(item);
        item.setPrescription(this);
    }

    public void removePrescriptionItem(PrescriptionItem item) {
        prescriptionItems.remove(item);
        item.setPrescription(null);
    }

    public void markAsDispensed(String dispensedBy) {
        this.isDispensed = true;
        this.dispensedDate = LocalDateTime.now();
        this.dispensedBy = dispensedBy;
        this.status = PrescriptionStatus.COMPLETED;
    }

    public void markAsBilled() {
        this.isBilled = true;
    }

    public void markAsPaid() {
        this.isPaid = true;
    }

    public void cancel() {
        this.status = PrescriptionStatus.CANCELLED;
    }

    public void expire() {
        this.status = PrescriptionStatus.EXPIRED;
    }

    public boolean isActive() {
        return this.status == PrescriptionStatus.ACTIVE;
    }
}