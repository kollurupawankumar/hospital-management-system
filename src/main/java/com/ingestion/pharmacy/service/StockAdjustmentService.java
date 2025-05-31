package com.ingestion.pharmacy.service;

import com.ingestion.pharmacy.model.Medicine;
import com.ingestion.pharmacy.model.MedicineStock;
import com.ingestion.common.model.inventory.StockAdjustment;
import com.ingestion.common.model.inventory.StockAdjustmentItem;
import com.ingestion.security.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StockAdjustmentService {

    StockAdjustment saveStockAdjustment(StockAdjustment stockAdjustment);
    
    Optional<StockAdjustment> findById(Long id);
    
    Optional<StockAdjustment> findByAdjustmentNumber(String adjustmentNumber);
    
    List<StockAdjustment> findAll();
    
    List<StockAdjustment> findByAdjustmentType(StockAdjustment.AdjustmentType adjustmentType);
    
    List<StockAdjustment> findByStatus(StockAdjustment.AdjustmentStatus status);
    
    List<StockAdjustment> findByAdjustmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<StockAdjustment> findByStatusOrderByAdjustmentDateDesc(StockAdjustment.AdjustmentStatus status);
    
    List<StockAdjustment> findByAdjustmentTypeOrderByAdjustmentDateDesc(StockAdjustment.AdjustmentType type);
    
    List<StockAdjustment> findByCreatedByOrderByAdjustmentDateDesc(Long userId);
    
    List<StockAdjustment> findByApprovedByOrderByAdjustmentDateDesc(Long userId);
    
    Long countByStatus(StockAdjustment.AdjustmentStatus status);
    
    Long countByAdjustmentType(StockAdjustment.AdjustmentType type);
    
    StockAdjustment createStockAdjustment(StockAdjustment.AdjustmentType adjustmentType, 
                                         String reason, String notes, User createdBy, 
                                         List<StockAdjustmentItem> items);
    
    StockAdjustment addItemToAdjustment(Long adjustmentId, Medicine medicine, MedicineStock medicineStock, 
                                       String batchNumber, Integer quantity, BigDecimal unitCost, 
                                       String reason);
    
    StockAdjustment removeItemFromAdjustment(Long adjustmentId, Long itemId);
    
    StockAdjustment approve(Long id, User approver);
    
    StockAdjustment complete(Long id);
    
    StockAdjustment reject(Long id);
    
    StockAdjustment cancel(Long id);
    
    void applyAdjustment(Long id);
    
    void deleteStockAdjustment(Long id);
}