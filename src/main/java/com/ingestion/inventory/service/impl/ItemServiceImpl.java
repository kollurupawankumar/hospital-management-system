package com.ingestion.inventory.service.impl;

import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.ItemCategory;
import com.ingestion.inventory.repository.ItemRepository;
import com.ingestion.inventory.service.ItemService;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Optional<Item> findByItemCode(String itemCode) {
        return itemRepository.findByItemCode(itemCode);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findByNameContaining(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Item> findByCategory(ItemCategory category) {
        return itemRepository.findByCategory(category);
    }

    @Override
    public List<Item> findByItemType(Item.ItemType itemType) {
        return itemRepository.findByItemType(itemType);
    }

    @Override
    public List<Item> findActiveItems() {
        return itemRepository.findByIsActive(true);
    }

    @Override
    public List<Item> findInactiveItems() {
        return itemRepository.findByIsActive(false);
    }

    @Override
    public List<Item> findPerishableItems() {
        return itemRepository.findByIsPerishable(true);
    }

    @Override
    public List<Item> findItemsNeedingReorder() {
        return itemRepository.findItemsNeedingReorder();
    }

    @Override
    @Transactional
    public Item createItem(String name, String description, ItemCategory category, Item.ItemType itemType, 
                         String unitOfMeasure, Integer reorderLevel, Integer minimumStock, Integer maximumStock, 
                         BigDecimal purchasePrice, BigDecimal sellingPrice, User createdBy) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setItemType(itemType);
        item.setUnitOfMeasure(unitOfMeasure);
        item.setReorderLevel(reorderLevel);
        item.setMinimumStock(minimumStock);
        item.setMaximumStock(maximumStock);
        item.setPurchasePrice(purchasePrice);
        item.setSellingPrice(sellingPrice);
        item.setIsActive(true);
        item.setCreator(createdBy);
        item.setLastModifiedBy(createdBy);
        
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(Long id, String name, String description, ItemCategory category, Item.ItemType itemType, 
                         String unitOfMeasure, Integer reorderLevel, Integer minimumStock, Integer maximumStock, 
                         BigDecimal purchasePrice, BigDecimal sellingPrice, User updatedBy) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setItemType(itemType);
        item.setUnitOfMeasure(unitOfMeasure);
        item.setReorderLevel(reorderLevel);
        item.setMinimumStock(minimumStock);
        item.setMaximumStock(maximumStock);
        item.setPurchasePrice(purchasePrice);
        item.setSellingPrice(sellingPrice);
        item.setLastModifiedBy(updatedBy);
        
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item activateItem(Long id, User updatedBy) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        item.setIsActive(true);
        item.setLastModifiedBy(updatedBy);
        
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item deactivateItem(Long id, User updatedBy) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        item.setIsActive(false);
        item.setLastModifiedBy(updatedBy);
        
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        itemRepository.delete(item);
    }

    @Override
    public boolean isItemCodeUnique(String itemCode) {
        return !itemRepository.existsByItemCode(itemCode);
    }

    @Override
    public boolean isBarcodeUnique(String barcode) {
        return !itemRepository.existsByBarcode(barcode);
    }
    
    @Override
    public List<Object[]> getItemCountByCategory() {
        // This would typically query the database to get counts of items by category
        // For now, we'll return an empty list as a placeholder
        return List.of();
    }
}