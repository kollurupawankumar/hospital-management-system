package com.ingestion.billing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.common.model.billing.InsuranceClaim;
import com.ingestion.patient.model.Patient;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy extends BaseEntity {

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "insurance_provider", nullable = false)
    private String insuranceProvider;

    @Column(name = "policy_type")
    private String policyType;

    @Column(name = "policy_name")
    private String policyName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "coverage_amount", precision = 10, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "remaining_coverage", precision = 10, scale = 2)
    private BigDecimal remainingCoverage;

    @Column(name = "premium_amount", precision = 10, scale = 2)
    private BigDecimal premiumAmount;

    @Column(name = "premium_payment_frequency")
    @Enumerated(EnumType.STRING)
    private PremiumFrequency premiumPaymentFrequency;

    @Column(name = "last_premium_date")
    private LocalDate lastPremiumDate;

    @Column(name = "next_premium_date")
    private LocalDate nextPremiumDate;

    @Column(name = "tpa_name")
    private String tpaName;

    @Column(name = "tpa_id")
    private String tpaId;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PolicyStatus status = PolicyStatus.ACTIVE;

    @Column(name = "notes", length = 1000)
    private String notes;

    @ElementCollection
    @CollectionTable(name = "insurance_policy_dependents", 
                    joinColumns = @JoinColumn(name = "policy_id"))
    @Column(name = "dependent_name")
    private Set<String> dependents = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "insurance_policy_exclusions", 
                    joinColumns = @JoinColumn(name = "policy_id"))
    @Column(name = "exclusion")
    private Set<String> exclusions = new HashSet<>();

    @OneToMany(mappedBy = "insurancePolicy")
    private List<InsuranceClaim> claims = new ArrayList<>();

    public enum PremiumFrequency {
        MONTHLY, QUARTERLY, HALF_YEARLY, YEARLY, ONE_TIME
    }

    public enum PolicyStatus {
        ACTIVE, EXPIRED, CANCELLED, PENDING, SUSPENDED
    }

    // Getters and Setters
    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
        if (this.remainingCoverage == null) {
            this.remainingCoverage = coverageAmount;
        }
    }

    public BigDecimal getRemainingCoverage() {
        return remainingCoverage;
    }

    public void setRemainingCoverage(BigDecimal remainingCoverage) {
        this.remainingCoverage = remainingCoverage;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public PremiumFrequency getPremiumPaymentFrequency() {
        return premiumPaymentFrequency;
    }

    public void setPremiumPaymentFrequency(PremiumFrequency premiumPaymentFrequency) {
        this.premiumPaymentFrequency = premiumPaymentFrequency;
    }

    public LocalDate getLastPremiumDate() {
        return lastPremiumDate;
    }

    public void setLastPremiumDate(LocalDate lastPremiumDate) {
        this.lastPremiumDate = lastPremiumDate;
    }

    public LocalDate getNextPremiumDate() {
        return nextPremiumDate;
    }

    public void setNextPremiumDate(LocalDate nextPremiumDate) {
        this.nextPremiumDate = nextPremiumDate;
    }

    public String getTpaName() {
        return tpaName;
    }

    public void setTpaName(String tpaName) {
        this.tpaName = tpaName;
    }

    public String getTpaId() {
        return tpaId;
    }

    public void setTpaId(String tpaId) {
        this.tpaId = tpaId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<String> getDependents() {
        return dependents;
    }

    public void setDependents(Set<String> dependents) {
        this.dependents = dependents;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    public void setExclusions(Set<String> exclusions) {
        this.exclusions = exclusions;
    }

    public List<InsuranceClaim> getClaims() {
        return claims;
    }

    public void setClaims(List<InsuranceClaim> claims) {
        this.claims = claims;
    }

    // Helper methods
    public void addDependent(String dependent) {
        dependents.add(dependent);
    }

    public void removeDependent(String dependent) {
        dependents.remove(dependent);
    }

    public void addExclusion(String exclusion) {
        exclusions.add(exclusion);
    }

    public void removeExclusion(String exclusion) {
        exclusions.remove(exclusion);
    }

    public void updateRemainingCoverage(BigDecimal claimAmount) {
        if (remainingCoverage != null && claimAmount != null) {
            remainingCoverage = remainingCoverage.subtract(claimAmount);
            if (remainingCoverage.compareTo(BigDecimal.ZERO) < 0) {
                remainingCoverage = BigDecimal.ZERO;
            }
        }
    }

    public void updateNextPremiumDate() {
        if (lastPremiumDate != null && premiumPaymentFrequency != null) {
            switch (premiumPaymentFrequency) {
                case MONTHLY:
                    nextPremiumDate = lastPremiumDate.plusMonths(1);
                    break;
                case QUARTERLY:
                    nextPremiumDate = lastPremiumDate.plusMonths(3);
                    break;
                case HALF_YEARLY:
                    nextPremiumDate = lastPremiumDate.plusMonths(6);
                    break;
                case YEARLY:
                    nextPremiumDate = lastPremiumDate.plusYears(1);
                    break;
                case ONE_TIME:
                    nextPremiumDate = null;
                    break;
            }
        }
    }

    public void recordPremiumPayment(LocalDate paymentDate) {
        this.lastPremiumDate = paymentDate;
        updateNextPremiumDate();
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return status == PolicyStatus.ACTIVE && 
               today.isAfter(startDate.minusDays(1)) && 
               today.isBefore(endDate.plusDays(1));
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    public void checkAndUpdateStatus() {
        if (isExpired()) {
            status = PolicyStatus.EXPIRED;
        }
    }
}