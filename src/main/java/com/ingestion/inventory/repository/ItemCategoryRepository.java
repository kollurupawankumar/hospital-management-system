package com.ingestion.inventory.repository;

import com.ingestion.inventory.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    
    Optional<ItemCategory> findByCategoryCode(String categoryCode);
    
    List<ItemCategory> findByNameContainingIgnoreCase(String name);
    
    List<ItemCategory> findByParentCategoryIsNull();
    
    List<ItemCategory> findByParentCategory(ItemCategory parentCategory);
    
    List<ItemCategory> findByIsActive(Boolean isActive);
    
    @Query("SELECT c FROM ItemCategory c WHERE c.parentCategory IS NULL AND c.isActive = true")
    List<ItemCategory> findActiveRootCategories();
    
    boolean existsByCategoryCode(String categoryCode);
}