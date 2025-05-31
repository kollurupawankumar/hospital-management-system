package com.ingestion.billing.repository;

import com.ingestion.billing.model.DiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicy, Long> {

    Optional<DiscountPolicy> findByPolicyCode(String policyCode);
    
    List<DiscountPolicy> findByPolicyName(String policyName);
    
    List<DiscountPolicy> findByDiscountType(DiscountPolicy.DiscountType discountType);
    
    List<DiscountPolicy> findByIsActive(Boolean isActive);
    
    List<DiscountPolicy> findByApplicableTo(DiscountPolicy.ApplicableTo applicableTo);
    
    List<DiscountPolicy> findByRequiresApproval(Boolean requiresApproval);
    
    @Query("SELECT dp FROM DiscountPolicy dp WHERE dp.isActive = true ORDER BY dp.policyName")
    List<DiscountPolicy> findAllActiveDiscountPolicies();
    
    @Query("SELECT dp FROM DiscountPolicy dp WHERE dp.isActive = true AND dp.discountType = :discountType ORDER BY dp.policyName")
    List<DiscountPolicy> findActiveByDiscountType(@Param("discountType") DiscountPolicy.DiscountType discountType);
    
    @Query("SELECT dp FROM DiscountPolicy dp WHERE dp.isActive = true AND dp.applicableTo = :applicableTo ORDER BY dp.policyName")
    List<DiscountPolicy> findActiveByApplicableTo(@Param("applicableTo") DiscountPolicy.ApplicableTo applicableTo);
    
    @Query("SELECT dp FROM DiscountPolicy dp WHERE dp.isActive = true AND (dp.applicableTo = :applicableTo OR dp.applicableTo = 'ALL') ORDER BY dp.policyName")
    List<DiscountPolicy> findActiveByApplicableToOrAll(@Param("applicableTo") DiscountPolicy.ApplicableTo applicableTo);
    
    @Query("SELECT dp FROM DiscountPolicy dp WHERE dp.isActive = true AND " +
           "(dp.effectiveFrom IS NULL OR dp.effectiveFrom <= :date) AND " +
           "(dp.effectiveTo IS NULL OR dp.effectiveTo >= :date) ORDER BY dp.policyName")
    List<DiscountPolicy> findActiveAndEffectiveAsOf(@Param("date") LocalDate date);
    
    @Query("SELECT dp FROM DiscountPolicy dp WHERE dp.isActive = true AND " +
           "(dp.effectiveFrom IS NULL OR dp.effectiveFrom <= :date) AND " +
           "(dp.effectiveTo IS NULL OR dp.effectiveTo >= :date) AND " +
           "(dp.applicableTo = :applicableTo OR dp.applicableTo = 'ALL') ORDER BY dp.policyName")
    List<DiscountPolicy> findActiveAndEffectiveByApplicableToAsOf(
            @Param("applicableTo") DiscountPolicy.ApplicableTo applicableTo, 
            @Param("date") LocalDate date);
    
    @Query("SELECT DISTINCT dp.discountType FROM DiscountPolicy dp WHERE dp.isActive = true ORDER BY dp.discountType")
    List<DiscountPolicy.DiscountType> findAllActiveDiscountTypes();
    
    @Query("SELECT DISTINCT dp.applicableTo FROM DiscountPolicy dp WHERE dp.isActive = true ORDER BY dp.applicableTo")
    List<DiscountPolicy.ApplicableTo> findAllActiveApplicableTo();
}