package com.ingestion.pharmacy.repository;

import com.ingestion.pharmacy.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByMedicineCode(String medicineCode);
    
    Optional<Medicine> findByBarcode(String barcode);
    
    List<Medicine> findByNameContainingIgnoreCase(String name);
    
    List<Medicine> findByGenericNameContainingIgnoreCase(String genericName);
    
    List<Medicine> findByBrandNameContainingIgnoreCase(String brandName);
    
    List<Medicine> findByCategory(String category);
    
    List<Medicine> findByCategoryAndSubCategory(String category, String subCategory);
    
    List<Medicine> findByManufacturer(String manufacturer);
    
    List<Medicine> findByDosageForm(Medicine.DosageForm dosageForm);
    
    List<Medicine> findByIsActive(Boolean isActive);
    
    List<Medicine> findByIsPrescriptionRequired(Boolean isPrescriptionRequired);
    
    List<Medicine> findByIsControlledSubstance(Boolean isControlledSubstance);
    
    @Query("SELECT m FROM Medicine m WHERE m.isActive = true ORDER BY m.name")
    List<Medicine> findAllActiveMedicines();
    
    @Query("SELECT m FROM Medicine m WHERE m.isActive = true AND m.category = :category ORDER BY m.name")
    List<Medicine> findActiveByCategoryOrderByName(@Param("category") String category);
    
    @Query("SELECT DISTINCT m.category FROM Medicine m WHERE m.isActive = true ORDER BY m.category")
    List<String> findAllActiveCategories();
    
    @Query("SELECT DISTINCT m.subCategory FROM Medicine m WHERE m.isActive = true AND m.category = :category ORDER BY m.subCategory")
    List<String> findAllActiveSubCategoriesByCategory(@Param("category") String category);
    
    @Query("SELECT DISTINCT m.manufacturer FROM Medicine m WHERE m.isActive = true ORDER BY m.manufacturer")
    List<String> findAllActiveManufacturers();
    
    @Query("SELECT m FROM Medicine m WHERE m.isActive = true AND m.name LIKE %:searchTerm% OR m.genericName LIKE %:searchTerm% OR m.brandName LIKE %:searchTerm% OR m.medicineCode LIKE %:searchTerm%")
    List<Medicine> searchMedicines(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT m FROM Medicine m JOIN m.stocks s GROUP BY m HAVING SUM(s.currentQuantity) <= m.reorderLevel AND m.isActive = true")
    List<Medicine> findMedicinesWithLowStock();
}