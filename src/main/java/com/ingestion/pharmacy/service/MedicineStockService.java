package com.ingestion.pharmacy.service;

import com.ingestion.pharmacy.model.Medicine;
import com.ingestion.pharmacy.model.MedicineStock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicineStockService {

    MedicineStock saveMedicineStock(MedicineStock medicineStock);
    
    Optional<MedicineStock> findById(Long id);
    
    List<MedicineStock> findAll();
    
    List<MedicineStock> findByMedicine(Medicine medicine);
    
    List<MedicineStock> findByMedicineAndIsExpired(Medicine medicine, Boolean isExpired);
    
    Optional<MedicineStock> findByMedicineAndBatchNumber(Medicine medicine, String batchNumber);
    
    List<MedicineStock> findByExpiryDateBefore(LocalDate date);
    
    List<MedicineStock> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<MedicineStock> findByIsExpired(Boolean isExpired);
    
    List<MedicineStock> findAvailableStocksByMedicineOrderByExpiryDate(Long medicineId);
    
    List<MedicineStock> findStocksExpiringBetween(LocalDate startDate, LocalDate endDate);
    
    Integer getTotalAvailableQuantityByMedicine(Long medicineId);
    
    List<MedicineStock> findAllAvailableStocks();
    
    Optional<MedicineStock> findAvailableStockByMedicineAndBatch(Long medicineId, String batchNumber);
    
    MedicineStock addStock(Medicine medicine, String batchNumber, LocalDate expiryDate, LocalDate manufacturingDate, 
                          Integer quantity, java.math.BigDecimal purchasePrice, java.math.BigDecimal sellingPrice, String location);
    
    MedicineStock updateStock(Long id, MedicineStock medicineStock);
    
    MedicineStock reduceStock(Long id, Integer quantity);
    
    MedicineStock addToStock(Long id, Integer quantity);
    
    void updateExpiryStatus(Long id);
    
    void updateAllExpiryStatuses();
    
    List<MedicineStock> findStocksExpiringInDays(int days);
    
    void deleteStock(Long id);
}