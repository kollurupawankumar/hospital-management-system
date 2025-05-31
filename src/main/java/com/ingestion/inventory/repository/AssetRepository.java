package com.ingestion.inventory.repository;

import com.ingestion.inventory.model.Asset;
import com.ingestion.inventory.model.ItemCategory;
import com.ingestion.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    
    Optional<Asset> findByAssetCode(String assetCode);
    
    List<Asset> findByNameContainingIgnoreCase(String name);
    
    List<Asset> findByCategory(ItemCategory category);
    
    List<Asset> findByAssetType(Asset.AssetType assetType);
    
    List<Asset> findByStatus(Asset.AssetStatus status);
    
    List<Asset> findByAssignedTo(User assignedTo);
    
    List<Asset> findByLocation(String location);
    
    List<Asset> findByWarrantyExpiryDateBefore(LocalDate date);
    
    @Query("SELECT a FROM Asset a WHERE a.nextMaintenanceDate <= :date")
    List<Asset> findAssetsNeedingMaintenance(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Asset a WHERE a.nextMaintenanceDate BETWEEN :startDate AND :endDate")
    List<Asset> findAssetsNeedingMaintenanceSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    boolean existsByAssetCode(String assetCode);
    
    boolean existsBySerialNumber(String serialNumber);
}