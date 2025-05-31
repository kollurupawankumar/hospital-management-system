package com.ingestion.common.model.billing;

import com.ingestion.billing.model.InsuranceClaimDocument;
import com.ingestion.billing.model.InsurancePolicy;
import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim extends BaseEntity {

    @Column(name = "claim_number", nullable = false, unique = true)
    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Column(name = "claim_date", nullable = false)
    private LocalDateTime claimDate;

    @Column(name = "admission_date")
    private LocalDateTime admissionDate;

    @Column(name = "discharge_date")
    private LocalDateTime dischargeDate;

    @Column(name = "diagnosis", length = 1000)
    private String diagnosis;

    @Column(name = "treatment_details", length = 1000)
    private String treatmentDetails;

    @Column(name = "claim_amount", precision = 10, scale = 2)
    private BigDecimal claimAmount;

    @Column(name = "approved_amount", precision = 10, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "rejected_amount", precision = 10, scale = 2)
    private BigDecimal rejectedAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.DRAFT;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "rejection_date")
    private LocalDateTime rejectionDate;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;

    @Column(name = "settlement_amount", precision = 10, scale = 2)
    private BigDecimal settlementAmount;

    @Column(name = "settlement_reference")
    private String settlementReference;

    @Column(name = "notes", length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "insuranceClaim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InsuranceClaimDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "insuranceClaim")
    private List<Invoice> invoices = new ArrayList<>();

    public enum ClaimStatus {
        DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, PARTIALLY_APPROVED, REJECTED, SETTLED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (claimNumber == null || claimNumber.isEmpty()) {
            // Generate claim number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            claimNumber = "CLM-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
        if (claimDate == null) {
            claimDate = LocalDateTime.now();
        }
        
        // Set the string version of creator for BaseEntity
        if (creator != null) {
            setCreatedBy(creator.getUsername());
        }
    }

    // Getters and Setters
    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDateTime claimDate) {
        this.claimDate = claimDate;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateTime getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentDetails() {
        return treatmentDetails;
    }

    public void setTreatmentDetails(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public BigDecimal getRejectedAmount() {
        return rejectedAmount;
    }

    public void setRejectedAmount(BigDecimal rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getRejectionDate() {
        return rejectionDate;
    }

    public void setRejectionDate(LocalDateTime rejectionDate) {
        this.rejectionDate = rejectionDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDateTime settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementReference() {
        return settlementReference;
    }

    public void setSettlementReference(String settlementReference) {
        this.settlementReference = settlementReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        // Also set the string version for BaseEntity
        setCreatedBy(creator != null ? creator.getUsername() : null);
    }

    public List<InsuranceClaimDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<InsuranceClaimDocument> documents) {
        this.documents = documents;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    // Helper methods
    public void addDocument(InsuranceClaimDocument document) {
        documents.add(document);
        document.setInsuranceClaim(this);
    }

    public void removeDocument(InsuranceClaimDocument document) {
        documents.remove(document);
        document.setInsuranceClaim(null);
    }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
        invoice.setInsuranceClaim(this);
    }

    public void removeInvoice(Invoice invoice) {
        invoices.remove(invoice);
        invoice.setInsuranceClaim(null);
    }

    public void submit() {
        this.status = ClaimStatus.SUBMITTED;
        this.submissionDate = LocalDateTime.now();
    }

    public void markUnderReview() {
        this.status = ClaimStatus.UNDER_REVIEW;
    }

    public void approve(BigDecimal approvedAmount) {
        this.status = ClaimStatus.APPROVED;
        this.approvalDate = LocalDateTime.now();
        this.approvedAmount = approvedAmount;
        
        if (approvedAmount.compareTo(claimAmount) < 0) {
            this.status = ClaimStatus.PARTIALLY_APPROVED;
            this.rejectedAmount = claimAmount.subtract(approvedAmount);
        }
    }

    public void reject(String reason) {
        this.status = ClaimStatus.REJECTED;
        this.rejectionDate = LocalDateTime.now();
        this.rejectionReason = reason;
        this.rejectedAmount = claimAmount;
        this.approvedAmount = BigDecimal.ZERO;
    }

    public void settle(BigDecimal settlementAmount, String reference) {
        this.status = ClaimStatus.SETTLED;
        this.settlementDate = LocalDateTime.now();
        this.settlementAmount = settlementAmount;
        this.settlementReference = reference;
    }

    public void cancel() {
        this.status = ClaimStatus.CANCELLED;
    }

    public BigDecimal calculateTotalClaimAmount() {
        BigDecimal total = BigDecimal.ZERO;
        
        for (Invoice invoice : invoices) {
            for (InvoiceItem item : invoice.getInvoiceItems()) {
                if (item.getIsInsuranceCovered() && item.getInsuranceCoveredAmount() != null) {
                    total = total.add(item.getInsuranceCoveredAmount());
                }
            }
        }
        
        this.claimAmount = total;
        return total;
    }
}