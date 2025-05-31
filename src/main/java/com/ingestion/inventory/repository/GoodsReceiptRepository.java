package com.ingestion.inventory.repository;

import com.ingestion.inventory.model.GoodsReceipt;
import com.ingestion.inventory.model.Store;
import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.model.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {
    
    Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber);
    
    List<GoodsReceipt> findByPurchaseOrder(PurchaseOrder purchaseOrder);
    
    List<GoodsReceipt> findBySupplier(Supplier supplier);
    
    List<GoodsReceipt> findByStore(Store store);
    
    List<GoodsReceipt> findByStatus(GoodsReceipt.ReceiptStatus status);
    
    List<GoodsReceipt> findByReceiptDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<GoodsReceipt> findByInvoiceNumber(String invoiceNumber);
    
    List<GoodsReceipt> findByIsCancelled(Boolean isCancelled);
}