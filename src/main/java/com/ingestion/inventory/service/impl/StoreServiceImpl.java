package com.ingestion.inventory.service.impl;

import com.ingestion.inventory.model.Store;
import com.ingestion.inventory.repository.StoreRepository;
import com.ingestion.inventory.service.StoreService;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public Optional<Store> findByStoreCode(String storeCode) {
        return storeRepository.findByStoreCode(storeCode);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public List<Store> findByNameContaining(String name) {
        return storeRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Store> findByStoreType(Store.StoreType storeType) {
        return storeRepository.findByStoreType(storeType);
    }

    @Override
    public List<Store> findActiveStores() {
        return storeRepository.findByIsActive(true);
    }

    @Override
    public List<Store> findInactiveStores() {
        return storeRepository.findByIsActive(false);
    }

    @Override
    public List<Store> findByManager(User manager) {
        return storeRepository.findByManager(manager);
    }

    @Override
    @Transactional
    public Store createStore(String name, String description, String location, Store.StoreType storeType, 
                           User manager, String contactNumber, String email, User createdBy) {
        Store store = new Store();
        store.setName(name);
        store.setDescription(description);
        store.setLocation(location);
        store.setStoreType(storeType);
        store.setManager(manager);
        store.setContactNumber(contactNumber);
        store.setEmail(email);
        store.setIsActive(true);
        store.setCreator(createdBy);
        store.setLastModifiedBy(createdBy);
        
        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public Store updateStore(Long id, String name, String description, String location, Store.StoreType storeType, 
                           User manager, String contactNumber, String email, User updatedBy) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
        
        store.setName(name);
        store.setDescription(description);
        store.setLocation(location);
        store.setStoreType(storeType);
        store.setManager(manager);
        store.setContactNumber(contactNumber);
        store.setEmail(email);
        store.setLastModifiedBy(updatedBy);
        
        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public Store activateStore(Long id, User updatedBy) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
        
        store.setIsActive(true);
        store.setLastModifiedBy(updatedBy);
        
        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public Store deactivateStore(Long id, User updatedBy) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
        
        store.setIsActive(false);
        store.setLastModifiedBy(updatedBy);
        
        return storeRepository.save(store);
    }

    @Override
    @Transactional
    public void deleteStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
        
        storeRepository.delete(store);
    }

    @Override
    public boolean isStoreCodeUnique(String storeCode) {
        return !storeRepository.existsByStoreCode(storeCode);
    }
}