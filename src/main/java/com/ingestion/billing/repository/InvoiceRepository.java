package com.ingestion.billing.repository;

import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Invoice.PaymentStatus;
import com.ingestion.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    List<Invoice> findByPatient(Patient patient);
    
    List<Invoice> findByPatientId(Long patientId);
    
    List<Invoice> findByInvoiceType(Invoice.InvoiceType invoiceType);
    
    List<Invoice> findByStatus(Invoice.InvoiceStatus status);
    
    List<Invoice> findByPaymentStatus(Invoice.PaymentStatus paymentStatus);
    
    List<Invoice> findByInvoiceDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Invoice> findByDueDateBefore(LocalDateTime date);
    
    List<Invoice> findByIsCancelled(Boolean isCancelled);
    
    @Query("SELECT i FROM Invoice i WHERE i.patient.id = :patientId ORDER BY i.invoiceDate DESC")
    List<Invoice> findByPatientOrderByInvoiceDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT i FROM Invoice i WHERE i.patient.id = :patientId ORDER BY i.invoiceDate DESC")
    List<Invoice> findByPatientIdOrderByDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT i FROM Invoice i WHERE i.patient.id = :patientId AND i.paymentStatus = :status ORDER BY i.invoiceDate DESC")
    List<Invoice> findByPatientIdAndStatus(
            @Param("patientId") Long patientId,
            @Param("status") PaymentStatus status);
    
    @Query("SELECT i FROM Invoice i WHERE i.patient.id = :patientId AND i.invoiceDate BETWEEN :startDate AND :endDate ORDER BY i.invoiceDate DESC")
    List<Invoice> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT i FROM Invoice i WHERE i.status = :status ORDER BY i.invoiceDate DESC")
    List<Invoice> findByStatusOrderByInvoiceDateDesc(@Param("status") Invoice.InvoiceStatus status);
    
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = :paymentStatus ORDER BY i.invoiceDate DESC")
    List<Invoice> findByPaymentStatusOrderByInvoiceDateDesc(@Param("paymentStatus") Invoice.PaymentStatus paymentStatus);
    
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate >= :startDate ORDER BY i.invoiceDate DESC")
    Page<Invoice> findRecentInvoices(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < CURRENT_DATE AND i.paymentStatus != 'PAID' AND i.isCancelled = false ORDER BY i.dueDate ASC")
    List<Invoice> findOverdueInvoices();
    
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :currentDate AND i.paymentStatus IN ('PENDING', 'PARTIAL') ORDER BY i.dueDate ASC")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT SUM(i.grandTotal) FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate AND i.isCancelled = false")
    Double getTotalInvoiceAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(i.paidAmount) FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate AND i.isCancelled = false")
    Double getTotalPaidAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    Long countByStatus(@Param("status") Invoice.InvoiceStatus status);
    
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.paymentStatus = :paymentStatus")
    Long countByPaymentStatus(@Param("paymentStatus") Invoice.PaymentStatus paymentStatus);
    
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.patient.id = :patientId AND i.paymentStatus = 'PAID'")
    Double getTotalPaidAmountByPatient(@Param("patientId") Long patientId);
    
    @Query("SELECT SUM(i.balanceAmount) FROM Invoice i WHERE i.patient.id = :patientId AND i.paymentStatus IN ('PENDING', 'PARTIAL')")
    Double getTotalOutstandingAmountByPatient(@Param("patientId") Long patientId);
    
    // Additional methods for the controller
    Page<Invoice> findByStatus(Invoice.InvoiceStatus status, Pageable pageable);
    
    Page<Invoice> findByPatientFirstNameContainingIgnoreCaseOrPatientLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);
    
    List<Invoice> findTop5ByOrderByInvoiceDateDesc();
    
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.paymentStatus = :paymentStatus")
    java.math.BigDecimal sumTotalAmountByPaymentStatus(@Param("paymentStatus") Invoice.PaymentStatus paymentStatus);
}