package com.ingestion.common.service.impl;

import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.model.purchasing.PurchaseOrderItem;
import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.common.repository.PurchaseOrderRepository;
import com.ingestion.common.service.PurchaseOrderService;
import com.ingestion.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        log.debug("Saving purchase order: {}", purchaseOrder.getOrderNumber());
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findById(Long id) {
        log.debug("Finding purchase order by ID: {}", id);
        return purchaseOrderRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findByOrderNumber(String orderNumber) {
        log.debug("Finding purchase order by order number: {}", orderNumber);
        return purchaseOrderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findByPoNumber(String poNumber) {
        log.debug("Finding purchase order by PO number: {}", poNumber);
        return purchaseOrderRepository.findByPoNumber(poNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findAll() {
        log.debug("Finding all purchase orders");
        return purchaseOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findBySupplier(Supplier supplier) {
        log.debug("Finding purchase orders by supplier: {}", supplier.getName());
        return purchaseOrderRepository.findBySupplier(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findBySupplierId(Long supplierId) {
        log.debug("Finding purchase orders by supplier ID: {}", supplierId);
        return purchaseOrderRepository.findBySupplierId(supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByStoreId(Long storeId) {
        log.debug("Finding purchase orders by store ID: {}", storeId);
        return purchaseOrderRepository.findByStoreId(storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByDepartmentId(Long departmentId) {
        log.debug("Finding purchase orders by department ID: {}", departmentId);
        return purchaseOrderRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByStatus(PurchaseOrder.PurchaseOrderStatus status) {
        log.debug("Finding purchase orders by status: {}", status);
        return purchaseOrderRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByPaymentStatus(PurchaseOrder.PaymentStatus paymentStatus) {
        log.debug("Finding purchase orders by payment status: {}", paymentStatus);
        return purchaseOrderRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByOrderType(PurchaseOrder.OrderType orderType) {
        log.debug("Finding purchase orders by order type: {}", orderType);
        return purchaseOrderRepository.findByOrderType(orderType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding purchase orders between dates: {} and {}", startDate, endDate);
        return purchaseOrderRepository.findByOrderDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByOrderDateAfter(LocalDateTime date) {
        log.debug("Finding purchase orders after date: {}", date);
        return purchaseOrderRepository.findByOrderDateAfter(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByExpectedDeliveryDateBefore(LocalDateTime date) {
        log.debug("Finding purchase orders with expected delivery before: {}", date);
        return purchaseOrderRepository.findByExpectedDeliveryDateBefore(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByStatusOrderByOrderDateDesc(PurchaseOrder.PurchaseOrderStatus status) {
        log.debug("Finding purchase orders by status ordered by date: {}", status);
        return purchaseOrderRepository.findByStatusOrderByOrderDateDesc(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByStatusInOrderByOrderDateDesc(List<PurchaseOrder.PurchaseOrderStatus> statuses) {
        log.debug("Finding purchase orders by statuses: {}", statuses);
        return purchaseOrderRepository.findByStatusInOrderByOrderDateDesc(statuses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByOrderTypeAndStatusOrderByOrderDateDesc(
            PurchaseOrder.OrderType orderType, PurchaseOrder.PurchaseOrderStatus status) {
        log.debug("Finding purchase orders by type {} and status {}", orderType, status);
        return purchaseOrderRepository.findByOrderTypeAndStatusOrderByOrderDateDesc(orderType, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findBySupplierAndOrderDateBetween(
            Supplier supplier, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding purchase orders by supplier {} between dates", supplier.getName());
        return purchaseOrderRepository.findBySupplierAndOrderDateBetween(supplier, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findBySupplierIdAndOrderDateBetween(
            Long supplierId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding purchase orders by supplier ID {} between dates", supplierId);
        return purchaseOrderRepository.findBySupplierIdAndOrderDateBetween(supplierId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByStatusAndSupplierId(
            PurchaseOrder.PurchaseOrderStatus status, Long supplierId) {
        log.debug("Finding purchase orders by status {} and supplier ID {}", status, supplierId);
        return purchaseOrderRepository.findByStatusAndSupplierId(status, supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByStatusesOrderByOrderDate(List<PurchaseOrder.PurchaseOrderStatus> statuses) {
        log.debug("Finding purchase orders by statuses: {}", statuses);
        return purchaseOrderRepository.findByStatusesOrderByOrderDate(statuses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findOverduePurchaseOrders() {
        log.debug("Finding overdue purchase orders");
        return purchaseOrderRepository.findOverduePurchaseOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> findByOrderDateAfter(LocalDateTime startDate, Pageable pageable) {
        log.debug("Finding purchase orders after date with pagination: {}", startDate);
        return purchaseOrderRepository.findByOrderDateAfter(startDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> findByOrderTypeOrderByOrderDateDesc(
            PurchaseOrder.OrderType orderType, Pageable pageable) {
        log.debug("Finding purchase orders by type with pagination: {}", orderType);
        return purchaseOrderRepository.findByOrderTypeOrderByOrderDateDesc(orderType, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByStatus(PurchaseOrder.PurchaseOrderStatus status) {
        log.debug("Counting purchase orders by status: {}", status);
        return purchaseOrderRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByOrderTypeAndStatus(PurchaseOrder.OrderType orderType, 
                                        PurchaseOrder.PurchaseOrderStatus status) {
        log.debug("Counting purchase orders by type {} and status {}", orderType, status);
        return purchaseOrderRepository.countByOrderTypeAndStatus(orderType, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Counting purchase orders between dates: {} and {}", startDate, endDate);
        return purchaseOrderRepository.countByOrderDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByOrderTypeAndOrderDateBetween(PurchaseOrder.OrderType orderType,
                                                  LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Counting purchase orders by type {} between dates", orderType);
        return purchaseOrderRepository.countByOrderTypeAndOrderDateBetween(orderType, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPurchaseOrderCountsByStatus() {
        log.debug("Getting purchase order counts by status");
        return purchaseOrderRepository.getPurchaseOrderCountsByStatus();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPurchaseOrderCountsByTypeAndStatus() {
        log.debug("Getting purchase order counts by type and status");
        return purchaseOrderRepository.getPurchaseOrderCountsByTypeAndStatus();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalAmountByStatusAndDateRange(PurchaseOrder.PurchaseOrderStatus status,
                                                   LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Getting total amount by status {} between dates", status);
        return purchaseOrderRepository.getTotalAmountByStatusAndDateRange(status, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> searchPurchaseOrders(String searchTerm) {
        log.debug("Searching purchase orders with term: {}", searchTerm);
        return purchaseOrderRepository.searchPurchaseOrders(searchTerm);
    }

    @Override
    public PurchaseOrder createPurchaseOrder(Supplier supplier, PurchaseOrder.OrderType orderType,
                                           LocalDate expectedDeliveryDate, String paymentTerms, 
                                           String shippingTerms, String notes, User createdBy) {
        log.debug("Creating purchase order for supplier: {}", supplier.getName());

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setOrderType(orderType);
        purchaseOrder.setExpectedDeliveryDate(expectedDeliveryDate);
        purchaseOrder.setPaymentTerms(paymentTerms);
        purchaseOrder.setShippingTerms(shippingTerms);
        purchaseOrder.setNotes(notes);
        purchaseOrder.setCreatedBy(createdBy != null ? createdBy.getUsername() : null);
        purchaseOrder.setStatus(PurchaseOrder.PurchaseOrderStatus.DRAFT);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    public PurchaseOrder createPurchaseOrder(Supplier supplier, Long storeId, Long departmentId,
                                           PurchaseOrder.OrderType orderType, LocalDate expectedDeliveryDate, 
                                           String paymentTerms, String shippingTerms, String notes, 
                                           User createdBy, List<PurchaseOrderItem> items) {
        log.debug("Creating purchase order with items for supplier: {}", supplier.getName());

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setStoreId(storeId);
        purchaseOrder.setDepartmentId(departmentId);
        purchaseOrder.setOrderType(orderType);
        purchaseOrder.setExpectedDeliveryDate(expectedDeliveryDate);
        purchaseOrder.setPaymentTerms(paymentTerms);
        purchaseOrder.setShippingTerms(shippingTerms);
        purchaseOrder.setNotes(notes);
        purchaseOrder.setCreatedBy(createdBy != null ? createdBy.getUsername() : null);
        purchaseOrder.setStatus(PurchaseOrder.PurchaseOrderStatus.DRAFT);

        // Add items
        if (items != null) {
            for (PurchaseOrderItem item : items) {
                purchaseOrder.addItem(item);
            }
        }

        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
        savedOrder.calculateTotals();
        
        return purchaseOrderRepository.save(savedOrder);
    }

    @Override
    public PurchaseOrder addItemToPurchaseOrder(Long purchaseOrderId, Long itemId, Long medicineId,
                                              String itemName, String itemCode, String description,
                                              String unitOfMeasure, Integer quantity, BigDecimal unitPrice, 
                                              BigDecimal discountPercentage, BigDecimal taxPercentage) {
        log.debug("Adding item to purchase order ID: {}", purchaseOrderId);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            
            if (!order.isEditable()) {
                throw new IllegalStateException("Purchase order is not editable in current status: " + order.getStatus());
            }

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setItemId(itemId);
            item.setMedicineId(medicineId);
            item.setItemName(itemName);
            item.setItemCode(itemCode);
            item.setDescription(description);
            item.setUnitOfMeasure(unitOfMeasure);
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);
            item.setDiscountPercentage(discountPercentage);
            item.setTaxPercentage(taxPercentage);

            order.addItem(item);
            order.calculateTotals();

            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder removeItemFromPurchaseOrder(Long purchaseOrderId, Long itemId) {
        log.debug("Removing item {} from purchase order ID: {}", itemId, purchaseOrderId);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            
            if (!order.isEditable()) {
                throw new IllegalStateException("Purchase order is not editable in current status: " + order.getStatus());
            }

            order.getItems().removeIf(item -> item.getId().equals(itemId));
            order.calculateTotals();

            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder updatePurchaseOrderTotals(Long purchaseOrderId) {
        log.debug("Updating totals for purchase order ID: {}", purchaseOrderId);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.calculateTotals();
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder updatePurchaseOrderStatus(Long id, PurchaseOrder.PurchaseOrderStatus status) {
        log.debug("Updating purchase order {} status to {}", id, status);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.setStatus(status);
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + id);
        }
    }

    @Override
    public PurchaseOrder submitForApproval(Long purchaseOrderId) {
        log.debug("Submitting purchase order {} for approval", purchaseOrderId);
        return updatePurchaseOrderStatus(purchaseOrderId, PurchaseOrder.PurchaseOrderStatus.PENDING_APPROVAL);
    }

    @Override
    public PurchaseOrder approve(Long purchaseOrderId, User approver) {
        log.debug("Approving purchase order {} by {}", purchaseOrderId, approver.getUsername());

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.approve(approver);
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder placeOrder(Long purchaseOrderId) {
        log.debug("Placing purchase order {}", purchaseOrderId);
        return updatePurchaseOrderStatus(purchaseOrderId, PurchaseOrder.PurchaseOrderStatus.PLACED);
    }

    @Override
    public PurchaseOrder receivePartial(Long purchaseOrderId) {
        log.debug("Marking purchase order {} as partially received", purchaseOrderId);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.markAsPartiallyReceived();
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder receiveComplete(Long purchaseOrderId) {
        log.debug("Marking purchase order {} as completely received", purchaseOrderId);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.markAsReceived();
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder cancel(Long purchaseOrderId, User cancelledBy, String reason) {
        log.debug("Cancelling purchase order {} by {}", purchaseOrderId, cancelledBy.getUsername());

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            
            if (!order.isCancellable()) {
                throw new IllegalStateException("Purchase order cannot be cancelled in current status: " + order.getStatus());
            }
            
            order.cancel(cancelledBy, reason);
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder reject(Long purchaseOrderId, User rejectedBy, String reason) {
        log.debug("Rejecting purchase order {} by {}", purchaseOrderId, rejectedBy.getUsername());

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(purchaseOrderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.setStatus(PurchaseOrder.PurchaseOrderStatus.REJECTED);
            order.setCancelledBy(rejectedBy);
            order.setCancelledDate(LocalDateTime.now());
            order.setCancellationReason(reason);
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + purchaseOrderId);
        }
    }

    @Override
    public PurchaseOrder close(Long purchaseOrderId) {
        log.debug("Closing purchase order {}", purchaseOrderId);
        return updatePurchaseOrderStatus(purchaseOrderId, PurchaseOrder.PurchaseOrderStatus.CLOSED);
    }

    @Override
    public PurchaseOrder markAsPaid(Long id, String paymentMethod, String paymentReference) {
        log.debug("Marking purchase order {} as paid", id);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.markAsPaid(paymentMethod, paymentReference);
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + id);
        }
    }

    @Override
    public PurchaseOrder markAsPartiallyPaid(Long id, String paymentMethod, String paymentReference) {
        log.debug("Marking purchase order {} as partially paid", id);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            order.markAsPartiallyPaid(paymentMethod, paymentReference);
            return purchaseOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + id);
        }
    }

    @Override
    public PurchaseOrder receiveOrderItem(Long orderId, Long itemId, Integer quantity, 
                                        String batchNumber, LocalDate expiryDate, 
                                        LocalDate manufacturingDate) {
        log.debug("Receiving item {} for purchase order {}", itemId, orderId);

        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            
            Optional<PurchaseOrderItem> itemOpt = order.getItems().stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst();
            
            if (itemOpt.isPresent()) {
                PurchaseOrderItem item = itemOpt.get();
                item.receiveQuantity(quantity, batchNumber, expiryDate, manufacturingDate);
                
                // Check if all items are fully received
                boolean allItemsReceived = order.getItems().stream()
                        .allMatch(PurchaseOrderItem::isFullyReceived);
                
                if (allItemsReceived) {
                    order.markAsReceived();
                } else {
                    order.markAsPartiallyReceived();
                }
                
                return purchaseOrderRepository.save(order);
            } else {
                throw new IllegalArgumentException("Item not found with ID: " + itemId);
            }
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + orderId);
        }
    }

    @Override
    public void deletePurchaseOrder(Long id) {
        log.debug("Deleting purchase order with ID: {}", id);
        
        Optional<PurchaseOrder> orderOpt = purchaseOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            PurchaseOrder order = orderOpt.get();
            
            if (!order.isEditable()) {
                throw new IllegalStateException("Cannot delete purchase order in current status: " + order.getStatus());
            }
            
            purchaseOrderRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Purchase order not found with ID: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOrderNumberUnique(String orderNumber) {
        return !purchaseOrderRepository.existsByOrderNumber(orderNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOrderNumberUnique(String orderNumber, Long excludeId) {
        return !purchaseOrderRepository.existsByOrderNumberAndIdNot(orderNumber, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPoNumberUnique(String poNumber) {
        return !purchaseOrderRepository.existsByPoNumber(poNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPoNumberUnique(String poNumber, Long excludeId) {
        return !purchaseOrderRepository.existsByPoNumberAndIdNot(poNumber, excludeId);
    }
}