package com.ingestion.inventory.service;

import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.security.model.User;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
    
    Supplier saveSupplier(Supplier supplier);
    
    Optional<Supplier> findById(Long id);
    
    Optional<Supplier> findBySupplierCode(String supplierCode);
    
    List<Supplier> findAll();
    
    List<Supplier> findByNameContaining(String name);
    
    List<Supplier> findActiveSuppliers();
    
    List<Supplier> findInactiveSuppliers();
    
    Supplier createSupplier(String name, String contactPerson, String email, String phone, 
                          String addressLine1, String addressLine2, String city, String state, 
                          String postalCode, String country, User createdBy);
    
    Supplier updateSupplier(Long id, String name, String contactPerson, String email, String phone, 
                          String addressLine1, String addressLine2, String city, String state, 
                          String postalCode, String country, User updatedBy);
    
    Supplier activateSupplier(Long id, User updatedBy);
    
    Supplier deactivateSupplier(Long id, User updatedBy);
    
    void deleteSupplier(Long id);
    
    boolean isSupplierCodeUnique(String supplierCode);
    
    boolean isEmailUnique(String email);
}