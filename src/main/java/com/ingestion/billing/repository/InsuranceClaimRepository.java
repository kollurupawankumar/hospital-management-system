package com.ingestion.billing.repository;

import com.ingestion.common.model.billing.InsuranceClaim;
import com.ingestion.billing.model.InsurancePolicy;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {

    Optional<InsuranceClaim> findByClaimNumber(String claimNumber);
    
    List<InsuranceClaim> findByPatient(Patient patient);
    
    List<InsuranceClaim> findByInsurancePolicy(InsurancePolicy insurancePolicy);
    
    List<InsuranceClaim> findByStatus(InsuranceClaim.ClaimStatus status);
    
    List<InsuranceClaim> findByClaimDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<InsuranceClaim> findBySubmissionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<InsuranceClaim> findBySettlementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT ic FROM InsuranceClaim ic WHERE ic.patient.id = :patientId ORDER BY ic.claimDate DESC")
    List<InsuranceClaim> findByPatientOrderByClaimDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT ic FROM InsuranceClaim ic WHERE ic.insurancePolicy.id = :policyId ORDER BY ic.claimDate DESC")
    List<InsuranceClaim> findByInsurancePolicyOrderByClaimDateDesc(@Param("policyId") Long policyId);
    
    @Query("SELECT ic FROM InsuranceClaim ic WHERE ic.status = :status ORDER BY ic.claimDate DESC")
    List<InsuranceClaim> findByStatusOrderByClaimDateDesc(@Param("status") InsuranceClaim.ClaimStatus status);
    
    @Query("SELECT ic FROM InsuranceClaim ic WHERE ic.claimDate >= :startDate ORDER BY ic.claimDate DESC")
    Page<InsuranceClaim> findRecentClaims(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT SUM(ic.claimAmount) FROM InsuranceClaim ic WHERE ic.claimDate BETWEEN :startDate AND :endDate")
    Double getTotalClaimAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(ic.approvedAmount) FROM InsuranceClaim ic WHERE ic.approvalDate BETWEEN :startDate AND :endDate")
    Double getTotalApprovedAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(ic.settlementAmount) FROM InsuranceClaim ic WHERE ic.settlementDate BETWEEN :startDate AND :endDate")
    Double getTotalSettlementAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(ic) FROM InsuranceClaim ic WHERE ic.status = :status")
    Long countByStatus(@Param("status") InsuranceClaim.ClaimStatus status);
    
    @Query("SELECT COUNT(ic) FROM InsuranceClaim ic WHERE ic.claimDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}