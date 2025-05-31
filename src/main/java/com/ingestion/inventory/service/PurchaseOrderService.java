package com.ingestion.inventory.service;

import com.ingestion.inventory.model.Store;
import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.inventory.model.Item;
import com.ingestion.security.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PurchaseOrderService {
    
    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);
    
    Optional<PurchaseOrder> findById(Long id);
    
    Optional<PurchaseOrder> findByPoNumber(String poNumber);
    
    List<PurchaseOrder> findAll();
    
    List<PurchaseOrder> findBySupplier(Supplier supplier);
    
    List<PurchaseOrder> findByStore(Store store);
    
    List<PurchaseOrder> findByStatus(PurchaseOrder.PurchaseOrderStatus status);
    
    List<PurchaseOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<PurchaseOrder> findByOrderDateAfter(LocalDateTime date);
    
    List<PurchaseOrder> findByExpectedDeliveryDateBefore(LocalDateTime date);
    
    List<PurchaseOrder> findByStatusesOrderByOrderDate(List<PurchaseOrder.PurchaseOrderStatus> statuses);
    
    List<PurchaseOrder> findOverduePurchaseOrders();
    
    PurchaseOrder createPurchaseOrder(Supplier supplier, Store store, LocalDateTime expectedDeliveryDate, 
                                    String paymentTerms, String shippingTerms, String notes, User createdBy);
    
    PurchaseOrder addItemToPurchaseOrder(Long purchaseOrderId, Item item, Integer quantity, 
                                       BigDecimal unitPrice, BigDecimal discountPercentage, 
                                       BigDecimal taxPercentage);
    
    PurchaseOrder removeItemFromPurchaseOrder(Long purchaseOrderId, Long itemId);
    
    PurchaseOrder updatePurchaseOrderTotals(Long purchaseOrderId);
    
    PurchaseOrder submitForApproval(Long purchaseOrderId);
    
    PurchaseOrder approve(Long purchaseOrderId, User approver);
    
    PurchaseOrder placeOrder(Long purchaseOrderId);
    
    PurchaseOrder cancel(Long purchaseOrderId, User cancelledBy, String reason);
    
    PurchaseOrder updateReceiptStatus(Long purchaseOrderId);
    
    PurchaseOrder close(Long purchaseOrderId);
    
    void deletePurchaseOrder(Long id);
    
    Map<String, Long> getPurchaseOrderCountsByStatus();
}