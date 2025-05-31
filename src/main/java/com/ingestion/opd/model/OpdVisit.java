package com.ingestion.opd.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.patient.model.Appointment;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opd_visits")
public class OpdVisit extends BaseEntity {

    @Column(name = "token_number", nullable = false)
    private String tokenNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "consultation_start_time")
    private LocalDateTime consultationStartTime;

    @Column(name = "consultation_end_time")
    private LocalDateTime consultationEndTime;

    @Column(name = "chief_complaint", length = 1000)
    private String chiefComplaint;

    @Column(name = "vital_signs", length = 500)
    private String vitalSigns;

    @Column(name = "diagnosis", length = 1000)
    private String diagnosis;

    @Column(name = "treatment_plan", length = 2000)
    private String treatmentPlan;

    @Column(name = "follow_up_instructions", length = 1000)
    private String followUpInstructions;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;

    @Column(name = "notes", length = 2000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status", nullable = false)
    private VisitStatus visitStatus = VisitStatus.REGISTERED;

    @Column(name = "is_billed")
    private Boolean isBilled = false;

    @OneToMany(mappedBy = "opdVisit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpdPrescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "opdVisit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpdLabOrder> labOrders = new ArrayList<>();

    @OneToOne(mappedBy = "opdVisit", cascade = CascadeType.ALL, orphanRemoval = true)
    private OpdBill bill;

    public enum VisitStatus {
        REGISTERED, CHECKED_IN, WAITING, IN_CONSULTATION, COMPLETED, CANCELLED, NO_SHOW
    }

    @PrePersist
    protected void onCreate() {
        if (tokenNumber == null || tokenNumber.isEmpty()) {
            // Generate token number based on date and sequence
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            tokenNumber = "OPD-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
    }

    // Getters and Setters
    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
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

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getConsultationStartTime() {
        return consultationStartTime;
    }

    public void setConsultationStartTime(LocalDateTime consultationStartTime) {
        this.consultationStartTime = consultationStartTime;
    }

    public LocalDateTime getConsultationEndTime() {
        return consultationEndTime;
    }

    public void setConsultationEndTime(LocalDateTime consultationEndTime) {
        this.consultationEndTime = consultationEndTime;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public String getFollowUpInstructions() {
        return followUpInstructions;
    }

    public void setFollowUpInstructions(String followUpInstructions) {
        this.followUpInstructions = followUpInstructions;
    }

    public LocalDateTime getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDateTime followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public VisitStatus getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(VisitStatus visitStatus) {
        this.visitStatus = visitStatus;
    }

    public Boolean getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(Boolean billed) {
        isBilled = billed;
    }

    public List<OpdPrescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<OpdPrescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<OpdLabOrder> getLabOrders() {
        return labOrders;
    }

    public void setLabOrders(List<OpdLabOrder> labOrders) {
        this.labOrders = labOrders;
    }

    public OpdBill getBill() {
        return bill;
    }

    public void setBill(OpdBill bill) {
        this.bill = bill;
    }

    // Helper methods
    public void addPrescription(OpdPrescription prescription) {
        prescriptions.add(prescription);
        prescription.setOpdVisit(this);
    }

    public void removePrescription(OpdPrescription prescription) {
        prescriptions.remove(prescription);
        prescription.setOpdVisit(null);
    }

    public void addLabOrder(OpdLabOrder labOrder) {
        labOrders.add(labOrder);
        labOrder.setOpdVisit(this);
    }

    public void removeLabOrder(OpdLabOrder labOrder) {
        labOrders.remove(labOrder);
        labOrder.setOpdVisit(null);
    }

    public void checkIn() {
        this.checkInTime = LocalDateTime.now();
        this.visitStatus = VisitStatus.CHECKED_IN;
    }

    public void startConsultation() {
        this.consultationStartTime = LocalDateTime.now();
        this.visitStatus = VisitStatus.IN_CONSULTATION;
    }

    public void completeConsultation() {
        this.consultationEndTime = LocalDateTime.now();
        this.visitStatus = VisitStatus.COMPLETED;
    }

    public void markNoShow() {
        this.visitStatus = VisitStatus.NO_SHOW;
    }

    public void cancel() {
        this.visitStatus = VisitStatus.CANCELLED;
    }
}