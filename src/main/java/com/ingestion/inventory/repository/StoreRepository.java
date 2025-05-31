package com.ingestion.inventory.repository;

import com.ingestion.inventory.model.Store;
import com.ingestion.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
    Optional<Store> findByStoreCode(String storeCode);
    
    List<Store> findByNameContainingIgnoreCase(String name);
    
    List<Store> findByStoreType(Store.StoreType storeType);
    
    List<Store> findByIsActive(Boolean isActive);
    
    List<Store> findByManager(User manager);
    
    boolean existsByStoreCode(String storeCode);
}