package com.ingestion.billing.service;

import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.InvoiceItem;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    Invoice saveInvoice(Invoice invoice);
    
    Optional<Invoice> findById(Long id);
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    List<Invoice> findAll();
    
    List<Invoice> findByPatient(Patient patient);
    
    List<Invoice> findByInvoiceType(Invoice.InvoiceType invoiceType);
    
    List<Invoice> findByStatus(Invoice.InvoiceStatus status);
    
    List<Invoice> findByPaymentStatus(Invoice.PaymentStatus paymentStatus);
    
    List<Invoice> findByInvoiceDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Invoice> findByDueDateBefore(LocalDateTime date);
    
    List<Invoice> findByIsCancelled(Boolean isCancelled);
    
    List<Invoice> findByPatientOrderByInvoiceDateDesc(Long patientId);
    
    List<Invoice> findByStatusOrderByInvoiceDateDesc(Invoice.InvoiceStatus status);
    
    List<Invoice> findByPaymentStatusOrderByInvoiceDateDesc(Invoice.PaymentStatus paymentStatus);
    
    Page<Invoice> findRecentInvoices(LocalDateTime startDate, Pageable pageable);
    
    List<Invoice> findOverdueInvoices();
    
    Double getTotalInvoiceAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Double getTotalPaidAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Long countByStatus(Invoice.InvoiceStatus status);
    
    Long countByPaymentStatus(Invoice.PaymentStatus paymentStatus);
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Invoice createInvoice(Patient patient, Invoice.InvoiceType invoiceType, LocalDateTime dueDate, 
                         String notes, User createdBy, List<InvoiceItem> items);
    
    Invoice addItemToInvoice(Long invoiceId, InvoiceItem.ServiceType serviceType, Long serviceId, 
                            String serviceCode, String description, Integer quantity, BigDecimal unitPrice, 
                            BigDecimal discountPercentage, BigDecimal taxPercentage);
    
    Invoice removeItemFromInvoice(Long invoiceId, Long itemId);
    
    Invoice applyDiscount(Long invoiceId, BigDecimal discountAmount);
    
    Invoice applyTax(Long invoiceId, BigDecimal taxAmount);
    
    Invoice calculateTotals(Long invoiceId);
    
    Invoice issue(Long invoiceId);
    
    Invoice markAsOverdue(Long invoiceId);
    
    Invoice cancel(Long invoiceId, User cancelledBy, String reason);
    
    Invoice recordPayment(Long invoiceId, BigDecimal amount, Payment.PaymentMethod paymentMethod, 
                         String paymentReference, String notes, User receivedBy);
    
    Invoice updatePaymentStatus(Long invoiceId);
    
    void checkAndUpdateOverdueInvoices();
    
    void deleteInvoice(Long id);
}