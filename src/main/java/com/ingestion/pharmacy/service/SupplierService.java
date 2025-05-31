package com.ingestion.pharmacy.service;

import com.ingestion.common.model.supplier.Supplier;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SupplierService {

    Supplier saveSupplier(Supplier supplier);
    
    Optional<Supplier> findById(Long id);
    
    Optional<Supplier> findBySupplierCode(String supplierCode);
    
    List<Supplier> findAll();
    
    List<Supplier> findByName(String name);
    
    List<Supplier> findByIsActive(Boolean isActive);
    
    List<Supplier> findByCategory(String category);
    
    List<Supplier> findAllActiveSuppliers();
    
    List<String> findAllSupplierCategories();
    
    List<Supplier> searchSuppliers(String searchTerm);
    
    Supplier createSupplier(Supplier supplier, Set<String> categories);
    
    Supplier updateSupplier(Long id, Supplier supplier);
    
    Supplier updateCategories(Long id, Set<String> categories);
    
    Supplier activateSupplier(Long id);
    
    Supplier deactivateSupplier(Long id);
    
    void deleteSupplier(Long id);
}