package com.ingestion.opd.repository;

import com.ingestion.opd.model.OpdBill;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpdBillRepository extends JpaRepository<OpdBill, Long> {

    Optional<OpdBill> findByBillNumber(String billNumber);
    
    Optional<OpdBill> findByOpdVisit(OpdVisit opdVisit);
    
    List<OpdBill> findByPaymentStatus(OpdBill.PaymentStatus paymentStatus);
    
    List<OpdBill> findByBillDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT b FROM OpdBill b WHERE b.opdVisit.patient = :patient ORDER BY b.billDate DESC")
    List<OpdBill> findByPatient(@Param("patient") Patient patient);
    
    @Query("SELECT SUM(b.totalAmount) FROM OpdBill b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.paymentStatus = 'PAID'")
    BigDecimal getTotalRevenueForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(b.totalAmount) FROM OpdBill b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.paymentStatus IN ('PENDING', 'PARTIAL')")
    BigDecimal getTotalPendingAmountForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM OpdBill b WHERE b.paymentStatus IN :statuses AND b.billDate BETWEEN :startDate AND :endDate")
    List<OpdBill> findByPaymentStatusAndDateRange(
            @Param("statuses") List<OpdBill.PaymentStatus> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) FROM OpdBill b WHERE b.billDate BETWEEN :startDate AND :endDate")
    Long countBillsForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}