package com.ingestion.billing.repository;

import com.ingestion.billing.model.InsurancePolicy;
import com.ingestion.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);
    
    List<InsurancePolicy> findByPatient(Patient patient);
    
    List<InsurancePolicy> findByInsuranceProvider(String insuranceProvider);
    
    List<InsurancePolicy> findByPolicyType(String policyType);
    
    List<InsurancePolicy> findByStatus(InsurancePolicy.PolicyStatus status);
    
    List<InsurancePolicy> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<InsurancePolicy> findByEndDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT ip FROM InsurancePolicy ip WHERE ip.patient.id = :patientId ORDER BY ip.endDate DESC")
    List<InsurancePolicy> findByPatientOrderByEndDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT ip FROM InsurancePolicy ip WHERE ip.status = :status ORDER BY ip.endDate DESC")
    List<InsurancePolicy> findByStatusOrderByEndDateDesc(@Param("status") InsurancePolicy.PolicyStatus status);
    
    @Query("SELECT ip FROM InsurancePolicy ip WHERE ip.endDate < CURRENT_DATE AND ip.status = 'ACTIVE'")
    List<InsurancePolicy> findExpiredPolicies();
    
    @Query("SELECT ip FROM InsurancePolicy ip WHERE ip.endDate BETWEEN CURRENT_DATE AND :expiryDate AND ip.status = 'ACTIVE'")
    List<InsurancePolicy> findPoliciesExpiringBefore(@Param("expiryDate") LocalDate expiryDate);
    
    @Query("SELECT ip FROM InsurancePolicy ip WHERE ip.nextPremiumDate BETWEEN CURRENT_DATE AND :dueDate AND ip.status = 'ACTIVE'")
    List<InsurancePolicy> findPoliciesWithPremiumDueBefore(@Param("dueDate") LocalDate dueDate);
    
    @Query("SELECT DISTINCT ip.insuranceProvider FROM InsurancePolicy ip ORDER BY ip.insuranceProvider")
    List<String> findAllInsuranceProviders();
    
    @Query("SELECT DISTINCT ip.policyType FROM InsurancePolicy ip WHERE ip.policyType IS NOT NULL ORDER BY ip.policyType")
    List<String> findAllPolicyTypes();
    
    @Query("SELECT COUNT(ip) FROM InsurancePolicy ip WHERE ip.status = :status")
    Long countByStatus(@Param("status") InsurancePolicy.PolicyStatus status);
    
    @Query("SELECT COUNT(ip) FROM InsurancePolicy ip WHERE ip.endDate < CURRENT_DATE AND ip.status = 'ACTIVE'")
    Long countExpiredPolicies();
    
    @Query("SELECT COUNT(ip) FROM InsurancePolicy ip WHERE ip.endDate BETWEEN CURRENT_DATE AND :expiryDate AND ip.status = 'ACTIVE'")
    Long countPoliciesExpiringBefore(@Param("expiryDate") LocalDate expiryDate);
}