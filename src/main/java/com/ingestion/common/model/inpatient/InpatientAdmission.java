package com.ingestion.common.model.inpatient;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.doctor.model.DischargeSummary;
import com.ingestion.doctor.model.InpatientNote;
import com.ingestion.doctor.model.RoundNote;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inpatient_admissions")
public class InpatientAdmission extends BaseEntity {

    @Column(name = "admission_number", unique = true, nullable = false)
    private String admissionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitting_doctor_id", nullable = false)
    private Doctor admittingDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_nurse_id")
    private Nurse assignedNurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed assignedBed;

    @Column(name = "admission_date", nullable = false)
    private LocalDateTime admissionDate;

    @Column(name = "expected_discharge_date")
    private LocalDateTime expectedDischargeDate;

    @Column(name = "actual_discharge_date")
    private LocalDateTime actualDischargeDate;
    
    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Column(name = "admission_reason", length = 1000)
    private String admissionReason;
    
    @Column(name = "admission_diagnosis", length = 1000)
    private String admissionDiagnosis;

    @Column(name = "admission_notes", length = 2000)
    private String admissionNotes;
    
    @Column(name = "discharge_notes", length = 2000)
    private String dischargeNotes;

    @Column(name = "discharge_summary", length = 2000)
    private String dischargeSummary;

    @Column(name = "discharge_instructions", length = 2000)
    private String dischargeInstructions;
    
    @Column(name = "room_number")
    private String roomNumber;
    
    @Column(name = "bed_number")
    private String bedNumber;
    
    @Column(name = "ward")
    private String ward;

