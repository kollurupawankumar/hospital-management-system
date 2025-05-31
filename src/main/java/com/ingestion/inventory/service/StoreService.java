package com.ingestion.inventory.service;

import com.ingestion.inventory.model.Store;
import com.ingestion.security.model.User;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    
    Store saveStore(Store store);
    
    Optional<Store> findById(Long id);
    
    Optional<Store> findByStoreCode(String storeCode);
    
    List<Store> findAll();
    
    List<Store> findByNameContaining(String name);
    
    List<Store> findByStoreType(Store.StoreType storeType);
    
    List<Store> findActiveStores();
    
    List<Store> findInactiveStores();
    
    List<Store> findByManager(User manager);
    
    Store createStore(String name, String description, String location, Store.StoreType storeType, 
                    User manager, String contactNumber, String email, User createdBy);
    
    Store updateStore(Long id, String name, String description, String location, Store.StoreType storeType, 
                    User manager, String contactNumber, String email, User updatedBy);
    
    Store activateStore(Long id, User updatedBy);
    
    Store deactivateStore(Long id, User updatedBy);
    
    void deleteStore(Long id);
    
    boolean isStoreCodeUnique(String storeCode);
}