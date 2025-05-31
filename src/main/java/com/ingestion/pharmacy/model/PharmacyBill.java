package com.ingestion.pharmacy.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pharmacy_bills")
public class PharmacyBill extends BaseEntity {

    @Column(name = "bill_number", nullable = false, unique = true)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billDate;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "grand_total", precision = 10, scale = 2)
    private BigDecimal grandTotal;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "notes", length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "pharmacyBill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PharmacyBillItem> billItems = new ArrayList<>();

    public enum PaymentStatus {
        PENDING, PARTIAL, PAID, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (billNumber == null || billNumber.isEmpty()) {
            // Generate bill number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            billNumber = "PH-BILL-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
        if (billDate == null) {
            billDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
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

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<PharmacyBillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<PharmacyBillItem> billItems) {
        this.billItems = billItems;
    }

    // Helper methods
    public void addBillItem(PharmacyBillItem item) {
        billItems.add(item);
        item.setPharmacyBill(this);
    }

    public void removeBillItem(PharmacyBillItem item) {
        billItems.remove(item);
        item.setPharmacyBill(null);
    }

    public void calculateTotals() {
        BigDecimal total = BigDecimal.ZERO;
        
        for (PharmacyBillItem item : billItems) {
            total = total.add(item.getTotalPrice());
        }
        
        this.totalAmount = total;
        
        // Calculate grand total
        this.grandTotal = this.totalAmount;
        
        if (this.discountAmount != null) {
            this.grandTotal = this.grandTotal.subtract(this.discountAmount);
        }
        
        if (this.taxAmount != null) {
            this.grandTotal = this.grandTotal.add(this.taxAmount);
        }
    }

    public void markAsPaid(String paymentMethod, String paymentReference) {
        this.paymentStatus = PaymentStatus.PAID;
        this.paymentDate = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
        
        // Update prescription payment status if applicable
        if (this.prescription != null) {
            this.prescription.markAsPaid();
        }
    }

    public void markAsPartiallyPaid(String paymentMethod, String paymentReference) {
        this.paymentStatus = PaymentStatus.PARTIAL;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
    }

    public void cancel() {
        this.paymentStatus = PaymentStatus.CANCELLED;
    }
}