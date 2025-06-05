package com.ingestion.billing.service;

import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentService {

    Payment savePayment(Payment payment);
    
    Optional<Payment> findById(Long id);
    
    Optional<Payment> findByPaymentNumber(String paymentNumber);
    
    List<Payment> findAll();
    
    List<Payment> findByInvoice(Invoice invoice);
    
    List<Payment> findByPatient(Patient patient);
    
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Payment> findByIsRefunded(Boolean isRefunded);
    
    List<Payment> findByPatientOrderByPaymentDateDesc(Long patientId);
    
    List<Payment> findByInvoiceOrderByPaymentDateDesc(Long invoiceId);
    
    List<Payment> findByPaymentMethodOrderByPaymentDateDesc(Payment.PaymentMethod paymentMethod);
    
    Page<Payment> findRecentPayments(LocalDateTime startDate, Pageable pageable);
    
    Double getTotalPaymentAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Double getTotalRefundAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Long countByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    Long countRefundedPayments();
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Payment createPayment(Invoice invoice, Patient patient, BigDecimal amount, 
                         Payment.PaymentMethod paymentMethod, String paymentReference, 
                         String transactionId, String notes, User receivedBy);
    
    Payment createPaymentWithoutInvoice(Patient patient, BigDecimal amount, 
                                      Payment.PaymentMethod paymentMethod, String paymentReference, 
                                      String transactionId, String notes, User receivedBy);
    
    Payment refundPayment(Long paymentId, User refundedBy, String reason, String refundReference);
    
    void deletePayment(Long id);
    
    // Additional methods for the controller
    Page<Payment> findAll(Pageable pageable);
    
    Page<Payment> findByPaymentMethod(String method, Pageable pageable);
    
    Page<Payment> findByPaymentDate(java.time.LocalDate paymentDate, Pageable pageable);
    
    List<Payment> findRecentPayments(int limit);
}