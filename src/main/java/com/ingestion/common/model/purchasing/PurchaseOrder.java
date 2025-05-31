package com.ingestion.common.model.purchasing;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends BaseEntity {

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "po_number", unique = true)
    private String poNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "store_id")
    private Long storeId; // Reference to store for inventory orders

    @Column(name = "department_id")
    private Long departmentId; // Reference to department for pharmacy orders

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status = PurchaseOrderStatus.DRAFT;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType = OrderType.GENERAL;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name = "shipping_amount", precision = 10, scale = 2)
    private BigDecimal shippingAmount = BigDecimal.ZERO;

    @Column(name = "grand_total", precision = 10, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "payment_terms", length = 500)
    private String paymentTerms;

    @Column(name = "shipping_terms", length = 500)
    private String shippingTerms;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();

    public enum PurchaseOrderStatus {
        DRAFT, PENDING_APPROVAL, APPROVED, PLACED, PARTIALLY_RECEIVED, 
        RECEIVED, CANCELLED, REJECTED, CLOSED
    }

    public enum PaymentStatus {
        PENDING, PARTIALLY_PAID, PAID, OVERDUE
    }

    public enum OrderType {
        GENERAL, PHARMACY, INVENTORY, MEDICAL_EQUIPMENT, LABORATORY, FOOD_SERVICES
    }

    @PrePersist
    protected void onCreate() {
        if (orderNumber == null || orderNumber.isEmpty()) {
            // Generate order number based on type
            String prefix = getOrderPrefix();
            orderNumber = prefix + "-" + System.nanoTime() % 100000;
        }
        if (poNumber == null || poNumber.isEmpty()) {
            poNumber = orderNumber; // Use same as order number if not set
        }
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    private String getOrderPrefix() {
        switch (orderType) {
            case PHARMACY:
                return "PO-PH";
            case INVENTORY:
                return "PO-INV";
            case MEDICAL_EQUIPMENT:
                return "PO-MED";
            case LABORATORY:
                return "PO-LAB";
            case FOOD_SERVICES:
                return "PO-FOOD";
            default:
                return "PO-GEN";
        }
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getShippingTerms() {
        return shippingTerms;
    }

    public void setShippingTerms(String shippingTerms) {
        this.shippingTerms = shippingTerms;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(LocalDateTime cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public User getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(User cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItem> items) {
        this.items = items;
    }

    // Helper methods
    public void addItem(PurchaseOrderItem item) {
        items.add(item);
        item.setPurchaseOrder(this);
    }

    public void removeItem(PurchaseOrderItem item) {
        items.remove(item);
        item.setPurchaseOrder(null);
    }

    public void calculateTotals() {
        BigDecimal itemsTotal = items.stream()
                .map(PurchaseOrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = itemsTotal;
        
        BigDecimal netAmount = itemsTotal.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        BigDecimal totalTax = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        BigDecimal shipping = shippingCost != null ? shippingCost : 
                             (shippingAmount != null ? shippingAmount : BigDecimal.ZERO);
        
        this.grandTotal = netAmount.add(totalTax).add(shipping);
    }

    public boolean isEditable() {
        return status == PurchaseOrderStatus.DRAFT || status == PurchaseOrderStatus.PENDING_APPROVAL;
    }

    public boolean isCancellable() {
        return status != PurchaseOrderStatus.CANCELLED && 
               status != PurchaseOrderStatus.RECEIVED && 
               status != PurchaseOrderStatus.CLOSED;
    }

    public void approve(User approver) {
        this.status = PurchaseOrderStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedDate = LocalDateTime.now();
    }

    public void cancel(User cancelledBy, String reason) {
        this.status = PurchaseOrderStatus.CANCELLED;
        this.cancelledBy = cancelledBy;
        this.cancelledDate = LocalDateTime.now();
        this.cancellationReason = reason;
    }

    public void markAsReceived() {
        this.status = PurchaseOrderStatus.RECEIVED;
        if (this.deliveryDate == null) {
            this.deliveryDate = LocalDate.now();
        }
    }

    public void markAsPartiallyReceived() {
        this.status = PurchaseOrderStatus.PARTIALLY_RECEIVED;
    }

    public void markAsPaid(String paymentMethod, String paymentReference) {
        this.paymentStatus = PaymentStatus.PAID;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
    }

    public void markAsPartiallyPaid(String paymentMethod, String paymentReference) {
        this.paymentStatus = PaymentStatus.PARTIALLY_PAID;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
    }

    public void updateReceiptStatus() {
        // Check if all items have been received
        boolean allItemsReceived = items.stream()
                .allMatch(item -> item.getReceivedQuantity() != null && 
                         item.getReceivedQuantity() >= item.getQuantity());
        
        boolean anyItemsReceived = items.stream()
                .anyMatch(item -> item.getReceivedQuantity() != null && 
                         item.getReceivedQuantity() > 0);
        
        if (allItemsReceived) {
            markAsReceived();
        } else if (anyItemsReceived) {
            markAsPartiallyReceived();
        }
    }
}