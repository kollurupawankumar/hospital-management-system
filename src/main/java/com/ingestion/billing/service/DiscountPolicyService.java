package com.ingestion.billing.service;

import com.ingestion.billing.model.DiscountPolicy;
import com.ingestion.security.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountPolicyService {

    DiscountPolicy saveDiscountPolicy(DiscountPolicy discountPolicy);
    
    Optional<DiscountPolicy> findById(Long id);
    
    Optional<DiscountPolicy> findByPolicyCode(String policyCode);
    
    List<DiscountPolicy> findAll();
    
    List<DiscountPolicy> findByPolicyName(String policyName);
    
    List<DiscountPolicy> findByDiscountType(DiscountPolicy.DiscountType discountType);
    
    List<DiscountPolicy> findByIsActive(Boolean isActive);
    
    List<DiscountPolicy> findByApplicableTo(DiscountPolicy.ApplicableTo applicableTo);
    
    List<DiscountPolicy> findByRequiresApproval(Boolean requiresApproval);
    
    List<DiscountPolicy> findAllActiveDiscountPolicies();
    
    List<DiscountPolicy> findActiveByDiscountType(DiscountPolicy.DiscountType discountType);
    
    List<DiscountPolicy> findActiveByApplicableTo(DiscountPolicy.ApplicableTo applicableTo);
    
    List<DiscountPolicy> findActiveByApplicableToOrAll(DiscountPolicy.ApplicableTo applicableTo);
    
    List<DiscountPolicy> findActiveAndEffectiveAsOf(LocalDate date);
    
    List<DiscountPolicy> findActiveAndEffectiveByApplicableToAsOf(
            DiscountPolicy.ApplicableTo applicableTo, LocalDate date);
    
    List<DiscountPolicy.DiscountType> findAllActiveDiscountTypes();
    
    List<DiscountPolicy.ApplicableTo> findAllActiveApplicableTo();
    
    DiscountPolicy createDiscountPolicy(String policyName, String policyCode, 
                                       DiscountPolicy.DiscountType discountType, BigDecimal discountValue, 
                                       String description, LocalDate effectiveFrom, LocalDate effectiveTo, 
                                       DiscountPolicy.ApplicableTo applicableTo, BigDecimal minimumBillAmount, 
                                       BigDecimal maximumDiscountAmount, Boolean requiresApproval, User createdBy);
    
    DiscountPolicy updateDiscountPolicy(Long id, DiscountPolicy discountPolicy, User lastModifiedBy);
    
    DiscountPolicy activateDiscountPolicy(Long id, User lastModifiedBy);
    
    DiscountPolicy deactivateDiscountPolicy(Long id, User lastModifiedBy);
    
    boolean isDiscountApplicable(Long discountPolicyId, BigDecimal billAmount);
    
    BigDecimal calculateDiscountAmount(Long discountPolicyId, BigDecimal billAmount);
    
    BigDecimal calculateDiscountAmountByApplicableTo(DiscountPolicy.ApplicableTo applicableTo, BigDecimal billAmount);
    
    void deleteDiscountPolicy(Long id);
}