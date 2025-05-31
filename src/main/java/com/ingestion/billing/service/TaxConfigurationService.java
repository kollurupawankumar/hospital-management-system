package com.ingestion.billing.service;

import com.ingestion.billing.model.TaxConfiguration;
import com.ingestion.security.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaxConfigurationService {

    TaxConfiguration saveTaxConfiguration(TaxConfiguration taxConfiguration);
    
    Optional<TaxConfiguration> findById(Long id);
    
    Optional<TaxConfiguration> findByTaxCode(String taxCode);
    
    List<TaxConfiguration> findAll();
    
    List<TaxConfiguration> findByTaxName(String taxName);
    
    List<TaxConfiguration> findByTaxType(TaxConfiguration.TaxType taxType);
    
    List<TaxConfiguration> findByIsActive(Boolean isActive);
    
    List<TaxConfiguration> findByApplicableTo(TaxConfiguration.ApplicableTo applicableTo);
    
    List<TaxConfiguration> findAllActiveTaxes();
    
    List<TaxConfiguration> findActiveByTaxType(TaxConfiguration.TaxType taxType);
    
    List<TaxConfiguration> findActiveByApplicableTo(TaxConfiguration.ApplicableTo applicableTo);
    
    List<TaxConfiguration> findActiveByApplicableToOrAll(TaxConfiguration.ApplicableTo applicableTo);
    
    List<TaxConfiguration> findActiveAndEffectiveAsOf(LocalDate date);
    
    List<TaxConfiguration> findActiveAndEffectiveByApplicableToAsOf(
            TaxConfiguration.ApplicableTo applicableTo, LocalDate date);
    
    List<TaxConfiguration.TaxType> findAllActiveTaxTypes();
    
    List<TaxConfiguration.ApplicableTo> findAllActiveApplicableTo();
    
    TaxConfiguration createTaxConfiguration(String taxName, String taxCode, BigDecimal taxPercentage, 
                                           TaxConfiguration.TaxType taxType, String description, 
                                           LocalDate effectiveFrom, LocalDate effectiveTo, 
                                           TaxConfiguration.ApplicableTo applicableTo, 
                                           String hsnSacCode, User createdBy);
    
    TaxConfiguration updateTaxConfiguration(Long id, TaxConfiguration taxConfiguration, User lastModifiedBy);
    
    TaxConfiguration activateTaxConfiguration(Long id, User lastModifiedBy);
    
    TaxConfiguration deactivateTaxConfiguration(Long id, User lastModifiedBy);
    
    BigDecimal calculateTaxAmount(Long taxConfigId, BigDecimal amount);
    
    BigDecimal calculateTaxAmountByApplicableTo(TaxConfiguration.ApplicableTo applicableTo, BigDecimal amount);
    
    void deleteTaxConfiguration(Long id);
}