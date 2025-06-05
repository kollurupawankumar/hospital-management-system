package com.ingestion.billing.repository;

import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentNumber(String paymentNumber);
    
    List<Payment> findByInvoice(Invoice invoice);
    
    List<Payment> findByPatient(Patient patient);
    
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Payment> findByIsRefunded(Boolean isRefunded);
    
    @Query("SELECT p FROM Payment p WHERE p.patient.id = :patientId ORDER BY p.paymentDate DESC")
    List<Payment> findByPatientOrderByPaymentDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT p FROM Payment p WHERE p.invoice.id = :invoiceId ORDER BY p.paymentDate DESC")
    List<Payment> findByInvoiceOrderByPaymentDateDesc(@Param("invoiceId") Long invoiceId);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod ORDER BY p.paymentDate DESC")
    List<Payment> findByPaymentMethodOrderByPaymentDateDesc(@Param("paymentMethod") Payment.PaymentMethod paymentMethod);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDate >= :startDate ORDER BY p.paymentDate DESC")
    Page<Payment> findRecentPayments(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate AND p.isRefunded = false")
    Double getTotalPaymentAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate AND p.isRefunded = true")
    Double getTotalRefundAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod = :paymentMethod")
    Long countByPaymentMethod(@Param("paymentMethod") Payment.PaymentMethod paymentMethod);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.isRefunded = true")
    Long countRefundedPayments();
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Additional methods for the controller
    Page<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod, Pageable pageable);
    
    Page<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<Payment> findTop5ByOrderByPaymentDateDesc();
}