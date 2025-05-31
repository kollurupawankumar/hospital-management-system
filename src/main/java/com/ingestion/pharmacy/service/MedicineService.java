package com.ingestion.pharmacy.service;

import com.ingestion.pharmacy.model.Medicine;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MedicineService {

    Medicine saveMedicine(Medicine medicine);
    
    Optional<Medicine> findById(Long id);
    
    Optional<Medicine> findByMedicineCode(String medicineCode);
    
    Optional<Medicine> findByBarcode(String barcode);
    
    List<Medicine> findAll();
    
    List<Medicine> findByName(String name);
    
    List<Medicine> findByGenericName(String genericName);
    
    List<Medicine> findByBrandName(String brandName);
    
    List<Medicine> findByCategory(String category);
    
    List<Medicine> findByCategoryAndSubCategory(String category, String subCategory);
    
    List<Medicine> findByManufacturer(String manufacturer);
    
    List<Medicine> findByDosageForm(Medicine.DosageForm dosageForm);
    
    List<Medicine> findByIsActive(Boolean isActive);
    
    List<Medicine> findByIsPrescriptionRequired(Boolean isPrescriptionRequired);
    
    List<Medicine> findByIsControlledSubstance(Boolean isControlledSubstance);
    
    List<Medicine> findAllActiveMedicines();
    
    List<Medicine> findActiveByCategoryOrderByName(String category);
    
    List<String> findAllActiveCategories();
    
    List<String> findAllActiveSubCategoriesByCategory(String category);
    
    List<String> findAllActiveManufacturers();
    
    List<Medicine> searchMedicines(String searchTerm);
    
    List<Medicine> findMedicinesWithLowStock();
    
    Medicine createMedicine(Medicine medicine, Set<String> ingredients, Set<String> sideEffects, Set<String> contraindications);
    
    Medicine updateMedicine(Long id, Medicine medicine);
    
    Medicine updateIngredients(Long id, Set<String> ingredients);
    
    Medicine updateSideEffects(Long id, Set<String> sideEffects);
    
    Medicine updateContraindications(Long id, Set<String> contraindications);
    
    Medicine activateMedicine(Long id);
    
    Medicine deactivateMedicine(Long id);
    
    void deleteMedicine(Long id);
}