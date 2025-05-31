package com.ingestion.inventory.service;

import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.ItemCategory;
import com.ingestion.security.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    
    Item saveItem(Item item);
    
    Optional<Item> findById(Long id);
    
    Optional<Item> findByItemCode(String itemCode);
    
    List<Item> findAll();
    
    List<Item> findByNameContaining(String name);
    
    List<Item> findByCategory(ItemCategory category);
    
    List<Item> findByItemType(Item.ItemType itemType);
    
    List<Item> findActiveItems();
    
    List<Item> findInactiveItems();
    
    List<Item> findPerishableItems();
    
    List<Item> findItemsNeedingReorder();
    
    Item createItem(String name, String description, ItemCategory category, Item.ItemType itemType, 
                  String unitOfMeasure, Integer reorderLevel, Integer minimumStock, Integer maximumStock, 
                  BigDecimal purchasePrice, BigDecimal sellingPrice, User createdBy);
    
    Item updateItem(Long id, String name, String description, ItemCategory category, Item.ItemType itemType, 
                  String unitOfMeasure, Integer reorderLevel, Integer minimumStock, Integer maximumStock, 
                  BigDecimal purchasePrice, BigDecimal sellingPrice, User updatedBy);
    
    Item activateItem(Long id, User updatedBy);
    
    Item deactivateItem(Long id, User updatedBy);
    
    void deleteItem(Long id);
    
    boolean isItemCodeUnique(String itemCode);
    
    boolean isBarcodeUnique(String barcode);
    
    List<Object[]> getItemCountByCategory();
}