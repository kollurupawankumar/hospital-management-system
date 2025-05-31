package com.ingestion.pharmacy.repository;

import com.ingestion.pharmacy.model.Medicine;
import com.ingestion.pharmacy.model.MedicineStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineStockRepository extends JpaRepository<MedicineStock, Long> {

    List<MedicineStock> findByMedicine(Medicine medicine);
    
    List<MedicineStock> findByMedicineAndIsExpired(Medicine medicine, Boolean isExpired);
    
    Optional<MedicineStock> findByMedicineAndBatchNumber(Medicine medicine, String batchNumber);
    
    List<MedicineStock> findByExpiryDateBefore(LocalDate date);
    
    List<MedicineStock> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<MedicineStock> findByIsExpired(Boolean isExpired);
    
    @Query("SELECT ms FROM MedicineStock ms WHERE ms.medicine.id = :medicineId AND ms.isExpired = false AND ms.currentQuantity > 0 ORDER BY ms.expiryDate ASC")
    List<MedicineStock> findAvailableStocksByMedicineOrderByExpiryDate(@Param("medicineId") Long medicineId);
    
    @Query("SELECT ms FROM MedicineStock ms WHERE ms.expiryDate BETWEEN :startDate AND :endDate AND ms.isExpired = false ORDER BY ms.expiryDate ASC")
    List<MedicineStock> findStocksExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(ms.currentQuantity) FROM MedicineStock ms WHERE ms.medicine.id = :medicineId AND ms.isExpired = false")
    Integer getTotalAvailableQuantityByMedicine(@Param("medicineId") Long medicineId);
    
    @Query("SELECT ms FROM MedicineStock ms WHERE ms.currentQuantity > 0 AND ms.isExpired = false ORDER BY ms.medicine.name, ms.expiryDate ASC")
    List<MedicineStock> findAllAvailableStocks();
    
    @Query("SELECT ms FROM MedicineStock ms WHERE ms.medicine.id = :medicineId AND ms.batchNumber = :batchNumber AND ms.isExpired = false AND ms.currentQuantity > 0")
    Optional<MedicineStock> findAvailableStockByMedicineAndBatch(@Param("medicineId") Long medicineId, @Param("batchNumber") String batchNumber);
}