package com.ingestion.inventory.service;

import com.ingestion.inventory.model.Asset;
import com.ingestion.inventory.model.ItemCategory;
import com.ingestion.security.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssetService {
    
    Asset saveAsset(Asset asset);
    
    Optional<Asset> findById(Long id);
    
    Optional<Asset> findByAssetCode(String assetCode);
    
    List<Asset> findAll();
    
    List<Asset> findByNameContaining(String name);
    
    List<Asset> findByCategory(ItemCategory category);
    
    List<Asset> findByAssetType(Asset.AssetType assetType);
    
    List<Asset> findByStatus(Asset.AssetStatus status);
    
    List<Asset> findByAssignedTo(User assignedTo);
    
    List<Asset> findByLocation(String location);
    
    List<Asset> findAssetsWithExpiredWarranty();
    
    List<Asset> findAssetsNeedingMaintenance();
    
    List<Asset> findAssetsNeedingMaintenanceSoon(int daysThreshold);
    
    Asset createAsset(String name, String description, ItemCategory category, Asset.AssetType assetType, 
                    LocalDate purchaseDate, BigDecimal purchasePrice, String manufacturer, String brand, 
                    String model, String serialNumber, LocalDate warrantyExpiryDate, String location, 
                    User createdBy);
    
    Asset updateAsset(Long id, String name, String description, ItemCategory category, Asset.AssetType assetType, 
                    LocalDate purchaseDate, BigDecimal purchasePrice, String manufacturer, String brand, 
                    String model, String serialNumber, LocalDate warrantyExpiryDate, String location, 
                    User updatedBy);
    
    Asset assignAsset(Long id, User assignedTo);
    
    Asset unassignAsset(Long id);
    
    Asset markForMaintenance(Long id);
    
    Asset markAsDamaged(Long id);
    
    Asset markAsDisposed(Long id);
    
    Asset markAsLost(Long id);
    
    Asset markAsAvailable(Long id);
    
    Asset recordMaintenance(Long id, LocalDate maintenanceDate, Integer maintenanceIntervalDays, String notes);
    
    Asset calculateDepreciation(Long id);
    
    void deleteAsset(Long id);
    
    boolean isAssetCodeUnique(String assetCode);
    
    boolean isSerialNumberUnique(String serialNumber);
}