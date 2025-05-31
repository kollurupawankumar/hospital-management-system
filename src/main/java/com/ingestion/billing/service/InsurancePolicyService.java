package com.ingestion.billing.service;

import com.ingestion.billing.model.InsurancePolicy;
import com.ingestion.patient.model.Patient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InsurancePolicyService {

    InsurancePolicy saveInsurancePolicy(InsurancePolicy insurancePolicy);
    
    Optional<InsurancePolicy> findById(Long id);
    
    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);
    
    List<InsurancePolicy> findAll();
    
    List<InsurancePolicy> findByPatient(Patient patient);
    
    List<InsurancePolicy> findByInsuranceProvider(String insuranceProvider);
    
    List<InsurancePolicy> findByPolicyType(String policyType);
    
    List<InsurancePolicy> findByStatus(InsurancePolicy.PolicyStatus status);
    
    List<InsurancePolicy> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<InsurancePolicy> findByEndDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<InsurancePolicy> findByPatientOrderByEndDateDesc(Long patientId);
    
    List<InsurancePolicy> findByStatusOrderByEndDateDesc(InsurancePolicy.PolicyStatus status);
    
    List<InsurancePolicy> findExpiredPolicies();
    
    List<InsurancePolicy> findPoliciesExpiringBefore(LocalDate expiryDate);
    
    List<InsurancePolicy> findPoliciesWithPremiumDueBefore(LocalDate dueDate);
    
    List<String> findAllInsuranceProviders();
    
    List<String> findAllPolicyTypes();
    
    Long countByStatus(InsurancePolicy.PolicyStatus status);
    
    Long countExpiredPolicies();
    
    Long countPoliciesExpiringBefore(LocalDate expiryDate);
    
    InsurancePolicy createInsurancePolicy(Patient patient, String policyNumber, String insuranceProvider, 
                                         String policyType, String policyName, LocalDate startDate, 
                                         LocalDate endDate, BigDecimal coverageAmount, BigDecimal premiumAmount, 
                                         InsurancePolicy.PremiumFrequency premiumPaymentFrequency, 
                                         String tpaName, String tpaId, String contactPerson, 
                                         String contactNumber, String contactEmail, String notes);
    
    InsurancePolicy updateInsurancePolicy(Long id, InsurancePolicy insurancePolicy);
    
    InsurancePolicy updateDependents(Long id, Set<String> dependents);
    
    InsurancePolicy updateExclusions(Long id, Set<String> exclusions);
    
    InsurancePolicy updateRemainingCoverage(Long id, BigDecimal claimAmount);
    
    InsurancePolicy recordPremiumPayment(Long id, LocalDate paymentDate);
    
    InsurancePolicy activatePolicy(Long id);
    
    InsurancePolicy expirePolicy(Long id);
    
    InsurancePolicy cancelPolicy(Long id);
    
    InsurancePolicy suspendPolicy(Long id);
    
    void checkAndUpdatePolicyStatuses();
    
    void deleteInsurancePolicy(Long id);
}