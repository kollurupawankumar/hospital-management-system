package com.ingestion.inventory.repository;

import com.ingestion.inventory.model.Inventory;
import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    List<Inventory> findByItem(Item item);
    
    List<Inventory> findByStore(Store store);
    
    Optional<Inventory> findByItemAndStoreAndBatchNumber(Item item, Store store, String batchNumber);
    
    List<Inventory> findByExpiryDateBefore(LocalDateTime date);
    
    List<Inventory> findByIsActive(Boolean isActive);
    
    @Query("SELECT i FROM Inventory i WHERE i.store = :store AND i.quantityAvailable <= i.item.reorderLevel")
    List<Inventory> findLowStockItems(@Param("store") Store store);
    
    @Query("SELECT i FROM Inventory i WHERE i.store = :store AND i.expiryDate BETWEEN :startDate AND :endDate")
    List<Inventory> findExpiringItems(@Param("store") Store store, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(i.quantityAvailable) FROM Inventory i WHERE i.item = :item")
    Integer getTotalQuantityForItem(@Param("item") Item item);
    
    @Query("SELECT SUM(i.quantityAvailable) FROM Inventory i WHERE i.item = :item AND i.store = :store")
    Integer getQuantityForItemInStore(@Param("item") Item item, @Param("store") Store store);
}