package com.ingestion.inventory.repository;

import com.ingestion.common.model.inventory.StockAdjustment;
import com.ingestion.inventory.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {
    
    Optional<StockAdjustment> findByAdjustmentNumber(String adjustmentNumber);
    
    List<StockAdjustment> findByStore(Store store);
    
    List<StockAdjustment> findByAdjustmentType(StockAdjustment.AdjustmentType adjustmentType);
    
    List<StockAdjustment> findByStatus(StockAdjustment.AdjustmentStatus status);
    
    List<StockAdjustment> findByAdjustmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<StockAdjustment> findByIsCancelled(Boolean isCancelled);
}