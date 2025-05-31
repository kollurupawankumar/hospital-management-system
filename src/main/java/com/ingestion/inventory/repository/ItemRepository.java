package com.ingestion.inventory.repository;

import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    Optional<Item> findByItemCode(String itemCode);
    
    List<Item> findByNameContainingIgnoreCase(String name);
    
    List<Item> findByCategory(ItemCategory category);
    
    List<Item> findByItemType(Item.ItemType itemType);
    
    List<Item> findByIsActive(Boolean isActive);
    
    List<Item> findByIsPerishable(Boolean isPerishable);
    
    @Query("SELECT i FROM Item i WHERE i.reorderLevel >= i.minimumStock")
    List<Item> findItemsNeedingReorder();
    
    boolean existsByItemCode(String itemCode);
    
    boolean existsByBarcode(String barcode);
}