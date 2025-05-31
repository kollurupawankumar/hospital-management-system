package com.ingestion.billing.repository;

import com.ingestion.billing.model.TaxConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxConfigurationRepository extends JpaRepository<TaxConfiguration, Long> {

    Optional<TaxConfiguration> findByTaxCode(String taxCode);
    
    List<TaxConfiguration> findByTaxName(String taxName);
    
    List<TaxConfiguration> findByTaxType(TaxConfiguration.TaxType taxType);
    
    List<TaxConfiguration> findByIsActive(Boolean isActive);
    
    List<TaxConfiguration> findByApplicableTo(TaxConfiguration.ApplicableTo applicableTo);
    
    @Query("SELECT tc FROM TaxConfiguration tc WHERE tc.isActive = true ORDER BY tc.taxName")
    List<TaxConfiguration> findAllActiveTaxes();
    
    @Query("SELECT tc FROM TaxConfiguration tc WHERE tc.isActive = true AND tc.taxType = :taxType ORDER BY tc.taxName")
    List<TaxConfiguration> findActiveByTaxType(@Param("taxType") TaxConfiguration.TaxType taxType);
    
    @Query("SELECT tc FROM TaxConfiguration tc WHERE tc.isActive = true AND tc.applicableTo = :applicableTo ORDER BY tc.taxName")
    List<TaxConfiguration> findActiveByApplicableTo(@Param("applicableTo") TaxConfiguration.ApplicableTo applicableTo);
    
    @Query("SELECT tc FROM TaxConfiguration tc WHERE tc.isActive = true AND (tc.applicableTo = :applicableTo OR tc.applicableTo = 'ALL') ORDER BY tc.taxName")
    List<TaxConfiguration> findActiveByApplicableToOrAll(@Param("applicableTo") TaxConfiguration.ApplicableTo applicableTo);
    
    @Query("SELECT tc FROM TaxConfiguration tc WHERE tc.isActive = true AND " +
           "(tc.effectiveFrom IS NULL OR tc.effectiveFrom <= :date) AND " +
           "(tc.effectiveTo IS NULL OR tc.effectiveTo >= :date) ORDER BY tc.taxName")
    List<TaxConfiguration> findActiveAndEffectiveAsOf(@Param("date") LocalDate date);
    
    @Query("SELECT tc FROM TaxConfiguration tc WHERE tc.isActive = true AND " +
           "(tc.effectiveFrom IS NULL OR tc.effectiveFrom <= :date) AND " +
           "(tc.effectiveTo IS NULL OR tc.effectiveTo >= :date) AND " +
           "(tc.applicableTo = :applicableTo OR tc.applicableTo = 'ALL') ORDER BY tc.taxName")
    List<TaxConfiguration> findActiveAndEffectiveByApplicableToAsOf(
            @Param("applicableTo") TaxConfiguration.ApplicableTo applicableTo, 
            @Param("date") LocalDate date);
    
    @Query("SELECT DISTINCT tc.taxType FROM TaxConfiguration tc WHERE tc.isActive = true ORDER BY tc.taxType")
    List<TaxConfiguration.TaxType> findAllActiveTaxTypes();
    
    @Query("SELECT DISTINCT tc.applicableTo FROM TaxConfiguration tc WHERE tc.isActive = true ORDER BY tc.applicableTo")
    List<TaxConfiguration.ApplicableTo> findAllActiveApplicableTo();
}