package com.ingestion.pharmacy.repository;

import com.ingestion.common.model.inventory.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {

    Optional<StockAdjustment> findByAdjustmentNumber(String adjustmentNumber);
    
    List<StockAdjustment> findByAdjustmentType(StockAdjustment.AdjustmentType adjustmentType);
    
    List<StockAdjustment> findByStatus(StockAdjustment.AdjustmentStatus status);
    
    List<StockAdjustment> findByAdjustmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.status = :status ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findByStatusOrderByAdjustmentDateDesc(@Param("status") StockAdjustment.AdjustmentStatus status);
    
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.adjustmentType = :type ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findByAdjustmentTypeOrderByAdjustmentDateDesc(@Param("type") StockAdjustment.AdjustmentType type);
    
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.createdBy.id = :userId ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findByCreatedByOrderByAdjustmentDateDesc(@Param("userId") Long userId);
    
    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.approvedBy.id = :userId ORDER BY sa.adjustmentDate DESC")
    List<StockAdjustment> findByApprovedByOrderByAdjustmentDateDesc(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(sa) FROM StockAdjustment sa WHERE sa.status = :status")
    Long countByStatus(@Param("status") StockAdjustment.AdjustmentStatus status);
    
    @Query("SELECT COUNT(sa) FROM StockAdjustment sa WHERE sa.adjustmentType = :type")
    Long countByAdjustmentType(@Param("type") StockAdjustment.AdjustmentType type);
}