package com.ingestion.inventory.service.impl;

import com.ingestion.inventory.model.Inventory;
import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.Store;
import com.ingestion.inventory.repository.InventoryRepository;
import com.ingestion.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<Inventory> findByItem(Item item) {
        return inventoryRepository.findByItem(item);
    }

    @Override
    public List<Inventory> findByStore(Store store) {
        return inventoryRepository.findByStore(store);
    }

    @Override
    public Optional<Inventory> findByItemAndStoreAndBatchNumber(Item item, Store store, String batchNumber) {
        return inventoryRepository.findByItemAndStoreAndBatchNumber(item, store, batchNumber);
    }

    @Override
    public List<Inventory> findExpiredItems() {
        return inventoryRepository.findByExpiryDateBefore(LocalDateTime.now());
    }

    @Override
    public List<Inventory> findExpiringItems(int daysThreshold) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(daysThreshold);
        
        List<Inventory> expiringItems = inventoryRepository.findExpiringItems(null, startDate, endDate);
        
        // Filter out items that are already expired
        return expiringItems.stream()
                .filter(inventory -> !inventory.isExpired())
                .collect(Collectors.toList());
    }

    @Override
    public List<Inventory> findLowStockItems(Store store) {
        return inventoryRepository.findLowStockItems(store);
    }

    @Override
    public Integer getTotalQuantityForItem(Item item) {
        Integer quantity = inventoryRepository.getTotalQuantityForItem(item);
        return quantity != null ? quantity : 0;
    }

    @Override
    public Integer getQuantityForItemInStore(Item item, Store store) {
        Integer quantity = inventoryRepository.getQuantityForItemInStore(item, store);
        return quantity != null ? quantity : 0;
    }

    @Override
    @Transactional
    public Inventory addStock(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.addStock(quantity);
        
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory removeStock(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.removeStock(quantity);
        
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory reserveStock(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.reserveStock(quantity);
        
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory releaseReservedStock(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.releaseReservedStock(quantity);
        
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory consumeReservedStock(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.consumeReservedStock(quantity);
        
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory createInventoryItem(Item item, Store store, String batchNumber, Integer quantity, 
                                       LocalDateTime manufacturingDate, LocalDateTime expiryDate, 
                                       BigDecimal purchasePrice, BigDecimal sellingPrice) {
        // Check if inventory with same item, store and batch number already exists
        Optional<Inventory> existingInventory = inventoryRepository.findByItemAndStoreAndBatchNumber(item, store, batchNumber);
        
        if (existingInventory.isPresent()) {
            // Update existing inventory
            Inventory inventory = existingInventory.get();
            inventory.addStock(quantity);
            
            // Update other details if provided
            if (manufacturingDate != null) {
                inventory.setManufacturingDate(manufacturingDate);
            }
            
            if (expiryDate != null) {
                inventory.setExpiryDate(expiryDate);
            }
            
            if (purchasePrice != null) {
                inventory.setPurchasePrice(purchasePrice);
            }
            
            if (sellingPrice != null) {
                inventory.setSellingPrice(sellingPrice);
            }
            
            return inventoryRepository.save(inventory);
        } else {
            // Create new inventory
            Inventory inventory = new Inventory();
            inventory.setItem(item);
            inventory.setStore(store);
            inventory.setBatchNumber(batchNumber);
            inventory.setQuantityAvailable(quantity);
            inventory.setQuantityReserved(0);
            inventory.setManufacturingDate(manufacturingDate);
            inventory.setExpiryDate(expiryDate);
            inventory.setPurchasePrice(purchasePrice);
            inventory.setSellingPrice(sellingPrice);
            inventory.setIsActive(true);
            
            return inventoryRepository.save(inventory);
        }
    }

    @Override
    @Transactional
    public void updateInventoryLocation(Long inventoryId, String rackNumber, String shelfNumber, String binNumber) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.setRackNumber(rackNumber);
        inventory.setShelfNumber(shelfNumber);
        inventory.setBinNumber(binNumber);
        
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        
        inventoryRepository.delete(inventory);
    }
}