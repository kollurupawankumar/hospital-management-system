package com.ingestion.inventory.service;

import com.ingestion.inventory.model.Inventory;
import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.Store;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    
    Inventory saveInventory(Inventory inventory);
    
    Optional<Inventory> findById(Long id);
    
    List<Inventory> findAll();
    
    List<Inventory> findByItem(Item item);
    
    List<Inventory> findByStore(Store store);
    
    Optional<Inventory> findByItemAndStoreAndBatchNumber(Item item, Store store, String batchNumber);
    
    List<Inventory> findExpiredItems();
    
    List<Inventory> findExpiringItems(int daysThreshold);
    
    List<Inventory> findLowStockItems(Store store);
    
    Integer getTotalQuantityForItem(Item item);
    
    Integer getQuantityForItemInStore(Item item, Store store);
    
    Inventory addStock(Long inventoryId, Integer quantity);
    
    Inventory removeStock(Long inventoryId, Integer quantity);
    
    Inventory reserveStock(Long inventoryId, Integer quantity);
    
    Inventory releaseReservedStock(Long inventoryId, Integer quantity);
    
    Inventory consumeReservedStock(Long inventoryId, Integer quantity);
    
    Inventory createInventoryItem(Item item, Store store, String batchNumber, Integer quantity, 
                                LocalDateTime manufacturingDate, LocalDateTime expiryDate, 
                                BigDecimal purchasePrice, BigDecimal sellingPrice);
    
    void updateInventoryLocation(Long inventoryId, String rackNumber, String shelfNumber, String binNumber);
    
    void deleteInventory(Long id);
}