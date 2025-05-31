package com.ingestion.pharmacy.service;

import com.ingestion.pharmacy.model.Medicine;
import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.model.purchasing.PurchaseOrderItem;
import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PurchaseOrderService {

    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);
    
    Optional<PurchaseOrder> findById(Long id);
    
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    
    List<PurchaseOrder> findAll();
    
    List<PurchaseOrder> findBySupplier(Supplier supplier);
    
    List<PurchaseOrder> findByStatus(PurchaseOrder.PurchaseOrderStatus status);
    
    List<PurchaseOrder> findByPaymentStatus(PurchaseOrder.PaymentStatus paymentStatus);
    
    List<PurchaseOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<PurchaseOrder> findByExpectedDeliveryDate(LocalDate expectedDeliveryDate);
    
    List<PurchaseOrder> findByDeliveryDate(LocalDate deliveryDate);
    
    List<PurchaseOrder> findByStatusOrderByOrderDateDesc(PurchaseOrder.PurchaseOrderStatus status);
    
    List<PurchaseOrder> findByStatusInOrderByOrderDateDesc(List<PurchaseOrder.PurchaseOrderStatus> statuses);
    
    List<PurchaseOrder> findBySupplierAndDateRange(Long supplierId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<PurchaseOrder> findByStatusAndSupplier(PurchaseOrder.PurchaseOrderStatus status, Long supplierId);
    
    Page<PurchaseOrder> findRecentOrders(LocalDateTime startDate, Pageable pageable);
    
    Long countByStatus(PurchaseOrder.PurchaseOrderStatus status);
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    PurchaseOrder createPurchaseOrder(Supplier supplier, LocalDate expectedDeliveryDate, String paymentTerms, 
                                     String notes, User createdBy, List<PurchaseOrderItem> items);
    
    PurchaseOrder addItemToPurchaseOrder(Long orderId, Medicine medicine, Integer quantity, 
                                        java.math.BigDecimal unitPrice, java.math.BigDecimal discountPercentage, 
                                        java.math.BigDecimal taxPercentage);
    
    PurchaseOrder removeItemFromPurchaseOrder(Long orderId, Long itemId);
    
    PurchaseOrder updatePurchaseOrderStatus(Long id, PurchaseOrder.PurchaseOrderStatus status);
    
    PurchaseOrder submitForApproval(Long id);
    
    PurchaseOrder approve(Long id, User approver);
    
    PurchaseOrder placeOrder(Long id);
    
    PurchaseOrder receivePartial(Long id);
    
    PurchaseOrder receiveComplete(Long id);
    
    PurchaseOrder cancel(Long id);
    
    PurchaseOrder reject(Long id);
    
    PurchaseOrder markAsPaid(Long id, String paymentMethod, String paymentReference);
    
    PurchaseOrder markAsPartiallyPaid(Long id, String paymentMethod, String paymentReference);
    
    PurchaseOrder receiveOrderItem(Long orderId, Long itemId, Integer quantity, String batchNumber, 
                                  LocalDate expiryDate, LocalDate manufacturingDate);
    
    void deletePurchaseOrder(Long id);
}