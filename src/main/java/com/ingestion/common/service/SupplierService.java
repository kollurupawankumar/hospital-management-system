package com.ingestion.common.service;

import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.security.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SupplierService {
    
    Supplier saveSupplier(Supplier supplier);
    
    Optional<Supplier> findById(Long id);
    
    Optional<Supplier> findBySupplierCode(String supplierCode);
    
    Optional<Supplier> findByEmail(String email);
    
    List<Supplier> findAll();
    
    List<Supplier> findByNameContaining(String name);
    
    List<Supplier> findByIsActive(Boolean isActive);
    
    List<Supplier> findBySupplierType(Supplier.SupplierType supplierType);
    
    List<Supplier> findActiveSuppliers();
    
    List<Supplier> findInactiveSuppliers();
    
    List<Supplier> findActiveSuppliersByType(Supplier.SupplierType supplierType);
    
    List<Supplier> findByCategory(String category);
    
    List<Supplier> findActiveByCategoryContaining(String category);
    
    List<String> findAllCategories();
    
    List<Supplier> searchSuppliers(String searchTerm);
    
    Long countActiveSuppliers();
    
    Long countActiveSuppliersByType(Supplier.SupplierType supplierType);
    
    Supplier createSupplier(String name, String contactPerson, String email, String phone, 
                          String addressLine1, String addressLine2, String city, String state, 
                          String postalCode, String country, Supplier.SupplierType supplierType, 
                          Set<String> categories, User createdBy);
    
    Supplier createSupplier(Supplier supplier, Set<String> categories, User createdBy);
    
    Supplier updateSupplier(Long id, String name, String contactPerson, String email, String phone, 
                          String addressLine1, String addressLine2, String city, String state, 
                          String postalCode, String country, User updatedBy);
    
    Supplier updateSupplier(Long id, Supplier supplier, User updatedBy);
    
    Supplier updateCategories(Long id, Set<String> categories);
    
    Supplier updateSupplierType(Long id, Supplier.SupplierType supplierType);
    
    Supplier activateSupplier(Long id, User updatedBy);
    
    Supplier deactivateSupplier(Long id, User updatedBy);
    
    void deleteSupplier(Long id);
    
    boolean isSupplierCodeUnique(String supplierCode);
    
    boolean isSupplierCodeUnique(String supplierCode, Long excludeId);
    
    boolean isEmailUnique(String email);
    
    boolean isEmailUnique(String email, Long excludeId);
}