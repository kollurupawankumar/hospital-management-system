package com.ingestion.common.repository;

import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.model.supplier.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    
    Optional<PurchaseOrder> findByPoNumber(String poNumber);
    
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
    
    Page<PurchaseOrder> findByOrderDateAfter(LocalDateTime startDate, Pageable pageable);
    
    Page<PurchaseOrder> findByOrderTypeOrderByOrderDateDesc(
            PurchaseOrder.OrderType orderType, Pageable pageable);
    
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status IN :statuses ORDER BY po.orderDate DESC")
    List<PurchaseOrder> findByStatusesOrderByOrderDate(@Param("statuses") List<PurchaseOrder.PurchaseOrderStatus> statuses);
    
    @Query("SELECT po FROM PurchaseOrder po WHERE po.expectedDeliveryDate < CURRENT_DATE AND po.status NOT IN ('RECEIVED', 'CANCELLED', 'CLOSED')")
    List<PurchaseOrder> findOverduePurchaseOrders();
    
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status = :status")
    Long countByStatus(@Param("status") PurchaseOrder.PurchaseOrderStatus status);
    
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.orderType = :orderType AND po.status = :status")
    Long countByOrderTypeAndStatus(@Param("orderType") PurchaseOrder.OrderType orderType, 
                                  @Param("status") PurchaseOrder.PurchaseOrderStatus status);
    
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.orderDate BETWEEN :startDate AND :endDate")
    Long countByOrderDateBetween(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.orderType = :orderType AND po.orderDate BETWEEN :startDate AND :endDate")
    Long countByOrderTypeAndOrderDateBetween(@Param("orderType") PurchaseOrder.OrderType orderType,
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT po.status as status, COUNT(po) as count FROM PurchaseOrder po GROUP BY po.status")
    List<Map<String, Object>> getPurchaseOrderCountsByStatus();
    
    @Query("SELECT po.orderType as orderType, po.status as status, COUNT(po) as count FROM PurchaseOrder po GROUP BY po.orderType, po.status")
    List<Map<String, Object>> getPurchaseOrderCountsByTypeAndStatus();
    
    @Query("SELECT SUM(po.grandTotal) FROM PurchaseOrder po WHERE po.status = :status AND po.orderDate BETWEEN :startDate AND :endDate")
    Double getTotalAmountByStatusAndDateRange(@Param("status") PurchaseOrder.PurchaseOrderStatus status,
                                            @Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT po FROM PurchaseOrder po WHERE " +
           "LOWER(po.orderNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(po.poNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(po.supplier.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PurchaseOrder> searchPurchaseOrders(@Param("searchTerm") String searchTerm);
    
    boolean existsByOrderNumber(String orderNumber);
    
    boolean existsByPoNumber(String poNumber);
    
    boolean existsByOrderNumberAndIdNot(String orderNumber, Long id);
    
    boolean existsByPoNumberAndIdNot(String poNumber, Long id);
}