package com.ingestion.common.service;

import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.model.purchasing.PurchaseOrderItem;
import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PurchaseOrderService {
    
    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);
    
    Optional<PurchaseOrder> findById(Long id);
    
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    
    Optional<PurchaseOrder> findByPoNumber(String poNumber);
    
    List<PurchaseOrder> findAll();
    
    List<PurchaseOrder> findBySupplier(Supplier supplier);
    
    List<PurchaseOrder> findBySupplierId(Long supplierId);
    
    List<PurchaseOrder> findByStoreId(Long storeId);
    
    List<PurchaseOrder> findByDepartmentId(Long departmentId);
    
    List<PurchaseOrder> findByStatus(PurchaseOrder.PurchaseOrderStatus status);
    
    List<PurchaseOrder> findByPaymentStatus(PurchaseOrder.PaymentStatus paymentStatus);
    
    List<PurchaseOrder> findByOrderType(PurchaseOrder.OrderType orderType);
    
    List<PurchaseOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<PurchaseOrder> findByOrderDateAfter(LocalDateTime date);
    
    List<PurchaseOrder> findByExpectedDeliveryDateBefore(LocalDateTime date);
    
    List<PurchaseOrder> findByStatusOrderByOrderDateDesc(PurchaseOrder.PurchaseOrderStatus status);
    
    List<PurchaseOrder> findByStatusInOrderByOrderDateDesc(List<PurchaseOrder.PurchaseOrderStatus> statuses);
    
    List<PurchaseOrder> findByOrderTypeAndStatusOrderByOrderDateDesc(
            PurchaseOrder.OrderType orderType, PurchaseOrder.PurchaseOrderStatus status);
    
    List<PurchaseOrder> findBySupplierAndOrderDateBetween(
            Supplier supplier, LocalDateTime startDate, LocalDateTime endDate);
    
    List<PurchaseOrder> findBySupplierIdAndOrderDateBetween(
            Long supplierId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<PurchaseOrder> findByStatusAndSupplierId(
            PurchaseOrder.PurchaseOrderStatus status, Long supplierId);
    
    List<PurchaseOrder> findByStatusesOrderByOrderDate(List<PurchaseOrder.PurchaseOrderStatus> statuses);
    
    List<PurchaseOrder> findOverduePurchaseOrders();
    
    Page<PurchaseOrder> findByOrderDateAfter(LocalDateTime startDate, Pageable pageable);
    
    Page<PurchaseOrder> findByOrderTypeOrderByOrderDateDesc(
            PurchaseOrder.OrderType orderType, Pageable pageable);
    
    Long countByStatus(PurchaseOrder.PurchaseOrderStatus status);
    
    Long countByOrderTypeAndStatus(PurchaseOrder.OrderType orderType, 
                                  PurchaseOrder.PurchaseOrderStatus status);
    
    Long countByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Long countByOrderTypeAndOrderDateBetween(PurchaseOrder.OrderType orderType,
                                           LocalDateTime startDate, LocalDateTime endDate);
    
    List<Map<String, Object>> getPurchaseOrderCountsByStatus();
    
    List<Map<String, Object>> getPurchaseOrderCountsByTypeAndStatus();
    
    Double getTotalAmountByStatusAndDateRange(PurchaseOrder.PurchaseOrderStatus status,
                                            LocalDateTime startDate, LocalDateTime endDate);
    
    List<PurchaseOrder> searchPurchaseOrders(String searchTerm);
    
    // Creation methods
    PurchaseOrder createPurchaseOrder(Supplier supplier, PurchaseOrder.OrderType orderType,
                                     LocalDate expectedDeliveryDate, String paymentTerms, 
                                     String shippingTerms, String notes, User createdBy);
    
    PurchaseOrder createPurchaseOrder(Supplier supplier, Long storeId, Long departmentId,
                                     PurchaseOrder.OrderType orderType, LocalDate expectedDeliveryDate, 
                                     String paymentTerms, String shippingTerms, String notes, 
                                     User createdBy, List<PurchaseOrderItem> items);
    
    // Item management
    PurchaseOrder addItemToPurchaseOrder(Long purchaseOrderId, Long itemId, Long medicineId,
                                       String itemName, String itemCode, String description,
                                       String unitOfMeasure, Integer quantity, BigDecimal unitPrice, 
                                       BigDecimal discountPercentage, BigDecimal taxPercentage);
    
    PurchaseOrder removeItemFromPurchaseOrder(Long purchaseOrderId, Long itemId);
    
    PurchaseOrder updatePurchaseOrderTotals(Long purchaseOrderId);
    
    // Status management
    PurchaseOrder updatePurchaseOrderStatus(Long id, PurchaseOrder.PurchaseOrderStatus status);
    
    PurchaseOrder submitForApproval(Long purchaseOrderId);
    
    PurchaseOrder approve(Long purchaseOrderId, User approver);
    
    PurchaseOrder placeOrder(Long purchaseOrderId);
    
    PurchaseOrder receivePartial(Long purchaseOrderId);
    
    PurchaseOrder receiveComplete(Long purchaseOrderId);
    
    PurchaseOrder cancel(Long purchaseOrderId, User cancelledBy, String reason);
    
    PurchaseOrder reject(Long purchaseOrderId, User rejectedBy, String reason);
    
    PurchaseOrder close(Long purchaseOrderId);
    
    // Payment management
    PurchaseOrder markAsPaid(Long id, String paymentMethod, String paymentReference);
    
    PurchaseOrder markAsPartiallyPaid(Long id, String paymentMethod, String paymentReference);
    
    // Item receiving
    PurchaseOrder receiveOrderItem(Long orderId, Long itemId, Integer quantity, 
                                  String batchNumber, LocalDate expiryDate, 
                                  LocalDate manufacturingDate);
    
    void deletePurchaseOrder(Long id);
    
    // Validation methods
    boolean isOrderNumberUnique(String orderNumber);
    
    boolean isOrderNumberUnique(String orderNumber, Long excludeId);
    
    boolean isPoNumberUnique(String poNumber);
    
    boolean isPoNumberUnique(String poNumber, Long excludeId);
}