    @Enumerated(EnumType.STRING)
    @Column(name = "admission_status", nullable = false)
    private AdmissionStatus admissionStatus = AdmissionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "admission_type", nullable = false)
    private AdmissionType admissionType;

    @Column(name = "is_emergency")
    private Boolean isEmergency = false;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalNote> clinicalNotes = new ArrayList<>();

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VitalSign> vitalSigns = new ArrayList<>();

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DietOrder> dietOrders = new ArrayList<>();

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NursingInstruction> nursingInstructions = new ArrayList<>();

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepartmentTransfer> departmentTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InpatientNote> inpatientNotes = new ArrayList<>();

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoundNote> roundNotes = new ArrayList<>();

    @OneToOne(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private DischargeSummary dischargeSummaryDoc;

    @PrePersist
    protected void onCreate() {
        if (admissionDate == null) {
            admissionDate = LocalDateTime.now();
        }
        
        // Generate admission number
        if (admissionNumber == null || admissionNumber.isEmpty()) {
            admissionNumber = "ADM" + System.currentTimeMillis();
        }
        
        // Update bed status
        if (assignedBed != null) {
            assignedBed.setStatus(Bed.BedStatus.OCCUPIED);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Update bed status if discharged
        if (admissionStatus == AdmissionStatus.DISCHARGED && assignedBed != null) {
            assignedBed.setStatus(Bed.BedStatus.CLEANING);
        }
    }

    public enum AdmissionStatus {
        PENDING, ADMITTED, DISCHARGED, TRANSFERRED, CANCELLED, DECEASED, LEFT_AGAINST_ADVICE
    }

    public enum AdmissionType {
        ELECTIVE, EMERGENCY, MATERNITY, SURGICAL, MEDICAL, PEDIATRIC, PSYCHIATRIC, OBSERVATION, OTHER
    }

    // Getters and Setters
    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getAdmittingDoctor() {
        return admittingDoctor;
    }

    public void setAdmittingDoctor(Doctor admittingDoctor) {
        this.admittingDoctor = admittingDoctor;
    }

    public Nurse getAssignedNurse() {
        return assignedNurse;
    }

    public void setAssignedNurse(Nurse assignedNurse) {
        this.assignedNurse = assignedNurse;
    }

    public Bed getAssignedBed() {
        return assignedBed;
    }

    public void setAssignedBed(Bed assignedBed) {
        this.assignedBed = assignedBed;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateTime getExpectedDischargeDate() {
        return expectedDischargeDate;
    }

    public void setExpectedDischargeDate(LocalDateTime expectedDischargeDate) {
        this.expectedDischargeDate = expectedDischargeDate;
    }

    public LocalDateTime getActualDischargeDate() {
        return actualDischargeDate;
    }

    public void setActualDischargeDate(LocalDateTime actualDischargeDate) {
        this.actualDischargeDate = actualDischargeDate;
    }

    public String getAdmissionReason() {
        return admissionReason;
    }

    public void setAdmissionReason(String admissionReason) {
        this.admissionReason = admissionReason;
    }

    public String getAdmissionNotes() {
        return admissionNotes;
    }

    public void setAdmissionNotes(String admissionNotes) {
        this.admissionNotes = admissionNotes;
    }

    public String getDischargeSummary() {
        return dischargeSummary;
    }

    public void setDischargeSummary(String dischargeSummary) {
        this.dischargeSummary = dischargeSummary;
    }

    public String getDischargeInstructions() {
        return dischargeInstructions;
    }

    public void setDischargeInstructions(String dischargeInstructions) {
        this.dischargeInstructions = dischargeInstructions;
    }

    public AdmissionStatus getAdmissionStatus() {
        return admissionStatus;
    }

    public void setAdmissionStatus(AdmissionStatus admissionStatus) {
        this.admissionStatus = admissionStatus;
    }

    public AdmissionType getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType(AdmissionType admissionType) {
        this.admissionType = admissionType;
    }

    public Boolean getEmergency() {
        return isEmergency;
    }

    public void setEmergency(Boolean emergency) {
        isEmergency = emergency;
    }

    public List<ClinicalNote> getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(List<ClinicalNote> clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public List<VitalSign> getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(List<VitalSign> vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public List<DietOrder> getDietOrders() {
        return dietOrders;
    }

    public void setDietOrders(List<DietOrder> dietOrders) {
        this.dietOrders = dietOrders;
    }

    public List<NursingInstruction> getNursingInstructions() {
        return nursingInstructions;
    }

    public void setNursingInstructions(List<NursingInstruction> nursingInstructions) {
        this.nursingInstructions = nursingInstructions;
    }

    public List<DepartmentTransfer> getDepartmentTransfers() {
        return departmentTransfers;
    }

    public void setDepartmentTransfers(List<DepartmentTransfer> departmentTransfers) {
        this.departmentTransfers = departmentTransfers;
    }

    public List<InpatientNote> getInpatientNotes() {
        return inpatientNotes;
    }

    public void setInpatientNotes(List<InpatientNote> inpatientNotes) {
        this.inpatientNotes = inpatientNotes;
    }

    public List<RoundNote> getRoundNotes() {
        return roundNotes;
    }

    public void setRoundNotes(List<RoundNote> roundNotes) {
        this.roundNotes = roundNotes;
    }

    public DischargeSummary getDischargeSummaryDoc() {
        return dischargeSummaryDoc;
    }

    public void setDischargeSummaryDoc(DischargeSummary dischargeSummaryDoc) {
        this.dischargeSummaryDoc = dischargeSummaryDoc;
    }

    // Helper methods
    public void addClinicalNote(ClinicalNote note) {
        clinicalNotes.add(note);
        note.setAdmission(this);
    }

    public void removeClinicalNote(ClinicalNote note) {
        clinicalNotes.remove(note);
        note.setAdmission(null);
    }

    public void addVitalSign(VitalSign vitalSign) {
        vitalSigns.add(vitalSign);
        vitalSign.setAdmission(this);
    }

    public void removeVitalSign(VitalSign vitalSign) {
        vitalSigns.remove(vitalSign);
        vitalSign.setAdmission(null);
    }

    public void addDietOrder(DietOrder dietOrder) {
        dietOrders.add(dietOrder);
        dietOrder.setAdmission(this);
    }

    public void removeDietOrder(DietOrder dietOrder) {
        dietOrders.remove(dietOrder);
        dietOrder.setAdmission(null);
    }

    public void addNursingInstruction(NursingInstruction instruction) {
        nursingInstructions.add(instruction);
        instruction.setAdmission(this);
    }

    public void removeNursingInstruction(NursingInstruction instruction) {
        nursingInstructions.remove(instruction);
        instruction.setAdmission(null);
    }

    public void addDepartmentTransfer(DepartmentTransfer transfer) {
        departmentTransfers.add(transfer);
        transfer.setAdmission(this);
    }

    public void removeDepartmentTransfer(DepartmentTransfer transfer) {
        departmentTransfers.remove(transfer);
        transfer.setAdmission(null);
    }

    public void addInpatientNote(InpatientNote note) {
        inpatientNotes.add(note);
        note.setAdmission(this);
    }

    public void removeInpatientNote(InpatientNote note) {
        inpatientNotes.remove(note);
        note.setAdmission(null);
    }

    public void addRoundNote(RoundNote note) {
        roundNotes.add(note);
        note.setAdmission(this);
    }

    public void removeRoundNote(RoundNote note) {
        roundNotes.remove(note);
        note.setAdmission(null);
    }

    // Method to discharge patient
    public void dischargePatient(LocalDateTime dischargeDate, String summary, String instructions) {
        this.admissionStatus = AdmissionStatus.DISCHARGED;
        this.actualDischargeDate = dischargeDate;
        this.dischargeDate = dischargeDate.toLocalDate();
        this.dischargeSummary = summary;
        this.dischargeInstructions = instructions;
        
        // Free up the bed
        if (this.assignedBed != null) {
            this.assignedBed.setStatus(Bed.BedStatus.CLEANING);
        }
    }
    
    // Getters and setters for new fields
    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getAdmissionDiagnosis() {
        return admissionDiagnosis;
    }

    public void setAdmissionDiagnosis(String admissionDiagnosis) {
        this.admissionDiagnosis = admissionDiagnosis;
    }

    public String getDischargeNotes() {
        return dischargeNotes;
    }

    public void setDischargeNotes(String dischargeNotes) {
        this.dischargeNotes = dischargeNotes;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}