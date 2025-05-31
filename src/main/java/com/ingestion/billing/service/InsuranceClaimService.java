package com.ingestion.billing.service;

import com.ingestion.common.model.billing.InsuranceClaim;
import com.ingestion.billing.model.InsuranceClaimDocument;
import com.ingestion.billing.model.InsurancePolicy;
import com.ingestion.common.model.billing.Invoice;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InsuranceClaimService {

    InsuranceClaim saveInsuranceClaim(InsuranceClaim insuranceClaim);
    
    Optional<InsuranceClaim> findById(Long id);
    
    Optional<InsuranceClaim> findByClaimNumber(String claimNumber);
    
    List<InsuranceClaim> findAll();
    
    List<InsuranceClaim> findByPatient(Patient patient);
    
    List<InsuranceClaim> findByInsurancePolicy(InsurancePolicy insurancePolicy);
    
    List<InsuranceClaim> findByStatus(InsuranceClaim.ClaimStatus status);
    
    List<InsuranceClaim> findByClaimDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<InsuranceClaim> findBySubmissionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<InsuranceClaim> findBySettlementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<InsuranceClaim> findByPatientOrderByClaimDateDesc(Long patientId);
    
    List<InsuranceClaim> findByInsurancePolicyOrderByClaimDateDesc(Long policyId);
    
    List<InsuranceClaim> findByStatusOrderByClaimDateDesc(InsuranceClaim.ClaimStatus status);
    
    Page<InsuranceClaim> findRecentClaims(LocalDateTime startDate, Pageable pageable);
    
    Double getTotalClaimAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Double getTotalApprovedAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Double getTotalSettlementAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Long countByStatus(InsuranceClaim.ClaimStatus status);
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    InsuranceClaim createInsuranceClaim(Patient patient, InsurancePolicy insurancePolicy, 
                                       LocalDateTime admissionDate, LocalDateTime dischargeDate, 
                                       String diagnosis, String treatmentDetails, String notes, 
                                       User createdBy);
    
    InsuranceClaim addInvoiceToClaim(Long claimId, Invoice invoice);
    
    InsuranceClaim removeInvoiceFromClaim(Long claimId, Long invoiceId);
    
    InsuranceClaim calculateClaimAmount(Long claimId);
    
    InsuranceClaim submit(Long claimId);
    
    InsuranceClaim markUnderReview(Long claimId);
    
    InsuranceClaim approve(Long claimId, BigDecimal approvedAmount);
    
    InsuranceClaim reject(Long claimId, String reason);
    
    InsuranceClaim settle(Long claimId, BigDecimal settlementAmount, String reference);
    
    InsuranceClaim cancel(Long claimId);
    
    InsuranceClaimDocument addDocument(Long claimId, InsuranceClaimDocument.DocumentType documentType, 
                                      String documentName, MultipartFile file, String description, 
                                      User uploadedBy);
    
    void removeDocument(Long claimId, Long documentId);
    
    InsuranceClaimDocument verifyDocument(Long documentId, User verifier);
    
    void deleteInsuranceClaim(Long id);
}