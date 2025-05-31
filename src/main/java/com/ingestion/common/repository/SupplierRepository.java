package com.ingestion.common.repository;

import com.ingestion.common.model.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    Optional<Supplier> findBySupplierCode(String supplierCode);
    
    Optional<Supplier> findByEmail(String email);
    
    List<Supplier> findByNameContainingIgnoreCase(String name);
    
    List<Supplier> findByIsActive(Boolean isActive);
    
    List<Supplier> findBySupplierType(Supplier.SupplierType supplierType);
    
    List<Supplier> findByIsActiveAndSupplierType(Boolean isActive, Supplier.SupplierType supplierType);
    
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true")
    List<Supplier> findActiveSuppliers();
    
    @Query("SELECT s FROM Supplier s WHERE s.isActive = false")
    List<Supplier> findInactiveSuppliers();
    
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true AND s.supplierType = :supplierType")
    List<Supplier> findActiveSuppliersByType(@Param("supplierType") Supplier.SupplierType supplierType);
    
    @Query("SELECT s FROM Supplier s JOIN s.categories c WHERE c = :category")
    List<Supplier> findByCategory(@Param("category") String category);
    
    @Query("SELECT s FROM Supplier s JOIN s.categories c WHERE c = :category AND s.isActive = true")
    List<Supplier> findActiveByCategoryContaining(@Param("category") String category);
    
    @Query("SELECT DISTINCT c FROM Supplier s JOIN s.categories c ORDER BY c")
    List<String> findAllCategories();
    
    @Query("SELECT s FROM Supplier s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Supplier> searchSuppliers(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = true")
    Long countActiveSuppliers();
    
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = true AND s.supplierType = :supplierType")
    Long countActiveSuppliersByType(@Param("supplierType") Supplier.SupplierType supplierType);
    
    boolean existsBySupplierCode(String supplierCode);
    
    boolean existsByEmail(String email);
    
    boolean existsBySupplierCodeAndIdNot(String supplierCode, Long id);
    
    boolean existsByEmailAndIdNot(String email, Long id);
}