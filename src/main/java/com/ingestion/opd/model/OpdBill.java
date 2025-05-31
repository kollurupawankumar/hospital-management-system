package com.ingestion.opd.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opd_bills")
public class OpdBill extends BaseEntity {

    @Column(name = "bill_number", nullable = false, unique = true)
    private String billNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opd_visit_id", nullable = false)
    private OpdVisit opdVisit;

    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billDate;

    @Column(name = "consultation_fee", nullable = false)
    private BigDecimal consultationFee;

    @Column(name = "medication_fee")
    private BigDecimal medicationFee;

    @Column(name = "lab_fee")
    private BigDecimal labFee;

    @Column(name = "other_charges")
    private BigDecimal otherCharges;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpdBillItem> billItems = new ArrayList<>();

    public enum PaymentStatus {
        PENDING, PARTIAL, PAID, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (billNumber == null || billNumber.isEmpty()) {
            // Generate bill number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            billNumber = "OPD-BILL-" + datePrefix + "-" + System.nanoTime() % 10000;
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

    public OpdVisit getOpdVisit() {
        return opdVisit;
    }

    public void setOpdVisit(OpdVisit opdVisit) {
        this.opdVisit = opdVisit;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimal getMedicationFee() {
        return medicationFee;
    }

    public void setMedicationFee(BigDecimal medicationFee) {
        this.medicationFee = medicationFee;
    }

    public BigDecimal getLabFee() {
        return labFee;
    }

    public void setLabFee(BigDecimal labFee) {
        this.labFee = labFee;
    }

    public BigDecimal getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(BigDecimal otherCharges) {
        this.otherCharges = otherCharges;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<OpdBillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<OpdBillItem> billItems) {
        this.billItems = billItems;
    }

    // Helper methods
    public void addBillItem(OpdBillItem billItem) {
        billItems.add(billItem);
        billItem.setBill(this);
    }

    public void removeBillItem(OpdBillItem billItem) {
        billItems.remove(billItem);
        billItem.setBill(null);
    }

    public void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        
        // Add consultation fee
        if (consultationFee != null) {
            total = total.add(consultationFee);
        }
        
        // Add medication fee
        if (medicationFee != null) {
            total = total.add(medicationFee);
        }
        
        // Add lab fee
        if (labFee != null) {
            total = total.add(labFee);
        }
        
        // Add other charges
        if (otherCharges != null) {
            total = total.add(otherCharges);
        }
        
        // Subtract discount
        if (discount != null) {
            total = total.subtract(discount);
        }
        
        // Add tax
        if (tax != null) {
            total = total.add(tax);
        }
        
        this.totalAmount = total;
    }

    public void markAsPaid(BigDecimal paidAmount, String paymentMethod, String paymentReference) {
        this.paidAmount = paidAmount;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
        this.paymentDate = LocalDateTime.now();
        
        if (paidAmount.compareTo(totalAmount) >= 0) {
            this.paymentStatus = PaymentStatus.PAID;
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.paymentStatus = PaymentStatus.PARTIAL;
        }
    }

    public void cancel() {
        this.paymentStatus = PaymentStatus.CANCELLED;
    }
}